package Tests;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;

import java.math.BigDecimal;
import java.util.Locale;

public class TestLibCountTokens {

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
        var quantity = enc.countTokens("Identifique o perfil de compra de cada cliente");

        System.out.println("Quantidade de Tokens: " + quantity);

        var cost = new BigDecimal(quantity)
                .divide(new BigDecimal("1000"))
                .multiply(new BigDecimal("0.0010"));
        System.out.println("Custo aproximado da requisição: R$ " + cost);
    }
}
