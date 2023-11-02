package src;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final double MAX_RADIUS = 1.15;
    private static final double MIN_RADIUS = 0.85;
    private static final String RESOURCES_PATH = "./output/";

    public static List<Particle> generateParticles(double W, double L, int N, double mass, double dt) {
        List<Particle> particles = new ArrayList<>();

        for (int i = 1; i <= N; i++) {
            Particle newParticle = createRandomParticle(i, W, L, mass, dt);
            while (checkOverlap(newParticle, particles)) {
                newParticle = createRandomParticle(i, W, L, mass, dt);
            }
            particles.add(newParticle);
        }

        return particles;
    }

    private static Particle createRandomParticle(int id, double W, double L, double mass, double dt) {
        double radius = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);
        double x = radius + Math.random() * (W - 2 * radius);
        double y = radius + L / 10 + Math.random() * (L - 2 * radius);
        return new Particle(id, new Coordinates(x, y), radius, mass, dt, Color.RED);
    }

    public static boolean checkOverlap(Particle newParticle, List<Particle> particles) {
        return particles.stream()
                .anyMatch(existingParticle ->
                        calculateDistance(newParticle.getPosition(), existingParticle.getPosition()) < newParticle.getRadius() + existingParticle.getRadius());
    }

    public static boolean checkOverlap(Particle p1, Particle p2) {
        return calculateDistance(p1.getPosition(), p2.getPosition()) < p1.getRadius() + p2.getRadius();
    }

    private static double calculateDistance(Coordinates p1, Coordinates p2) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static final double K_NORMAL = 250;
    public static final double GAMMA = 2.5;
    public static final double U = 0.1;
    public static final double GRAVITY = -5;
    public static final double K_TAN = 2 * K_NORMAL;

    public static double getNormalForce(double superpositionA, double superpositionB) {
        return -K_NORMAL * (superpositionA) - GAMMA * (superpositionB); // (N.1)
    }

    public static Coordinates getNormalForce(double superpositionA, Coordinates versor, double superpositionB) {
        double force = getNormalForce(superpositionA, superpositionB);
        return versor.scale(force);
    }

    //  (T.1)
    public static double getTangentialForce(double superpositionA, double relativeTangentialVelocity, double superpositionB, double dt) {
        double res1 = - U * Math.abs(getNormalForce(superpositionA, superpositionB)) * Math.signum(relativeTangentialVelocity);
        double res2 = -K_TAN  * (relativeTangentialVelocity * dt);
        return Math.min(res1, res2);
    }

    public static Coordinates getTangentialForce(double superpositionA, Coordinates relativeTangentialVelocity, Coordinates normalVersor, double superpositionB, double dt) {
        Coordinates tan = new Coordinates(-normalVersor.getY(), normalVersor.getX());
        double force = getTangentialForce(superpositionA, relativeTangentialVelocity.dotProduct(tan), superpositionB, dt);
        return tan.scale(force);
    }

    public static Coordinates getWallForce(double superpositionA, Coordinates relativeTangentialVelocity, Coordinates normalVersor, double superpositionB, double dt) {
        Coordinates tan = new Coordinates(-normalVersor.getY(), normalVersor.getX());
        double forceT = getTangentialForce(superpositionA, relativeTangentialVelocity.dotProduct(tan), superpositionB, dt);
        double forceN = getNormalForce(superpositionA, superpositionB);
        return normalVersor.scale(forceN).add(tan.scale(forceT));
    }

    public static String createFile(String name, String extension) {
        int count = 1;
        while (true) {
            String filePath = RESOURCES_PATH + name + count + "." + extension;
            File file = new File(filePath);
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + filePath);
                    return filePath;
                }
            } catch (IOException e) {
                throw new RuntimeException("Error writing random particles to file (" + name + ") in ParticleUtils.createFile.", e);
            }
            count++;
        }
    }

    public static void writeParticlesToFileXyz(String filePath, List<Particle> particles, List<Double> fixedX, List<Double> fixedY, String comment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write((fixedX.size() + particles.size()) + "\n" + comment + "\n");

            for (int i = 0; i < fixedX.size(); i++) {
                writer.write(String.format("%.2f %.2f 0.0 0.0 0.0001 0 0 0\n", fixedX.get(i), fixedY.get(i)));
            }

            for (Particle particle : particles) {
                writer.write(particle.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing particles to file (" + filePath + ") in ParticlesUtils.writeParticlesToFile.", e);
        }
    }

    public static void writeParticlesToFileXyz(String filePath, List<Particle> particles, List<Double> fixedX, List<Double> fixedY) {
        writeParticlesToFileXyz(filePath, particles, fixedX, fixedY, "");
    }

    public static <T> void writeListToFile(List<T> list, String filePath, boolean appendNewLine) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i).toString());
                if (appendNewLine && i < list.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing list to file (" + filePath + ").", e);
        }
    }
}
