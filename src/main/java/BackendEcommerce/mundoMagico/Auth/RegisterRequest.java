package BackendEcommerce.mundoMagico.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String username;
    String lastname;
    String email;
    String password;
   // String gender;
   // String phone;
   // String dni;

}
