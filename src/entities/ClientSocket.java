package entities;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSocket {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean closed = false;

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Cliente " + socket.getRemoteSocketAddress() + " se conectou ao servidor.");
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            closed = true;
        } catch (IOException e) {
            System.out.println("Erro ao fechar socket: " + e.getMessage());
        }
    }

    public String getMessage() {
        try {
            if (closed) return null;
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean sendMsg(String msg) {
        if (closed) return false;
        out.println(msg);
        return out.checkError();
    }

    public boolean isClosed() {
        return closed;
    }
}
