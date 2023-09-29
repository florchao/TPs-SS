package src;

import java.io.IOException;
import java.util.List;

public class MainEx2 {
    private static double r = 2.25;
    private double k = 2500;
    static double circleR =21.49;
    static double mass = 0.25;

    //w = v/r
    private double getForce( Particle particle){
        double v = Math.sqrt((Math.pow(particle.getVx(), 2)) + Math.pow(particle.getVy(), 2));
        return (particle.getU()/circleR - (v/ particle.getRadius()));
    }

    //fuerza que le ejerce p1 sobre p2
    private double collisionForce(Particle p1, Particle p2){
        return k*(Math.abs(p1.getAngle() - p2.getAngle()) - 2*r/circleR)*Math.signum(p1.getAngle() - p2.getAngle());
    }

    //w = movementEquation(p1, list)
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
        FileGenerator writeFiles = new FileGenerator();
        writeFiles.generateStaticFile("./input/inputFile.txt", r, n, mass, 0.01, circleR);

        List<Particle> particles = ParticleGenerator.generateParticles("./input/inputFile.txt");


    }

}
