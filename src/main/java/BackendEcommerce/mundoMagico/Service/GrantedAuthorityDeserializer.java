package BackendEcommerce.mundoMagico.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrantedAuthorityDeserializer extends JsonDeserializer<List<GrantedAuthority>> {

    @Override
    public List<GrantedAuthority> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        ArrayNode arrayNode = p.getCodec().readTree(p);

        for (int i = 0; i < arrayNode.size(); i++) {
            String authority = arrayNode.get(i).asText();
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }
}
