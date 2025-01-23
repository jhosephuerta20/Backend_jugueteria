package BackendEcommerce.mundoMagico.Service;

import BackendEcommerce.mundoMagico.Auth.ChangePasswordRequest;
import BackendEcommerce.mundoMagico.Exception.UserNotFoundException;
import BackendEcommerce.mundoMagico.Exception.UsernameAlreadyExistsException;
import BackendEcommerce.mundoMagico.Exception.existsByUsername;
import BackendEcommerce.mundoMagico.User.User;
import BackendEcommerce.mundoMagico.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
    // Método para encontrar y actualizar usuario por Usuario
    public User updateUserById(Integer id, User updatedUser) {
        // Verificar si el usuario existe
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar si el nuevo username ya está en uso
        if (!user.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new UsernameAlreadyExistsException("El username ya está en uso");
        }

        // Actualizar los detalles del usuario
        if (updatedUser.getLastname() != null) {
            user.setLastname(updatedUser.getLastname());
        }

        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }

        if (updatedUser.getDni() != null) {
            user.setDni(updatedUser.getDni());
        }

        if (updatedUser.getGender() != null) {
            user.setGender(updatedUser.getGender());
        }

        // Guardar el usuario actualizado
        return userRepository.save(user);
    }

    public boolean changePassword(Integer id, ChangePasswordRequest changePasswordRequest) {
        // Buscar al usuario por ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar que la contraseña actual coincida
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            return false; // La contraseña actual es incorrecta
        }

        // Validar que las contraseñas nuevas coincidan
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        // Encriptar la nueva contraseña y actualizar el usuario
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return true;
    }





    public UserDetails getUserById(Integer id) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList() // Aquí se pueden agregar roles si es necesario
            );
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado con ID: " + id);
        }
    }
}

