package BackendEcommerce.mundoMagico.Service;
import BackendEcommerce.mundoMagico.Jwt.JwtService;

import BackendEcommerce.mundoMagico.Auth.AuthResponse;
import BackendEcommerce.mundoMagico.Auth.ChangePasswordRequest;
import BackendEcommerce.mundoMagico.Auth.LoginRequest;
import BackendEcommerce.mundoMagico.Auth.RegisterRequest;
import BackendEcommerce.mundoMagico.Exception.UsernameAlreadyExistsException;
import BackendEcommerce.mundoMagico.Jwt.JwtService;
import BackendEcommerce.mundoMagico.Repository.ProductoRepository;
import BackendEcommerce.mundoMagico.Repository.UserRepository;
import BackendEcommerce.mundoMagico.User.Producto.Producto;
import BackendEcommerce.mundoMagico.User.Role;
import BackendEcommerce.mundoMagico.User.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    private ProductoRepository productoRepository;



    public AuthResponse login(LoginRequest request) {
        User userDetails = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), request.getPassword()));

        Integer userId = userDetails.getId_user();  // Usa el ID como Integer

        Map<String, Object> extraClaims = new HashMap<>();
        String token = jwtService.getToken(extraClaims, userDetails, userId.toString());  // Si el método getToken requiere un String para el ID

        return AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .userId(userId)  // Pasa userId como Integer
                .build();
    }



    public AuthResponse register(RegisterRequest request) {
        // Verificar si el username o el email ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadCredentialsException("El nombre de usuario ya está registrado");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadCredentialsException("El correo electrónico ya está registrado");
        }

        // Construcción del objeto usuario
        User user = User.builder()
                .username(request.getUsername())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        // Guardar el usuario en la base de datos
        userRepository.save(user);

        // Crear el mapa de claims extra
        Map<String, Object> extraClaims = new HashMap<>();

        // Obtener el userId
        String userId = String.valueOf(user.getId_user());  // Convierte el userId a String

        // Generar el token
        String token = jwtService.getToken(extraClaims, user, userId);

        // Retornar la respuesta con el token generado
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername()) // Se utiliza el username
                .userId(user.getId_user())    // Se pasa el userId como Integer
                .build();
    }

    // Método para obtener los usuarios registrados
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User updateUserById(Integer id, User updatedUser) {
        // Verificar si el usuario existe
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar si el nuevo username ya está en uso
        if (!user.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new UsernameAlreadyExistsException("El username ya está en uso");
        }

        // Actualizar los detalles del usuario
        if (updatedUser.getLastname() != null) {
            user.setLastname(updatedUser.getLastname());
        }

        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }

        if (updatedUser.getDni() != null) {
            user.setDni(updatedUser.getDni());
        }

        if (updatedUser.getGender() != null) {
            user.setGender(updatedUser.getGender());
        }

        // Guardar el usuario actualizado
        return userRepository.save(user);
    }
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

}



