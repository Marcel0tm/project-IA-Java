package Application;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.Arrays;

public class CategorizeProducts {
    public static void main(String[] args) {
        var user = "Escova de cabelo";
        var system = "Você é um categorizador de produtos";

        var key = System.getenv("KEY_OPENAI");
        var service = new OpenAiService(key);

        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4")
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), user),
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), system)
                ))
                .n(5)
                .build();

        service
                .createChatCompletion(completionRequest)
                .getChoices()
                .forEach(c -> {
                    System.out.println(c.getMessage().getContent());
                    System.out.println("----------------------------");
                });
    }
}
