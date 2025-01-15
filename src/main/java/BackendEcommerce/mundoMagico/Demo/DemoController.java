package BackendEcommerce.mundoMagico.Demo;

import BackendEcommerce.mundoMagico.Service.AuthService;
import BackendEcommerce.mundoMagico.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class                                                 DemoController {
    private final AuthService authService;
    //END POINT PARA BUSCAR POR ID
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        User user = authService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    //END POINT PARA ACTUALIZAR POR ID
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = authService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }}
