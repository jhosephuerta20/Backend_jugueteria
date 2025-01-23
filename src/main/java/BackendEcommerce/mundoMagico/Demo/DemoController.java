package BackendEcommerce.mundoMagico.Demo;

import BackendEcommerce.mundoMagico.Auth.ChangePasswordRequest;
import BackendEcommerce.mundoMagico.Service.AuthService;
import BackendEcommerce.mundoMagico.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  BackendEcommerce.mundoMagico.Service.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {
    private final AuthService authService;
    private UserService userService;
    //END POINT PARA BUSCAR POR ID
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = authService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(authService.updateUserById(id, updatedUser));
    }
    //END POINT PARA ACTUALIZAR POR ID

}