package BackendEcommerce.mundoMagico.Stripe;

import org.springframework.beans.factory.annotation.Value;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51QTBiWGVrTx2ekztpDBHi32qfXdiehHPLpme6ldSCzd9VHaHVpSzegDOubQsj1l2P5gIzeF5NIaeWKsO7c4w79dn00Dlprg5uX";
    }
}
