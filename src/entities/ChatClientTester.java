package entities;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClientTester {
    public static void main(String[] args) {
        int numberOfClients = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfClients);

        for (int i = 0; i < numberOfClients; i++) {
            final int clientId = i;
            executorService.submit(() -> {
                try {
                    ChatClient client = new ChatClient();
                    client.start();
                } catch (IOException e) {
                    System.out.println("Erro ao iniciar cliente " + clientId + ": " + e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
    }
}
