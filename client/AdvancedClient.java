package client;

import shared.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Arrays;

/**
 * Advanced client that supports N-dimensional matrix operations
 */
public class AdvancedClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    
    public static void main(String[] args) {
        Scanner scanner = null;
        Socket socket = null;
        
        try {
            scanner = new Scanner(System.in);
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            
            System.out.print("Enter your name: ");
            String clientName = scanner.nextLine();
            
            // Send client identification
            objectOutput.writeObject("ADVANCED_CLIENT:" + clientName);
            objectOutput.flush();
            
            // Start message reader thread
            new AdvancedMessageReader(objectInput).start();
            
            System.out.println("\n=== Advanced N-Dimensional Matrix Client ===");
            System.out.println("Connected as: " + clientName);
            
            while (true) {
                try {
                    showMainMenu();
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    
                    switch (choice) {
                        case 1:
                            handleChatMessage(objectOutput, scanner, clientName);
                            break;
                        case 2:
                            handleMatrixOperation(objectOutput, scanner, clientName);
                            break;
                        case 3:
                            handleQuickMatrixOperation(objectOutput, scanner, clientName);
                            break;
                        case 4:
                            showMatrixExamples();
                            break;
                        case 5:
                            System.out.println("Goodbye!");
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear invalid input
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            closeResources(scanner, socket);
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Send Chat Message");
        System.out.println("2. N-Dimensional Matrix Operation (Custom)");
        System.out.println("3. Quick Matrix Operation (1D/2D/3D)");
        System.out.println("4. Show Matrix Examples");
        System.out.println("5. Exit");
        System.out.print("Choose option: ");
    }
    
    private static void handleChatMessage(ObjectOutputStream out, Scanner scanner, String clientName) 
            throws IOException {
        System.out.print("Enter message: ");
        String message = scanner.nextLine();
        out.writeObject("CHAT:" + clientName + ": " + message);
        out.flush();
    }
    
    private static void handleMatrixOperation(ObjectOutputStream out, Scanner scanner, String clientName) 
            throws IOException {
        try {
            System.out.println("\n=== Custom N-Dimensional Matrix Operation ===");
            
            // Get dimensions for matrices
            System.out.println("Define matrix dimensions (both matrices must have same dimensions):");
            int[] dimensions = MatrixInputHelper.inputDimensions(scanner);
            
            System.out.println("Matrix dimensions: " + Arrays.toString(dimensions));
            System.out.println("Total elements per matrix: " + calculateTotalElements(dimensions));
            
            // Input matrices
            NDMatrix matrixA = MatrixInputHelper.inputNDMatrix("Matrix A", dimensions, scanner);
            NDMatrix matrixB = MatrixInputHelper.inputNDMatrix("Matrix B", dimensions, scanner);
            
            // Choose operation
            Operation operation = chooseOperation(scanner);
            
            // Create and send request
            UniversalMatrixRequest request = new UniversalMatrixRequest(matrixA, matrixB, operation, clientName);
            
            if (!request.isValid()) {
                System.err.println("Invalid request: " + request.getValidationError());
                return;
            }
            
            System.out.println("Sending matrix operation request...");
            out.writeObject(request);
            out.flush();
            
        } catch (Exception e) {
            System.err.println("Error in matrix operation: " + e.getMessage());
        }
    }
    
    private static void handleQuickMatrixOperation(ObjectOutputStream out, Scanner scanner, String clientName) 
            throws IOException {
        try {
            System.out.println("\n=== Quick Matrix Operation ===");
            
            System.out.println("Input Matrix A:");
            NDMatrix matrixA = MatrixInputHelper.inputQuickMatrix(scanner);
            
            System.out.println("Input Matrix B (must have same dimensions as Matrix A):");
            System.out.println("Required dimensions: " + Arrays.toString(matrixA.getDimensions()));
            NDMatrix matrixB = MatrixInputHelper.inputNDMatrix("Matrix B", matrixA.getDimensions(), scanner);
            
            Operation operation = chooseOperation(scanner);
            
            UniversalMatrixRequest request = new UniversalMatrixRequest(matrixA, matrixB, operation, clientName);
            
            if (!request.isValid()) {
                System.err.println("Invalid request: " + request.getValidationError());
                return;
            }
            
            System.out.println("Sending matrix operation request...");
            out.writeObject(request);
            out.flush();
            
        } catch (Exception e) {
            System.err.println("Error in quick matrix operation: " + e.getMessage());
        }
    }
    
    private static Operation chooseOperation(Scanner scanner) {
        System.out.println("\nChoose operation:");
        System.out.println("1. ADD");
        System.out.println("2. SUBTRACT");
        System.out.println("3. MULTIPLY (element-wise)");
        System.out.println("4. DIVIDE (element-wise)");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        return switch (choice) {
            case 1 -> Operation.ADD;
            case 2 -> Operation.SUBTRACT;
            case 3 -> Operation.MULTIPLY;
            case 4 -> Operation.DIVIDE;
            default -> throw new IllegalArgumentException("Invalid operation choice");
        };
    }
    
    private static void showMatrixExamples() {
        System.out.println("\n=== Matrix Examples ===");
        System.out.println("1D Vector [3]: [a, b, c]");
        System.out.println("2D Matrix [2,3]: [[a,b,c], [d,e,f]]");
        System.out.println("3D Matrix [2,2,2]: [[[a,b],[c,d]], [[e,f],[g,h]]]");
        System.out.println("4D Matrix [2,2,2,2]: Four-dimensional tensor");
        System.out.println("\nAll operations are element-wise:");
        System.out.println("- ADD: A[i] + B[i]");
        System.out.println("- SUBTRACT: A[i] - B[i]");
        System.out.println("- MULTIPLY: A[i] * B[i]");
        System.out.println("- DIVIDE: A[i] / B[i]");
    }
    
    private static int calculateTotalElements(int[] dimensions) {
        int total = 1;
        for (int dim : dimensions) {
            total *= dim;
        }
        return total;
    }
    
    private static void closeResources(Scanner scanner, Socket socket) {
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
