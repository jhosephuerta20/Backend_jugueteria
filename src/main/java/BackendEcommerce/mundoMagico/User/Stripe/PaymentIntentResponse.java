package BackendEcommerce.mundoMagico.User.Stripe;

public  class PaymentIntentResponse {
    private String clientSecret;

    public PaymentIntentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
