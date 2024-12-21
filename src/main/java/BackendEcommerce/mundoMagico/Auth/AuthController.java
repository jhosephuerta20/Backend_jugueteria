package BackendEcommerce.mundoMagico.Auth;

import BackendEcommerce.mundoMagico.Service.AuthService;
import BackendEcommerce.mundoMagico.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Utilizamos la clase authService para acceder a los métodos de login y response, que es el token
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();  // Llama al servicio para obtener todos los usuarios
        return ResponseEntity.ok(users);  // Devuelve los usuarios en la respuesta
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        authService.deleteUser(id); // llama al servicio para eliminar al usuario
        return ResponseEntity.noContent().build(); //devuelve un 204
    }
}