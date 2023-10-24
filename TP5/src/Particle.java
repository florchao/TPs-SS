package src;

import java.awt.*;
import java.util.Objects;

import static src.Utils.GRAVITY;

public class Particle {
    private static final double ZERO = 0.0;
    private final static double B = (2.0 / 3.0);
    private final static double C = -(1.0 / 6.0);

    private Double forceX;
    private Double forceY;

    private final Double radius;
    private final Double mass;
    private final int id;
    private boolean reInjected = false;
    private Color color;

    private final Double dt;
    private final Double sqrDt;
    private Pair position;
    private Pair velocity;
    private Pair prevAcceleration;
    private Pair actualAcceleration;
    private Pair actualVelocity;
    private boolean gone = false;

    public void resetForce() {
        forceX = ZERO;
        forceY = ZERO;
    }

    public double getEnergy(){
        return Math.pow(this.velocity.module(Pair.ZERO), 2) * mass / 2.0;
    }

    public void addToForce(double x, double y) {
        forceX = forceX + x;
        forceY = forceY + y;
    }

    public Particle(int id, Pair position, Double radius, Double mass, Double dt, Color color) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.forceX = ZERO;
        this.forceY = ZERO;
        this.velocity = new Pair(ZERO, ZERO);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);
        actualAcceleration = new Pair(ZERO, ZERO);
        prevAcceleration = new Pair(ZERO, GRAVITY);
        this.color = color;
    }

    public Particle copy() {
        return new Particle(id, position, radius, mass, dt, color);
    }

    public void addToForce(Pair pair) {
        forceX = forceX + pair.getX();
        forceY = forceY + pair.getY();
    }

    public Pair getAcceleration() {
        Pair aux = new Pair(forceX, forceY);
        return aux.scale(1.0 / mass);
    }

    public void reInject() {
        reInjected = true;
        setColor(Color.RED);
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public Pair getPosition() {
        return position;
    }

    public Pair getVelocity() {
        return velocity;
    }

    public void prediction() {
        actualAcceleration = this.getAcceleration();
        this.position = position.sum(
                velocity.scale(dt).sum(
                        actualAcceleration.scale(B).sum(
                                prevAcceleration.scale(C)
                        ).scale(sqrDt)
                )
        );

        this.actualVelocity = velocity;

        this.velocity = this.actualVelocity.sum(
                this.actualAcceleration.scale(1.5 * dt).sum(
                        prevAcceleration.scale(-0.5 * dt)
                )
        );

    }

    public void correction(){
        if (reInjected){
            this.velocity = new Pair(ZERO, ZERO);
            reInjected = false;
            prevAcceleration = new Pair(ZERO, GRAVITY);
        }else {
            this.velocity = actualVelocity.sum(
                    this.getAcceleration().scale((1.0 / 3.0) * dt).sum(
                            actualAcceleration.scale((5.0 / 6.0) * dt).sum(
                                    prevAcceleration.scale(-(1.0 / 6.0) * dt)
                            )
                    )
            );
            prevAcceleration = actualAcceleration;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isGone() {
        return gone;
    }

    public void setGone(boolean gone) {
        this.gone = gone;
    }
}
