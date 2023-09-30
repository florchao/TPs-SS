package src;

public class Particle {

    private int id;
    private double x;
    private double y;
    private double m;
    private double vx;
    private double vy;
    private double radius;
    private double u;
    private double ax;
    private double ay;
    //w = v/r

    public Particle(double x, double y, double vx, double vy, double m) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.m = m;
    }

    public Particle(double x, double y, double vx, double vy, double radius, double m) {
        this.x = x;
        this.y = y;
        this.m = m;
        this.radius = radius;
        this.vx = vx;
        this.vy = vy;
    }
    public Particle(int id, double x, double y, double vx, double vy, double u, double radius, double m) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.m = m;
        this.vx = vx;
        this.vy = vy;
        this.u = u;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        double l = 135;
        double aux = x % l;
        if (aux < 0){
            aux +=l;
        }
        this.x = aux;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getM() {
        return m;
    }
    public void setM(double m) {
        this.m = m;
    }
    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }
    public double getU() {
        return u;
    }
    public void setU(double u) {
        this.u = u;
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                ", m=" + m +
                ", vx=" + vx +
                ", vy=" + vy +
                '}';
    }
}
