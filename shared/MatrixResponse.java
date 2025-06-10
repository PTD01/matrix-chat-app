
import java.io.Serializable;

public class MatrixResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private double[][] result;
    private String message;

    public MatrixResponse(double[][] result, String message) {
        this.result = result;
        this.message = message;
    }

    public double[][] getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
