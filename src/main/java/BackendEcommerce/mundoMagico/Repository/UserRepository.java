package BackendEcommerce.mundoMagico.Repository;

import BackendEcommerce.mundoMagico.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    //busqueda por email
   Optional<UserDetails> findByEmail(String email);
    //Busqueda por username
  Optional<User> findByUsername(String username);
}
