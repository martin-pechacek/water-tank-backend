package watertank.enums;

public enum Distance {
    ULTRASONIC_SENSOR(252),
    SPILLWAY(241);

    private int distance;

    Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
