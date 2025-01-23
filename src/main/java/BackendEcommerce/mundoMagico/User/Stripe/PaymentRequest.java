package BackendEcommerce.mundoMagico.User.Stripe;

public class PaymentRequest {

    private Integer amount;  // El monto del pago en centavos
    private String description;  // Descripción del pago

    // Getters y Setters

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}