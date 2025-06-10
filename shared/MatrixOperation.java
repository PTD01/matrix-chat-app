
public class MatrixOperation {

    public static synchronized double[][] add(double[][] A, double[][] B) {
        validateDimensions(A, B);
        double[][] result = new double[A.length][A[0].length];

        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                result[i][j] = A[i][j] + B[i][j];

        return result;
    }

    public static synchronized double[][] subtract(double[][] A, double[][] B) {
        validateDimensions(A, B);
        double[][] result = new double[A.length][A[0].length];

        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++)
                result[i][j] = A[i][j] - B[i][j];

        return result;
    }

    public static synchronized double[][] multiply(double[][] A, double[][] B) {
        if (A[0].length != B.length)
            throw new IllegalArgumentException("Matrix dimensions incompatible for multiplication.");

        double[][] result = new double[A.length][B[0].length];

        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < B[0].length; j++)
                for (int k = 0; k < B.length; k++)
                    result[i][j] += A[i][k] * B[k][j];

        return result;
    }

    public static synchronized double[][] divide(double[][] A, double[][] B) {
        validateDimensions(A, B);
        double[][] result = new double[A.length][A[0].length];

        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A[0].length; j++) {
                if (B[i][j] == 0)
                    throw new ArithmeticException("Divide by zero at position (" + i + "," + j + ")");
                result[i][j] = A[i][j] / B[i][j];
            }

        return result;
    }

    private static void validateDimensions(double[][] A, double[][] B) {
        if (A.length != B.length || A[0].length != B[0].length)
            throw new IllegalArgumentException("Matrix dimensions must match.");
    }
}

