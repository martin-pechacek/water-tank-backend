package watertank.exceptions;

public class MeasurementException extends RuntimeException {

    public static final MeasurementException NOT_FOUND = new MeasurementException("Measurement not found");
    public static final MeasurementException MISSING_DEVICE_ID = new MeasurementException("Missing device ID");

    public MeasurementException(String message) {
        super(message);
    }
}
