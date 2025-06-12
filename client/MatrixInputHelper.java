package client;

import shared.NDMatrix;
import java.util.Scanner;
import java.util.Arrays;

public class MatrixInputHelper {

    public static double[][] input2DMatrix(int rows, int cols, Scanner scanner) {
        double[][] matrix = new double[rows][cols];
        System.out.println("Enter values for 2D matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("[" + i + "][" + j + "]: ");
                matrix[i][j] = scanner.nextDouble();
            }
        }
        return matrix;
    }

    public static double[][][] input3DMatrix(int depth, int rows, int cols, Scanner scanner) {
        double[][][] matrix = new double[depth][rows][cols];
        System.out.println("Enter values for 3D matrix:");
        for (int d = 0; d < depth; d++) {
            System.out.println("Enter values for depth layer " + d + ":");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.print("[" + d + "][" + i + "][" + j + "]: ");
                    matrix[d][i][j] = scanner.nextDouble();
                }
            }
        }
        return matrix;
    }

    /**
     * Input N-dimensional matrix with specified dimensions
     */
    public static NDMatrix inputNDMatrix(String matrixName, int[] dimensions, Scanner scanner) {
        NDMatrix matrix = new NDMatrix(dimensions);
        System.out.println("Enter values for " + matrixName + " " + Arrays.toString(dimensions) + ":");

        inputNDMatrixRecursive(matrix, new int[dimensions.length], 0, dimensions, scanner);

        return matrix;
    }

    /**
     * Recursive helper method to input N-dimensional matrix values
     */
    private static void inputNDMatrixRecursive(NDMatrix matrix, int[] currentIndices, int dimension,
                                             int[] dimensions, Scanner scanner) {
        if (dimension == dimensions.length) {
            // Base case: we've reached the deepest level, input the value
            System.out.print("Enter value at " + Arrays.toString(currentIndices) + ": ");
            double value = scanner.nextDouble();
            matrix.set(value, currentIndices);
        } else {
            // Recursive case: iterate through current dimension
            for (int i = 0; i < dimensions[dimension]; i++) {
                currentIndices[dimension] = i;
                inputNDMatrixRecursive(matrix, currentIndices, dimension + 1, dimensions, scanner);
            }
        }
    }

    /**
     * Get dimensions from user input
     */
    public static int[] inputDimensions(Scanner scanner) {
        System.out.print("Enter number of dimensions: ");
        int numDimensions = scanner.nextInt();

        if (numDimensions <= 0) {
            throw new IllegalArgumentException("Number of dimensions must be positive");
        }

        int[] dimensions = new int[numDimensions];
        System.out.println("Enter size for each dimension:");

        for (int i = 0; i < numDimensions; i++) {
            System.out.print("Dimension " + (i + 1) + " size: ");
            dimensions[i] = scanner.nextInt();

            if (dimensions[i] <= 0) {
                throw new IllegalArgumentException("All dimension sizes must be positive");
            }
        }

        return dimensions;
    }

    /**
     * Quick input for common matrix types
     */
    public static NDMatrix inputQuickMatrix(Scanner scanner) {
        System.out.println("Quick matrix input options:");
        System.out.println("1. 1D Vector");
        System.out.println("2. 2D Matrix");
        System.out.println("3. 3D Matrix");
        System.out.println("4. Custom N-D Matrix");
        System.out.print("Choose option: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter vector length: ");
                int length = scanner.nextInt();
                return inputNDMatrix("vector", new int[]{length}, scanner);

            case 2:
                System.out.print("Enter rows: ");
                int rows = scanner.nextInt();
                System.out.print("Enter columns: ");
                int cols = scanner.nextInt();
                return inputNDMatrix("matrix", new int[]{rows, cols}, scanner);

            case 3:
                System.out.print("Enter depth: ");
                int depth = scanner.nextInt();
                System.out.print("Enter rows: ");
                int rows3d = scanner.nextInt();
                System.out.print("Enter columns: ");
                int cols3d = scanner.nextInt();
                return inputNDMatrix("3D matrix", new int[]{depth, rows3d, cols3d}, scanner);

            case 4:
                int[] dimensions = inputDimensions(scanner);
                return inputNDMatrix("N-D matrix", dimensions, scanner);

            default:
                throw new IllegalArgumentException("Invalid choice");
        }
    }
}
