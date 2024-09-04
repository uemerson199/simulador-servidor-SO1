package entities;

import javax.swing.*;
import java.awt.*;

public class ChatServerGUI extends JFrame {
    private JTextArea chatArea;

    public ChatServerGUI() {
        setTitle("Chat Server");
        setSize(600, 400);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    public synchronized void appendRequest(String clientAddress, String requestDetails) {
        chatArea.append("===== Nova Requisição =====\n");
        chatArea.append("Cliente: " + clientAddress + "\n");
        chatArea.append("Detalhes:\n" + requestDetails + "\n");
        chatArea.append("----------------------------\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }


    public synchronized void appendMessage(String message, boolean isRequest) {
        if (isRequest) {
            chatArea.append("===== Nova Requisição =====\n");
            chatArea.append(message + "\n");
        } else {
            chatArea.append(message + "\n");
        }
        chatArea.append("----------------------------\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}