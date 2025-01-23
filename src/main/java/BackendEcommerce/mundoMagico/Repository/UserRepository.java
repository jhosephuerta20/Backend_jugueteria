package BackendEcommerce.mundoMagico.Repository;

import BackendEcommerce.mundoMagico.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Búsqueda por email
    Optional<User> findByEmail(String email);

    // Búsqueda por username
    Optional<User> findByUsername(String username);

    // Verifica si existe un username
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
