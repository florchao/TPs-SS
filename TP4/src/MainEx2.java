package src;

import java.io.IOException;
import java.util.List;

public class MainEx2 {
    private double L = 135;
    private double r = 2.25;
    private double k = 2500;

    private double getForce( Particle particle){
        return (particle.getU() - particle.getVx());
    }

    //fuerza que le ejerce p1 sobre p2
    private double collisionForce(Particle p1, Particle p2){
        return k*(Math.abs(p1.getX() - p2.getX()) - 2*r)*Math.signum(p1.getX() - p2.getX());
    }

    //a = movementEquation(p1, list)
    private double movementEquation(Particle p1, List<Particle> particleList){
        double sumForces = 0;
        for (Particle p2 : particleList) {
            if (p2 != p1) {
                sumForces += collisionForce(p1, p2);
            }
        }
        return (getForce(p1) + sumForces)/p1.getM();
    }

    public static void main(String[] args) throws IOException {
        int n = Integer.parseInt(args[0]);
        double mass = 25;
        double particleR = 2.25;
        double circleR =21.49;

        FileGenerator writeFiles = new FileGenerator();
        writeFiles.generateStaticFile("./input/inputFile.txt", particleR, n, mass, 0.01, circleR);

        List<Particle> particles = ParticleGenerator.generateParticles("./input/inputFile.txt");

    }

}
