package BackendEcommerce.mundoMagico.Exception;

public class StripeException extends RuntimeException {
    public StripeException(String message) {
        super(message);
    }
}
