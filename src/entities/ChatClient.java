package entities;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = ChatSever.PORT;
    private ClientSocket clientSocket;
    private Scanner sc;

    public ChatClient() {
        sc = new Scanner(System.in);
    }

    public void start() throws IOException, InterruptedException {
        clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, SERVER_PORT));
        System.out.println("Cliente conectado ao servidor em: " + SERVER_ADDRESS + ":" + SERVER_PORT);
        messageLoop();
    }

    private void messageLoop() throws IOException, InterruptedException {
        String path = "C:\\temp\\in.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder requestBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim(); // Remove espaços em branco ao redor da linha

                if (line.isEmpty()) {
                    continue; // Ignora linhas vazias
                }

                if (line.equals("---")) {
                    // Quando encontrar o delimitador, processa a requisição
                    if (requestBuilder.length() > 0) {
                        String request = requestBuilder.toString().trim();
                        if (!request.isEmpty()) { // Verifica se a requisição não está vazia
                            System.out.println("Requisição enviada: " + request);
                            clientSocket.sendMsg(request);
                        }
                        requestBuilder.setLength(0); // Limpa o buffer
                        // Adiciona um delay entre o envio de requisições
                        Thread.sleep(1000); // 1000 ms = 1 segundo
                    }
                } else {
                    // Adiciona linha à requisição atual
                    requestBuilder.append(line).append("\n");
                }
            }

            // Envia a última requisição se existir
            if (requestBuilder.length() > 0) {
                String request = requestBuilder.toString().trim();
                if (!request.isEmpty()) { // Verifica se a requisição não está vazia
                    System.out.println("Requisição enviada: " + request);
                    clientSocket.sendMsg(request);
                }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Envia mensagens do console até que o usuário digite "sair"
        while (sc.hasNextLine()) {
            String msg = sc.nextLine().trim();
            if ("sair".equalsIgnoreCase(msg)) {
                clientSocket.sendMsg(msg);
                break;
            }
            if (!msg.isEmpty()) { // Verifica se a mensagem não está vazia
                clientSocket.sendMsg(msg);
            }
            Thread.sleep(500);
        }

        clientSocket.close();
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao iniciar cliente: " + e.getMessage());
        }

        System.out.println("Cliente finalizado!");
    }
}
