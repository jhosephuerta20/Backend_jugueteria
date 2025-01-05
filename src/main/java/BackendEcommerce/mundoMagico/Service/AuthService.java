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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        // Intenta autenticar al usuario usando el email
        Optional<UserDetails> userOptional = userRepository.findByEmail(request.getEmail());

        // Si el usuario no se encontró en ninguna de las dos búsquedas, lanza una excepción
        UserDetails user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Autenticación del usuario
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));

        //Genera el token al login
        String token=jwtService.getToken(user);
        String username = user.getUsername();
        return AuthResponse.builder()
                .token(token)
                .username(username)
                .build();

    }

    public AuthResponse register(RegisterRequest request) {
        //CONSTRUCCION DE UN OBJETO USUARIO
        User user = User.builder()
                .username(request.getUsername())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
               // .gender(request.getGender())
              //  .phone(request.getPhone())
             //   .dni(request.getDni())
                .role(Role.USER)
                .build();
        //GUARDA EN LA BD AL USUARIO
        userRepository.save(user);
        //LA CLASE AUTHRESPONSE COMPILA Y DEVUELVE UN TOKEN CREADO EN LA CLASE JWTSERVICE
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .username(user.getUsername())
                .build();

    }

    // Método para obtener los usuarios registrados
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //METODO PARA MOSTRAR UN USUARIO POR SU ID
    public User getUserById(Long id) {
        return userRepository.findById(Math.toIntExact(id))
                .orElseThrow(()-> new IllegalArgumentException("User with ID " + id + " not found"));
    }

    //METODO PARA ACTUALIZAR POR ID
    public User updateUser(Long id, User updatedUser) {
        // Buscar el usuario existente
        User existingUser = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));

        // Actualizar los campos necesarios
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setDni(updatedUser.getDni());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Encriptar la nueva contraseña

        // Guardar los cambios
        return userRepository.save(existingUser);
    }
}