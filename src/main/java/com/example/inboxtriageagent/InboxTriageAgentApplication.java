package org.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgentApp {

    public static void main(String[] args) {
        SpringApplication.run(AgentApp.class, args);
    }

    @Bean
    CommandLineRunner runner(ChatClient.Builder builder, InboxTools inboxTools) {
        return args -> {
            ChatClient client = builder
                .defaultTools(inboxTools)
                .build();

            String answer = client
                .prompt("List the files in my inbox, read each one, and mark them with a priority (HIGH, MEDIUM, or LOW) based on urgency.")
                .call()
                .content();

            System.out.println("LLM says: " + answer);
        };
    }
}
