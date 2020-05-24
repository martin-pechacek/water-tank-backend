package watertank.enums;

public enum Distance {
    ULTRASONIC_SENSOR(252),
    SPILLWAY(228);

    private int distance;

    Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public static int maxWaterLevel(){
        return ULTRASONIC_SENSOR.getDistance() - SPILLWAY.getDistance();
    }
}
