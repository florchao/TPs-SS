public class Limit {
    private static int counter = 1000;
    private final int id;
    private final double weight = 0;

    private final double radius = 0.0001;
    private final double vx = 0;
    private  final double vy = 0;

    private double rx;
    private double ry;

    public Limit( double x, double y) {
        this.id = counter;
        this.rx = x;
        this.ry = y;
        counter++;
    }

    public static int getCounter() {
        return counter;
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        return weight;
    }

    public double getRadius() {
        return radius;
    }


    public double getXpos() {
        return rx;
    }

    public double getYpos() {
        return ry;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }
}