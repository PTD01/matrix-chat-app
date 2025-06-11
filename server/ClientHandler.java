// server/ClientHandler.java
import shared.MatrixRequest;
import shared.MatrixResponse;
import shared.MatrixOperation;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            clients.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            clientName = (String) input.readObject();
            broadcast(clientName + " has joined the chat.");

            Object message;
            while ((message = input.readObject()) != null) {
                if (message instanceof String) {
                    broadcast(clientName + ": " + message);
                } else if (message instanceof MatrixRequest) {
                    MatrixRequest req = (MatrixRequest) message;
                    double[][] result = handleMatrix(req);
                    output.writeObject(new MatrixResponse(result));
                }
            }
        } catch (Exception e) {
            System.out.println(clientName + " disconnected.");
        } finally {
            clients.remove(this);
            broadcast(clientName + " has left the chat.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String msg) {
        for (ClientHandler ch : clients) {
            try {
                ch.output.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private double[][] handleMatrix(MatrixRequest req) {
        switch (req.getOperation().toUpperCase()) {
            case "ADD": return MatrixOperation.add(req.getMatrixA(), req.getMatrixB());
            case "SUB": return MatrixOperation.subtract(req.getMatrixA(), req.getMatrixB());
            case "MUL": return MatrixOperation.multiply(req.getMatrixA(), req.getMatrixB());
            case "DIV": return MatrixOperation.divide(req.getMatrixA(), req.getMatrixB());
            default: throw new IllegalArgumentException("Unknown operation: " + req.getOperation());
        }
    }
}
