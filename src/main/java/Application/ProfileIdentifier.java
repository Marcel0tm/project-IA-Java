package Application;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;

public class ProfileIdentifier {

    public static void main(String[] args) {

        var systemPrompt = """
                Identifique o perfil de compra de cada cliente.
                
                A resposta deve ser:
                
                Cliente - descreva o perfil do cliente em três palavras
                """;

        var customers = loadCustomersFromFile();

        var quantityTokens = countTokens(customers);
        var model = "gpt-3.5-turbo";
        var sizeOutPut = 2048;
        if (quantityTokens > 4096 - sizeOutPut) {
            model = "gpt-3.5-turbo-16k";
        }

        System.out.println("QUANTITY TOKENS: " + quantityTokens);
        System.out.println("Model: " + model);


        var request = ChatCompletionRequest
                .builder()
                .model(model)
                .maxTokens(sizeOutPut)
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                systemPrompt),
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                customers)))
                .build();

        var key = System.getenv("KEY_OPENAI");
        var service = new OpenAiService(key, Duration.ofSeconds(60));

        System.out.println(
                service
                        .createChatCompletion(request)
                        .getChoices().get(0).getMessage().getContent());
    }

    private static int countTokens(String prompt) {
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
        return enc.countTokens(prompt);
    }


    private static String loadCustomersFromFile() {
        try {
            var path = Path.of(ClassLoader
                    .getSystemResource("lista_de_compras_100_clientes.csv")
                    .toURI());
            return Files.readAllLines(path).toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o arquivo!", e);
        }
    }
}
