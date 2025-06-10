
import java.io.Serializable;

public class MatrixRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private double[][] matrixA;
    private double[][] matrixB;
    private String operation; // "add", "subtract", "multiply", "divide"

    public MatrixRequest(double[][] matrixA, double[][] matrixB, String operation) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.operation = operation;
    }

    public double[][] getMatrixA() {
        return matrixA;
    }

    public double[][] getMatrixB() {
        return matrixB;
    }

    public String getOperation() {
        return operation;
    }
}
