package watertank.exceptions;

public class MeasurementException extends RuntimeException {

    public static final MeasurementException NOT_FOUND = new MeasurementException("Measurement not found");

    public MeasurementException() {
        super();
    }

    public MeasurementException(String message) {
        super(message);
    }

    public MeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}
