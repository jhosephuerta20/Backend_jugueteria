package BackendEcommerce.mundoMagico.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "586E327235738782F413F424827B4B826506536566B597037376397924";

    // Generador del token (Este es el que necesitas)
    public String getToken(Map<String, Object> extraClaims, UserDetails user, String userId) {
        // Añadir el id al payload del token
        extraClaims.put("id", userId);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername()) // El username se sigue usando como subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generador de token sin parámetros extra (Este método es redundante y puede ser eliminado)
    //public String getToken(UserDetails user, String userId) {
    //    return getToken(new HashMap<>(), user, userId);
    //}

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Obtener el id del usuario desde el token
    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("id", String.class); // Extraer el id desde el payload
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
