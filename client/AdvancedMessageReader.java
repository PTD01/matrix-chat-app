package client;

import shared.*;
import java.io.ObjectInputStream;

/**
 * Advanced message reader that can handle N-dimensional matrix responses
 */
public class AdvancedMessageReader extends Thread {
    private final ObjectInputStream in;
    
    public AdvancedMessageReader(ObjectInputStream in) {
        this.in = in;
        setDaemon(true); // Dies when main thread dies
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Object response = in.readObject();
                handleResponse(response);
            }
        } catch (Exception e) {
            System.out.println("\n[Connection] Lost connection to server.");
        }
    }
    
    private void handleResponse(Object response) {
        if (response instanceof String) {
            handleStringMessage((String) response);
        } else if (response instanceof UniversalMatrixResponse) {
            handleUniversalMatrixResponse((UniversalMatrixResponse) response);
        } else if (response instanceof MatrixResponse) {
            handleLegacyMatrixResponse((MatrixResponse) response);
        } else {
            System.out.println("\n[Server] Unknown response type: " + response.getClass().getSimpleName());
        }
    }
    
    private void handleStringMessage(String message) {
        if (message.startsWith("CHAT:")) {
            System.out.println("\n[Chat] " + message.substring(5));
        } else {
            System.out.println("\n[Server] " + message);
        }
        System.out.print("Choose option: "); // Re-prompt user
    }
    
    private void handleUniversalMatrixResponse(UniversalMatrixResponse response) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MATRIX OPERATION RESULT");
        System.out.println("=".repeat(50));
        
        if (response.hasError()) {
            System.out.println("❌ Error: " + response.getErrorMessage());
        } else {
            NDMatrix result = response.getResult();
            
            System.out.println("✅ Operation: " + response.getOperation());
            if (response.getClientId() != null) {
                System.out.println("📤 Client: " + response.getClientId());
            }
            if (response.getProcessingTimeMs() > 0) {
                System.out.println("⏱️  Processing time: " + response.getProcessingTimeMs() + "ms");
            }
            
            System.out.println("📊 Result dimensions: " + java.util.Arrays.toString(result.getDimensions()));
            System.out.println("📈 Total elements: " + result.getTotalElements());
            
            System.out.println("\n📋 Result:");
            System.out.println(result.toFormattedString());
        }
        
        System.out.println("=".repeat(50));
        System.out.print("Choose option: "); // Re-prompt user
    }
    
    private void handleLegacyMatrixResponse(MatrixResponse response) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("LEGACY MATRIX RESULT");
        System.out.println("=".repeat(40));
        
        if (response.hasError()) {
            System.out.println("❌ Error: " + response.getErrorMessage());
        } else {
            double[][] result = response.getResult();
            System.out.println("✅ 2D Matrix Result:");
            
            for (double[] row : result) {
                System.out.print("[");
                for (int j = 0; j < row.length; j++) {
                    if (j > 0) System.out.print(", ");
                    System.out.printf("%8.2f", row[j]);
                }
                System.out.println("]");
            }
        }
        
        System.out.println("=".repeat(40));
        System.out.print("Choose option: "); // Re-prompt user
    }
}
