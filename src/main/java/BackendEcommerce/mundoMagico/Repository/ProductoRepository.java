package BackendEcommerce.mundoMagico.Repository;

import BackendEcommerce.mundoMagico.User.Producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Búsqueda por nombre del producto
    List<Producto> findByName(String name);

    // Verifica si existe un producto por nombre
    boolean existsByName(String name);




    // Puedes agregar otros métodos según necesites
}
