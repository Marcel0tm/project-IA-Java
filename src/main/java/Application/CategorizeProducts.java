package Application;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;

public class CategorizeProducts {
    public static void main(String[] args) {

        var sc = new Scanner(System.in);

        System.out.print("Digite as categorias válidas de produtos: ");
        var categorias = sc.nextLine();
        System.out.print("Digite o nome do produto: ");
        var user = sc.nextLine();

        var system = """
        Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado
        
        Escolha uma categoria dentra a lista abaixo:
        
        %s
        
        Exemplo de uso:
        
        Pergunta: Bola de Futebol
        Resposta: Esportes
        
        Regras a serem seguidas:
        
        Caso o usuário pergunte algo que não seja sobre categorização de produto, você deve responder que não pode ajudar com isso pois seu papel é apenas categorizar os produtos.
        Caso o usuário informe um produto que não se encaixe em nenhuma das categorias passadas você deve dizer que este produto pertence a categoria de "Outros"
        """.formatted(categorias);

        requests(user, system);

        sc.close();
    }

    public static void requests(String user, String system) {
        var key = System.getenv("KEY_OPENAI");
        var service = new OpenAiService(key, Duration.ofSeconds(30));

        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4")
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), user),
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), system)
                ))
                .build();

        service
                .createChatCompletion(completionRequest)
                .getChoices()
                .forEach(c -> System.out.println(c.getMessage().getContent()));
    }
}
