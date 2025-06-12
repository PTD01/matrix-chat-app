package client;

import shared.MatrixMessage;
import shared.MatrixMessage.Operation;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ClientA {
    public static void main(String[] args) {
        Scanner scanner = null;
        Socket socket = null;
        try {
            scanner = new Scanner(System.in);
            socket = new Socket("localhost", 12345);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            writer.println(name); // send name to server

            // Start listening for messages
            new client.MessageReader(objectInput).start();

            while (true) {
                System.out.println("\nChoose option:");
                System.out.println("[1] Chat");
                System.out.println("[2] Matrix Operation");
                
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    if (choice == 1) {
                        System.out.print("Enter message: ");
                        String msg = scanner.nextLine();
                        writer.println(msg);
                    } else if (choice == 2) {
                        System.out.println("[1] 2D Matrix\n[2] 3D Matrix");
                        int dim = scanner.nextInt();

                        double[][][] matrixA, matrixB;
                        Operation op;

                        System.out.println("Choose operation: 1.ADD 2.SUB 3.MUL 4.DIV");
                        int opChoice = scanner.nextInt();
                        op = switch (opChoice) {
                            case 1 -> Operation.ADD;
                            case 2 -> Operation.SUBTRACT;
                            case 3 -> Operation.MULTIPLY;
                            case 4 -> Operation.DIVIDE;
                            default -> throw new IllegalArgumentException("Invalid operation.");
                        };

                        if (dim == 1) {
                            System.out.print("Enter number of rows and columns (e.g. 3 3): ");
                            int r = scanner.nextInt(), c = scanner.nextInt();
                            matrixA = new double[1][][];
                            matrixB = new double[1][][];
                            matrixA[0] = client.MatrixInputHelper.input2DMatrix(r, c, scanner);
                            matrixB[0] = client.MatrixInputHelper.input2DMatrix(r, c, scanner);
                        } else if (dim == 2) {
                            System.out.print("Enter depth, rows and columns (e.g. 2 3 3): ");
                            int d = scanner.nextInt(), r = scanner.nextInt(), c = scanner.nextInt();
                            matrixA = client.MatrixInputHelper.input3DMatrix(d, r, c, scanner);
                            matrixB = client.MatrixInputHelper.input3DMatrix(d, r, c, scanner);
                        } else {
                            throw new IllegalArgumentException("Invalid dimension choice. Please choose 1 or 2.");
                        }

                        // Send a signal to server to expect matrix object
                        writer.println("MATRIX_REQUEST");

                        // Send the matrix message
                        MatrixMessage msg = new MatrixMessage(matrixA, matrixB, op);
                        objectOutput.writeObject(msg);
                        objectOutput.flush();
                        System.out.println("[✅] Matrix request sent. Awaiting response...");
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear invalid input
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
}
