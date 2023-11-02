package src;

import java.util.Objects;

public class Coordinates {

    public static final Coordinates ZERO = new Coordinates(0.0, 0.0);
    private Double x;
    private Double y;

    public Coordinates(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates Coordinates = (Coordinates) o;
        return Objects.equals(x, Coordinates.x) && Objects.equals(y, Coordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double dotProduct(Coordinates other) {
        return this.x * other.getX() + this.y * other.getY();
    }

    public Coordinates scale(double scalar) {
        return new Coordinates(this.x * scalar, this.y * scalar);
    }

    public Coordinates subtract(Coordinates other) {
        return new Coordinates(this.x - other.x, this.y - other.y);
    }

    public Coordinates add(Coordinates other) {
        return new Coordinates(this.x + other.x, this.y + other.y);
    }

    public double distance(Coordinates other) {
        double deltaX = this.x - other.x;
        double deltaY = this.y - other.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public Coordinates normalize() {
        double length = Math.sqrt(x * x + y * y);
        if (length != 0) {
            return new Coordinates(x / length, y / length);
        } else {
            return new Coordinates(0.0, 0.0); // Returning a zero vector if the input vector is already a zero vector
        }
    }
}
