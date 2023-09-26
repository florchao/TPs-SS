package src;

public class Particle {

    private double x;
    private double y;
    private double m;
    private double vx;
    private double vy;
    private double u;

    public Particle(int x,double y, double vx, double vy, double m) {
        this.x = x;
        this.y = y;
        this.m = m;
        this.vx = vx;
        this.vy = vy;
    }

    public Particle(double x, double y, double m, double vx, double vy, double u) {
        this.x = x;
        this.y = y;
        this.m = m;
        this.vx = vx;
        this.vy = vy;
        this.u = u;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
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
