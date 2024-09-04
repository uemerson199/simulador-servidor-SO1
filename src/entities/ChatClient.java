package entities;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner sc;

    public ChatClient() {
        sc = new Scanner(System.in);
    }

    public void start() throws IOException, InterruptedException {
        clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ChatSever.PORT)) ;
        System.out.println("Cliente conectado ao servidor em : " + SERVER_ADDRESS + ": " + ChatSever.PORT);
        messageLoop();
    }

    private void messageLoop() throws IOException, InterruptedException {
        String path = "C:\\temp\\in.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder requestBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("---")) {
                    // Quando encontrar o delimitador, processa a requisição
                    if (requestBuilder.length() > 0) {
                        String request = requestBuilder.toString().trim();
                        System.out.println("Requisição enviada: " + request);
                        clientSocket.sendMsg(request);
                        requestBuilder.setLength(0); // Limpa o buffer
                        // Adiciona um delay entre o envio de requisições
                        Thread.sleep(1000); // 1000 ms = 1 segundo
                    }
                } else {
                    // Adiciona linha à requisição atual
                    requestBuilder.append(line).append("\n");
                }
            }


            if (requestBuilder.length() > 0) {
                String request = requestBuilder.toString().trim();
                System.out.println("Requisição enviada: " + request);
                clientSocket.sendMsg(request);
                Thread.sleep(1000); // 1000 ms = 1 segundo
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }


        while (sc.hasNextLine()) {
            String msg = sc.nextLine();
            clientSocket.sendMsg(msg);
            Thread.sleep(500);
        }
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