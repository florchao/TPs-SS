import java.util.Objects;

public class Particle implements Comparable<Particle>{
    private static int counter = 1;
    private final int id;
    private final double weight;
    private final double radius;
    private double vx;
    private double vy;
    private double rx;
    private double ry;

    public Particle(double radius, double vx, double vy, double rx, double ry, double weight) {
        this.radius = radius;
        this.id = counter;
        this.vx = vx;
        this.vy= vy;
        this.rx = rx;
        this.ry = ry;
        this.weight = weight;
        counter++;
    }

    public double timeToCollision(Particle p2) {
        double deltaY = p2.getYpos() - getYpos();
        double deltaX = p2.getXpos() - getXpos();

        double deltaVy = p2.getVy() - getVy();
        double deltaVx = p2.getVx() - getVx();

        double deltaVR = deltaX*deltaVx + deltaY*deltaVy;
        double deltaV2 = deltaVx*deltaVx + deltaVy*deltaVy;
        double deltaR2 = deltaX*deltaX + deltaY*deltaY;

        double sigma = getRadius() + p2.getRadius();

        double d = (deltaVR * deltaVR) - deltaV2 * (deltaR2 - sigma * sigma);

        if (deltaVR >= 0 || d < 0)
            return -1;

        double tolerance = 1E-9;

        if (deltaR2 + tolerance < sigma * sigma) {
            throw new RuntimeException("overlapping particles: " + this.getId() + " and " + p2.getId());
        }

        return -(deltaVR + Math.sqrt(d)) / deltaV2;
    }

    public double getRadius() {
        return radius;
    }

    public double getVx(){
        return vx;
    }

    public double getVy(){
        return vy;
    }

    public void setVelocity(double vx, double vy) {
        setVx(vx);
        setVy(vy);
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }


    public double getXpos() {
        return rx;
    }


    public double getYpos() {
        return ry;
    }


    public void updatePosition(double time) {
        this.rx = this.rx + getVx()*time;
        this.ry = this.ry + getVy()*time;
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Particle p2) {
        return this.id - p2.id;
    }

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

    @Override
    public String toString(){
        return "Particle " + this.id;
    }

    public static void resetId() {
        counter = 1;
    }
}