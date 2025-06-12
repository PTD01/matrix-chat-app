package client;

import shared.MatrixRequest;
import shared.Operation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientB {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Identify this client
            out.writeObject("ClientB");

            // Start thread to listen for server responses
            new MessageReader(in).start();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n[ClientB] Choose Action:");
                System.out.println("1. Send Chat Message");
                System.out.println("2. Send Matrix Operation");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (choice == 1) {
                    System.out.print("Message: ");
                    String msg = scanner.nextLine();
                    out.writeObject("[ClientB]: " + msg);

                } else if (choice == 2) {
                    System.out.print("Matrix rows: ");
                    int rows = scanner.nextInt();
                    System.out.print("Matrix cols: ");
                    int cols = scanner.nextInt();

                    double[][] matrix1 = MatrixInputHelper.input2DMatrix(rows, cols, scanner);
                    double[][] matrix2 = MatrixInputHelper.input2DMatrix(rows, cols, scanner);

                    System.out.print("Operation (ADD/SUBTRACT/MULTIPLY/DIVIDE): ");
                    String op = scanner.next().toUpperCase();

                    MatrixRequest request = new MatrixRequest(matrix1, matrix2, Operation.valueOf(op));
                    out.writeObject(request);
                }
            }
        } catch (Exception e) {
            System.err.println("ClientB Error: " + e.getMessage());
        }
    }
}
