package BackendEcommerce.mundoMagico.Service;

import BackendEcommerce.mundoMagico.Repository.ProductoRepository;
import BackendEcommerce.mundoMagico.User.Producto.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodosProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.orElse(null); // o lanzar excepción si no se encuentra el producto
    }
    public List<Producto> getProductosByIds(List<Integer> productIds) {
        return productoRepository.findAllById(productIds);  // Utilizando el método de JPA para obtener productos por sus IDs
    }

}