package model;

public class CoordinateModel {
    private double x;
    private double y;
    private String message;

    public CoordinateModel(double x, double y, String message) {
        this.x = x;
        this.y = y;
        this.message = message;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getMessage() {
        return message;
    }
}
