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

    public void start() throws IOException {
        clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ChatSever.PORT)) ;
        System.out.println("Cliente conectado ao servidor em : " + SERVER_ADDRESS + ": " + ChatSever.PORT);
        messageLoop();
    }

    private void messageLoop() throws IOException {
        // Caminho do arquivo que será lido
        String path = "C:\\temp\\in.txt";

        // Bloco try-with-resources para garantir o fechamento do BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            // Envia cada linha do arquivo como uma mensagem
            while (line != null) {
                System.out.println("Requisição enviada: " + line);
                clientSocket.sendMsg(line);  // Envia a linha lida para o servidor
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Loop para enviar mensagens digitadas pelo usuário
        String msg;
        do {
            System.out.print("Digite uma mensagem (ou sair para finalizar): ");
            msg = sc.nextLine();
            sc.nextLine();
            clientSocket.sendMsg(msg);
        } while(!msg.equalsIgnoreCase("sair"));
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar cliente: " + e.getMessage());
        }

        System.out.println("Cliente finalizado!");
    }
}
