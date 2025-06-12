package client;

import shared.MatrixResponse;

import java.io.ObjectInputStream;

public class MessageReader extends Thread {

    private final ObjectInputStream in;

    public MessageReader(ObjectInputStream in) {
        this.in = in;
    }

    public void run() {
        try {
            while (true) {
                Object response = in.readObject();
                if (response instanceof String) {
                    System.out.println("[Server] " + response);
                } else if (response instanceof MatrixResponse matrixResponse) {
                    System.out.println("[Matrix Result]:");
                    double[][] result = matrixResponse.getResult();
                    for (double[] row : result) {
                        for (double val : row) {
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Connection lost.");
        }
    }
}
