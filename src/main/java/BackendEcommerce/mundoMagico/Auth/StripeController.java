package BackendEcommerce.mundoMagico.Auth;

import BackendEcommerce.mundoMagico.Exception.StripeException;
import BackendEcommerce.mundoMagico.Service.ProductoService;
import BackendEcommerce.mundoMagico.User.Producto.Producto;
import BackendEcommerce.mundoMagico.User.Stripe.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StripeController {

    private static final Logger logger = LoggerFactory.getLogger(StripeController.class);
    private final ProductoService productoService;

    static {
        // Configura tu clave secreta de Stripe desde una variable de entorno
        String stripeApiKey = System.getenv("sk_test_51QTBiWGVrTx2ekztpDBHi32qfXdiehHPLpme6ldSCzd9VHaHVpSzegDOubQsj1l2P5gIzeF5NIaeWKsO7c4w79dn00Dlprg5uX");
        if (stripeApiKey == null || stripeApiKey.isEmpty()) {
            logger.error("Clave secreta de Stripe no configurada. Verifica tu entorno.");
        } else {
            Stripe.apiKey = stripeApiKey;
            logger.info("Clave secreta de Stripe configurada correctamente.");
        }
    }




    // Constructor
    public StripeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Crear el PaymentIntent (como ya lo tenías)
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> body) {
        List<Integer> productIds = (List<Integer>) body.get("productId");
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Product IDs are required"));
        }

        // Suponiendo que tienes un servicio para obtener los productos por ID
        List<Producto> productos = productoService.getProductosByIds(productIds);
        if (productos.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Products not found"));
        }

        // Precio en soles
        double priceInSoles = productos.stream().mapToDouble(Producto::getPrice).sum();
        // Tipo de cambio de Sol a USD (ajustar según sea necesario)
        double conversionRate = 0.27;
        // Convertimos el precio a centavos de USD
        long amountInCents = (long) (priceInSoles * conversionRate * 100);

        try {
            // Creamos el PaymentIntent en Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd") // Definir la moneda como USD
                    .putMetadata("productIds", productIds.toString()) // Agregar metadata
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Preparamos la respuesta con el client secret de Stripe
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Si ocurre un error, respondemos con un error 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al procesar el pago con Stripe"));
        }
    }
}
