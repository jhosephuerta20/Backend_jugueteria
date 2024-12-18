package BackendEcommerce.mundoMagico.Service;

import BackendEcommerce.mundoMagico.Auth.AuthResponse;
import BackendEcommerce.mundoMagico.Auth.LoginRequest;
import BackendEcommerce.mundoMagico.Auth.RegisterRequest;
import BackendEcommerce.mundoMagico.Jwt.JwtService;
import BackendEcommerce.mundoMagico.Repository.UserRepository;
import BackendEcommerce.mundoMagico.User.Role;
import BackendEcommerce.mundoMagico.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse register(RegisterRequest request) {
        //CONSTRUCCION DE UN OBJETO USUARIO
        User user = User.builder()
                .username(request.getUsername())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .phone(request.getPhone())
                .dni(request.getDni())
                .role(Role.USER)
                .build();
        //GUARDA EN LA BD AL USUARIO
        userRepository.save(user);
        //LA CLASE AUTHRESPONSE COMPILA Y DEVUELVE UN TOKEN CREADO EN LA CLASE JWTSERVICE
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();

    }

    // Método para obtener los usuarios registrados
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}