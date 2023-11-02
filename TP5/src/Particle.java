package src;

import java.awt.*;
import java.util.*;

public class Particle {

    private static final double ZERO = 0.0;
    private static final double B = 2.0 / 3.0;
    private static final double C = -1.0 / 6.0;

    private final int id;
    private final Double radius;
    private final Double mass;
    private final Double dt;
    private final Double sqrDt;
    private Coordinates position;
    private Coordinates velocity;
    private Coordinates prevAcceleration;
    private Coordinates actualAcceleration;
    private Coordinates actualVelocity;
    private boolean gone = false;
    private boolean reInjected = false;
    private Color color;

    private double forceX;
    private double forceY;
    private Coordinates floorRelativeVelocity = Coordinates.ZERO;
    private Coordinates rightRelativeVelocity = Coordinates.ZERO;
    private Coordinates leftRelativeVelocity = Coordinates.ZERO;

    public Particle(int id, Coordinates position, Double radius, Double mass, Double dt, Color color) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.velocity = new Coordinates(ZERO, ZERO);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);
        this.actualAcceleration = new Coordinates(ZERO, ZERO);
        this.prevAcceleration = new Coordinates(ZERO, Utils.GRAVITY);
        this.color = color;
    }

    public Particle copy() {
        return new Particle(id, position, radius, mass, dt, color);
    }

    public void resetForce() {
        this.forceX = ZERO;
        this.forceY = ZERO;
    }

    public void addToForce(double x, double y) {
        this.forceX += x;
        this.forceY += y;
    }

    public void addToForce(Coordinates coordinates) {
        this.forceX += coordinates.getX();
        this.forceY += coordinates.getY();
    }

    public Coordinates getAcceleration() {
        return new Coordinates(this.forceX, this.forceY).scale(1.0 / mass);
    }

    public void reInject() {
        this.reInjected = true;
        this.setColor(Color.RED);
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + color.getRed() + " " + color.getGreen() + " " + color.getBlue();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Coordinates getPosition() {
        return position;
    }

    public Coordinates getVelocity() {
        return velocity;
    }

    public void prediction() {
        this.actualAcceleration = this.getAcceleration();
        this.position = this.position.add(this.velocity.scale(dt).add(
                this.actualAcceleration.scale(B).add(
                        this.prevAcceleration.scale(C)
                ).scale(sqrDt)
        ));

        this.actualVelocity = this.velocity;

        this.velocity = this.actualVelocity.add(
                this.actualAcceleration.scale(1.5 * dt).add(
                        this.prevAcceleration.scale(-0.5 * dt)
                )
        );
    }

    public void correction() {
        if (reInjected) {
            this.velocity = Coordinates.ZERO;
            reInjected = false;
            prevAcceleration = new Coordinates(ZERO, Utils.GRAVITY);
        } else {
            this.velocity = this.actualVelocity.add(
                    this.getAcceleration().scale(1.0 / 3.0 * dt).add(
                            this.actualAcceleration.scale(5.0 / 6.0 * dt).add(
                                    this.prevAcceleration.scale(-(1.0 / 6.0) * dt)
                            )
                    )
            );
            prevAcceleration = actualAcceleration;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle particle)) return false;
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

    public void setFloorRelativeVelocity(Coordinates floorRelativeVelocity) {
        this.floorRelativeVelocity = floorRelativeVelocity;
    }

    public void setRightRelativeVelocity(Coordinates rightRelativeVelocity) {
        this.rightRelativeVelocity = rightRelativeVelocity;
    }

    public void setLeftRelativeVelocity(Coordinates leftRelativeVelocity) {
        this.leftRelativeVelocity = leftRelativeVelocity;
    }

    public Coordinates getFloorRelativeVelocity() {
        return floorRelativeVelocity;
    }

    public Coordinates getRightRelativeVelocity() {
        return rightRelativeVelocity;
    }

    public Coordinates getLeftRelativeVelocity() {
        return leftRelativeVelocity;
    }
}
