package BackendEcommerce.mundoMagico.User.Pedidos;

import BackendEcommerce.mundoMagico.User.DetallesPedido.DetallePedido;
import BackendEcommerce.mundoMagico.User.User;
import BackendEcommerce.mundoMagico.User.Producto.Producto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orden", unique = true, nullable = false, length = 50)
    private String numeroOrden;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;
}