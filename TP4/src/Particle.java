package src;

import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private int id;
    private double x;
    private double x2;
    private double x3;
    private double x4;
    private double x5;
    private double realX;
    private double r;
    private double m;
    private double fX;
    private double vx;
    private double u;

    public Particle(int id, double x, double vx, double u, double r, double m, double fX, double realX) {
        this.id = id;
        this.x = x;
        this.realX = realX;
        this.r = r;
        this.m = m;
        this.vx = vx;
        this.u = u;
        this.fX = fX;
        this.x2 = 0;
        this.x3 = 0;
        this.x4 = 0;
        this.x5 = 0;
    }

    public Particle(int id, double x, double vx, double u, double r, double m, double fX, double x2, double x3, double x4, double x5, double realX) {
        this.id = id;
        this.x = x;
        this.r = r;
        this.m = m;
        this.vx = vx;
        this.u = u;
        this.fX = fX;
        this.realX = realX;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.x5 = x5;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        double L = 135;
        double aux = x % L;
        if (aux < 0){
            aux +=L;
        }
        this.x = aux;
    }
    public double getR() {
        return r;
    }

    public double getM() {
        return m;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getU() {
        return u;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getX3() {
        return x3;
    }

    public void setX3(double x3) {
        this.x3 = x3;
    }

    public double getX4() {
        return x4;
    }

    public void setX4(double x4) {
        this.x4 = x4;
    }

    public double getX5() {
        return x5;
    }

    public void setX5(double x5) {
        this.x5 = x5;
    }

    public double getFx() {
        return fX;
    }

    public int getId() {
        return id;
    }

    public double getRealX() {
        return realX;
    }

    public void setRealX(double realX) {
        this.realX = realX;
    }

    public boolean collides(Particle p, Double dt) {
        double dr = this.getX() - p.getX();
        double dv = this.getVx() - p.getVx();

        double sigma = this.r + p.getR();

        double dvdr = (dr * dv);
        if (dvdr >= 0) {
            return false;
        }

        double dv2 = (dv * dv);
        double dr2 = (dr * dr);
        double d = Math.pow(dvdr, 2) - dv2 * (dr2 - Math.pow(sigma, 2));
        if (d < 0) {
            return false;
        }

        return (-(dvdr + Math.sqrt(d)) / dv2 ) < dt;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", x=" + x +
                '}';
    }

    @Override
    public int compareTo(Particle p2) {
        return Double.compare(this.x, p2.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Particle other = (Particle) o;
        return id == other.id;
    }
}