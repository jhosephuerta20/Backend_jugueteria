package BackendEcommerce.mundoMagico.Auth;

import BackendEcommerce.mundoMagico.Exception.UserNotFoundException;
import BackendEcommerce.mundoMagico.Exception.UsernameAlreadyExistsException;
import BackendEcommerce.mundoMagico.Jwt.JwtService;
import BackendEcommerce.mundoMagico.Repository.ProductoRepository;
import BackendEcommerce.mundoMagico.Repository.UserRepository;
import BackendEcommerce.mundoMagico.Service.AuthService;
import BackendEcommerce.mundoMagico.Service.ProductoService;
import BackendEcommerce.mundoMagico.Service.UserService;
import BackendEcommerce.mundoMagico.User.Producto.Producto;
import BackendEcommerce.mundoMagico.User.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Utilizamos el servicio de autenticación para obtener el AuthResponse
        AuthResponse response = authService.login(request);  // Deberías llamar al servicio que devuelve AuthResponse
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/obtenerUsuarioPorId/{id_user}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Integer id_user) {
        System.out.println("ID recibido: " + id_user);
        Optional<User> optionalUser = userService.findById(id_user);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado con ID: " + id_user));
        }
    }

    @PutMapping("/actualizarUsuarioPorId/{id}")
    public ResponseEntity<?> actualizarUsuarioPorId(
            @PathVariable Integer id, // Cambiar username a id
            @RequestBody User updatedUser) {
        try {
            User user = userService.updateUserById(id, updatedUser); // Cambiar el método a usar ID
            return ResponseEntity.ok(user); // Devuelve el usuario actualizado
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado con ID: " + id));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al actualizar: " + e.getMessage()));
        }
    }

    @PutMapping("/changePassword/{id}") //se corrigio algunos errores
    public ResponseEntity<?> cambiarContraseña(
            @PathVariable Integer id,
            @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(id, request);
            return ResponseEntity.ok("Contraseña cambiada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado con ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al cambiar la contraseña"));
        }

    }

}
