package BackendEcommerce.mundoMagico.User;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false, length = 40)
    private String lastname;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Column(nullable = false, unique = true, length = 70)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 10)
    private String gender;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe tener 9 dígitos")
    @Column(length = 9)
    private String phone;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
