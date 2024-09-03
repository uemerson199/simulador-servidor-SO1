package entities;

import entities.ChatClient;

import java.io.IOException;

public class ChatClientTester {
    public static void main(String[] args) {
        int numberOfClients = 5;
        for (int i = 0; i < numberOfClients; i++) {
            final int clientId = i;
            new Thread(() -> {
                try {
                    ChatClient client = new ChatClient();
                    client.start();
                } catch (IOException e) {
                    System.out.println("Erro ao iniciar cliente " + clientId + ": " + e.getMessage());
                }
            }).start();
        }
    }
}
