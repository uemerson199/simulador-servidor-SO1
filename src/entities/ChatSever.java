package entities;

import interfaces.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatSever {

    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private ChatServerGUI gui;
    private Queue<Request> requestQueue;
    private ExecutorService executorService;
    private final int quantum = 2;

    public ChatSever() {
        gui = new ChatServerGUI();
        gui.setVisible(true);
        requestQueue = new LinkedList<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        gui.appendMessage("Servidor foi iniciado na porta: " + PORT, false);
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

            executorService.submit(this::processRequests);
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

            ClientSocket clientSocket = request.getClientSocket();
            if (request.getEstimatedTime() > 0) {
                int processingTime = Math.min(request.getEstimatedTime(), quantum);
                processRequestForTime(clientSocket, processingTime);

                request.reduceEstimatedTime(processingTime);

                if (request.getEstimatedTime() > 0) {
                    synchronized (requestQueue) {
                        requestQueue.offer(request);
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private void processRequestForTime(ClientSocket clientSocket, int timeToProcess) {
        long endTime = System.currentTimeMillis() + timeToProcess * 1000L;
        try {
            while (System.currentTimeMillis() < endTime) {
                String msg = clientSocket.getMessage();
                if (msg == null) break;
                if ("sair".equalsIgnoreCase(msg)) return;
                String clientAddress = clientSocket.getRemoteSocketAddress().toString();
                gui.appendRequest(clientAddress, msg);
                System.out.println("Requisição recebida do cliente " + clientAddress + ": " + msg);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        try {
            ChatSever server = new ChatSever();
            server.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }

        System.out.println("Servidor finalizado!");
    }
}
