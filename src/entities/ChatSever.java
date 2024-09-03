package entities;

import interfaces.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatSever {

    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private ChatServerGUI gui;
    private PriorityQueue<Request> requestQueue;
    private ExecutorService executorService;

    public ChatSever() {
        gui = new ChatServerGUI();
        gui.setVisible(true);
        requestQueue = new PriorityQueue<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        gui.appendMessage("Servidor foi iniciado na porta: " + PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            int estimatedTime = (int) (Math.random() * 10) + 1;
            Request request = new Request(new ClientSocket(clientSocket), estimatedTime);
            synchronized (requestQueue) {
                requestQueue.offer(request);
                requestQueue.notify();
            }

            executorService.submit(() -> processRequests());
        }
    }

    private void processRequests() {
        while (true) {
            Request request;
            synchronized (requestQueue) {
                while (requestQueue.isEmpty()) {
                    try {
                        requestQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                request = requestQueue.poll();
            }

            for (Request req : requestQueue) {
                req.increasePriority();
            }

            ClientSocket clientSocket = request.getClientSocket();
            clientMessageLoop(clientSocket);
        }
    }

    public void clientMessageLoop(ClientSocket clientSocket) {
        String msg;
        try {
            while ((msg = clientSocket.getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg))
                    return;
                // Usa println para garantir a quebra de linha
                System.out.println(String.format("Requisição recebida do cliente %s: %s", clientSocket.getRemoteSocketAddress(), msg));
            }
        } finally {
            clientSocket.close();
        }
    }


    public static void main(String[] args) {
        try {
            ChatSever server = new ChatSever();
            server.start();
        } catch (IOException e) {
            System.out.println("Error ao iniciar o servidor: " + e.getMessage());
        }

        System.out.println("Servidor finalizado!");
    }
}
