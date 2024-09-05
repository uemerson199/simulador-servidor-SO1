package interfaces;

import entities.ClientSocket;

public class Request implements Comparable<Request> {
    private final ClientSocket clientSocket;
    private int estimatedTime;
    private int priority;

    public Request(ClientSocket clientSocket, int estimatedTime) {
        this.clientSocket = clientSocket;
        this.estimatedTime = estimatedTime;
        this.priority = estimatedTime;
    }


    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void reduceEstimatedTime(int time) {
        this.estimatedTime -= time;
    }

    public int getPriority() {
        return priority;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    @Override
    public int compareTo(Request other) {
        return Integer.compare(this.priority, other.priority);
    }
}
