package interfaces;

import entities.ClientSocket;

public class Request implements Comparable<Request> {
    private final ClientSocket clientSocket;
    private final int estimatedTime;
    private int priority;

    public Request(ClientSocket clientSocket, int estimatedTime) {
        this.clientSocket = clientSocket;
        this.estimatedTime = estimatedTime;
        this.priority = estimatedTime;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public int getPriority() {
        return priority;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public void increasePriority() {
        this.priority -= 1;
    }

    @Override
    public int compareTo(Request other) {
        return Integer.compare(this.priority, other.priority);
    }
}