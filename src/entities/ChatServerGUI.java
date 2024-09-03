package entities;

import javax.swing.*;
import java.awt.*;

public class ChatServerGUI extends JFrame {
    private JTextArea chatArea;

    public ChatServerGUI() {
        setTitle("Chat Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }
}
