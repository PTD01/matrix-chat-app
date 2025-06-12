package client;

import java.util.Scanner;

/**
 * Simple launcher to choose which client to run
 */
public class ClientLauncher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Chat App Client Launcher ===");
        System.out.println("Choose which client to run:");
        System.out.println("1. AdvancedClient (N-Dimensional Matrix operations)");
        System.out.println("2. ClientA (Legacy - 2D/3D Matrix operations)");
        System.out.println("3. ClientB (Simple - 2D Matrix operations)");
        System.out.println("4. ClientC (Simple - 2D Matrix operations)");
        System.out.print("Enter your choice (1-4): ");
        
        try {
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.println("Starting AdvancedClient...");
                    AdvancedClient.main(args);
                    break;
                case 2:
                    System.out.println("Starting ClientA...");
                    ClientA.main(args);
                    break;
                case 3:
                    System.out.println("Starting ClientB...");
                    ClientB.main(args);
                    break;
                case 4:
                    System.out.println("Starting ClientC...");
                    ClientC.main(args);
                    break;
                default:
                    System.out.println("Invalid choice. Please run again and choose 1, 2, 3, or 4.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println("Please run again and enter a valid number.");
        } finally {
            scanner.close();
        }
    }
}
