package BackendEcommerce.mundoMagico.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Integer userId;

    // Constructor manual
    public AuthResponse(String token, String username, Integer userId) {
        this.token = token;
        this.username = username;
        this.userId = userId;
    }
}