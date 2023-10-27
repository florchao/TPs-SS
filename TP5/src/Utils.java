package src;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final double MAX_RADIUS = 0.0115; // TODO: esto en que unidad esta? (en la consigna dice A=0.15cm)
    private static final double MIN_RADIUS = 0.0085;

    public static List<Particle> generateParticles(Double W, Double L, int N, Double mass, Double dt) {
        List<Particle> particles = new ArrayList<>();
        double x, y, radius;
        boolean overlap;

        for (int i = 1; i <= N; i++) {
            // Generate initial particle
            radius = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);
            x = radius + Math.random() * (W - 2 * radius);
            y = radius + L / 10 + Math.random() * (L - 2 * radius);
            Particle newParticle = new Particle(i, new Pair(x, y), radius, mass, dt, Color.RED);

            overlap = false;

            // Check for overlap with existing particles
            for (Particle existingParticle : particles) {
                double distance = calculateDistance(newParticle, existingParticle);
                if (distance < newParticle.getRadius() + existingParticle.getRadius()) {
                    overlap = true;
                    break;
                }
            }

            // If overlap detected, generate a new particle
            if (overlap) {
                System.out.println("OVERLAP");
                i--;
            } else {
                particles.add(newParticle);
            }
        }

        return particles;
    }

    private static double calculateDistance(Particle p1, Particle p2) {
        double x1 = p1.getPosition().getX();
        double y1 = p1.getPosition().getY();
        double x2 = p2.getPosition().getX();
        double y2 = p2.getPosition().getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static boolean overlap(Particle p1, Particle p2) {
        if (!p1.equals(p2)) {
            return calculateDistance(p1, p2) < p1.getRadius() + p2.getRadius();
        } else
            return false;
    }

    public static final double K_NORMAL = 0.25;
    public static final double GRAVITY = -0.05;

    public static final double K_TAN = 2 * K_NORMAL;

    public static double getNormalForce(double superposition) {
        return -K_NORMAL * (superposition); // TODO: hay que cambiar esta ecuacion
    }

    // TODO: esto tambien es lo otro que hay que cambiar
    public static Pair getNormalForce(double superposition, Pair versor) {

        double force = getNormalForce(superposition);

        return versor.scale(force);
    }

    public static double getTangencialForce(double superposition, double relativeTangencialVelocity) {
        return -K_TAN * (superposition) * (relativeTangencialVelocity);
    }

    public static Pair getTangencialForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double force = getTangencialForce(superposition, relativeTangencialVelocity.dot(tan));

        return tan.scale(force);
    }

    public static Pair getWallForce(double superposition, Pair relativeTangencialVelocity, Pair normalVersor) {

        Pair tan = new Pair(-normalVersor.getY(), normalVersor.getX());

        double forceT = getTangencialForce(superposition, relativeTangencialVelocity.dot(tan));
        double forceN = getNormalForce(superposition);
        return normalVersor.scale(forceN).sum(tan.scale(forceT));
    }

}
