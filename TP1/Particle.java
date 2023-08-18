import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private final double radius;
    private final double property;
    private double x;
    private double y;
    private List<Particle> neighbours;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Particle(int id, double radius, double property, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.property = property;
        this.neighbours = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public double getProperty() {
        return property;
    }

    public double getRadius() {
        return radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<Particle> getNeighbours() {
        return neighbours;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setNeighbours(List<Particle> neighbours) {
        this.neighbours = neighbours;
    }
}
