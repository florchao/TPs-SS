package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainEx2 {
    private static double r = 2.25;
    private static double k = 2500;
    static double L = 135;
    static double mass = 25;
    public static double tf = 100;
    private static List<List<Double>> Rs = new ArrayList<>();

    private static final double[] alpha = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};


    private static double getForce(Particle particle){
        return (particle.getU() - particle.getVx());
    }

    //fuerza que le ejerce p1 sobre p2
    private static double collisionForce(Particle p1, Particle p2){
        return k*(Math.abs(p1.getX() - p2.getX()) - 2*r)*Math.signum(p1.getX() - p2.getX());
    }

    //a = movementEquation(p1, list)
    private static double movementEquation(Particle p1, List<Particle> particleList, double dt){
        double sumForces = 0;
        for (Particle p2 : particleList) {
            if (p2 != p1 && p1.collides(p2, dt)) {
                sumForces += collisionForce(p2, p1);
            }
        }
        return (getForce(p1) + sumForces)/p1.getM();
    }


    public static void gear(double dT, List<Particle> particles) throws IOException {

        double t = dT;
        List<List<Double>> current = Rs;

        while(t <= tf) {
            for (Particle particle: particles) {
                particle.setX(current.get(particle.getId()).get(0));
                particle.setVx(current.get(particle.getId()).get(1));
                System.out.println("id:" + particle.getId() + "     vx:" + particle.getVx());
                particle.setAx(current.get(particle.getId()).get(2));

            }
            FileGenerator.writeOutput(particles, t);

            //predictions
            List<List<Double>> newDerivatives = gearPredictor(current, dT, particles);

            //evaluator
            List<Double> deltasR2 = getR2(newDerivatives, dT, particles);

            //correction
            current = gearCorrector(newDerivatives, dT, deltasR2);

            t += dT;
        }
    }

    public static void initialRs(List<Particle> particles, double dt){
        for (Particle particle: particles) {
            List<Double> auxR = new ArrayList<>();
            //r0
            auxR.add(particle.getX());
            //r1
            auxR.add(particle.getVx());
            //r2
            Double aux = movementEquation(particle, particles, dt);
            auxR.add(aux);
            //r3
            auxR.add(0.0);
            //r4
            auxR.add(0.0);
            //r5
            auxR.add(0.0);

            Rs.add(auxR);
        }
    }

    public static List<List<Double>> gearPredictor(List<List<Double>> der, double dT, List<Particle> particles){
        List<List<Double>> newDerivatives = new ArrayList<>();
        int count = 0;
        for(List<Double> rs : der) {
            List<Double> auxNewDerivatives = new ArrayList<>();

            double r0x = rs.get(0) + rs.get(1) * dT + rs.get(2) * Math.pow(dT, 2) / 2 + rs.get(3) * Math.pow(dT, 3) / 6 + rs.get(4) * Math.pow(dT, 4) / 24 + rs.get(5) * Math.pow(dT, 5) / 120;
            particles.get(count).setX(r0x);
            auxNewDerivatives.add(r0x);

            double r1x = rs.get(1) + rs.get(2) * dT + rs.get(3) * Math.pow(dT, 2) / 2 + rs.get(4) * Math.pow(dT, 3) / 6 + rs.get(5) * Math.pow(dT, 4) / 24;
            particles.get(count).setVx(r1x);
            auxNewDerivatives.add(r1x);

            double r2x = rs.get(2) + rs.get(3) * dT + rs.get(4) * Math.pow(dT, 2) / 2 + rs.get(5) * Math.pow(dT, 3) / 6;
            particles.get(count).setAx(r2x);
            auxNewDerivatives.add(r2x);

            double r3x = rs.get(3) + rs.get(4) * dT + rs.get(5) * Math.pow(dT, 2) / 2;
            auxNewDerivatives.add(r3x);

            double r4x = rs.get(4) + rs.get(5) * dT;
            auxNewDerivatives.add(r4x);

            double r5x = rs.get(5);
            auxNewDerivatives.add(r5x);

            newDerivatives.add(auxNewDerivatives);
            count++;
        }

        return newDerivatives;
    }

    private static List<Double> getR2(List<List<Double>> newDerivatives, double dT, List<Particle> particles){
        List<Double> deltasR2 = new ArrayList<>();
        for(Particle particle : particles){

            Double F = movementEquation(particle, particles,dT);
            Double r2 = newDerivatives.get(particle.getId()).get(2);

            double dR2X = (F - r2) * dT*dT / 2;

            deltasR2.add(dR2X);
        }
        return deltasR2;
    }

    public static List<List<Double>> gearCorrector(List<List<Double>> der, double dT, List<Double>  dR2){
        List<List<Double>> newDerivatives = new ArrayList<>();
        int count = 0;
        for(List<Double> rs : der) {

            List<Double> auxNewDerivatives = new ArrayList<>();

            double r0x = rs.get(0) + (alpha[0] * dR2.get(count));
            auxNewDerivatives.add(r0x % L);

            double r1x = rs.get(1) + (alpha[1] * dR2.get(count) * 1 ) / (dT);
            auxNewDerivatives.add(r1x);


            double r2x = rs.get(2) + (alpha[2] * dR2.get(count) * 2) / (dT * dT);
            auxNewDerivatives.add(r2x);


            double r3x = rs.get(3) + (alpha[3] * dR2.get(count) * 6) / (Math.pow(dT, 3));
            auxNewDerivatives.add(r3x);


            double r4x = rs.get(4) + (alpha[4] * dR2.get(count) * 24) / (Math.pow(dT, 4));
            auxNewDerivatives.add(r4x);

            double r5x = rs.get(5) + (alpha[5] * dR2.get(count) * 120) / (Math.pow(dT, 5));
            auxNewDerivatives.add(r5x);

            count++;
            newDerivatives.add(auxNewDerivatives);
        }
        return newDerivatives;
    }

    public static void main(String[] args) throws IOException {
        int n = Integer.parseInt(args[0]);
        FileGenerator writeFiles = new FileGenerator();
        writeFiles.generateStaticFile("./input/inputFile.txt", r, n, mass, L);

        List<Particle> particles = ParticleGenerator.generateParticles("./input/inputFile.txt");

        int P = Integer.parseInt(args[1]);
        double dT = Math.pow(10, -P);
        initialRs(particles, dT);

        FileGenerator.createFiles("./input/positions.txt");
        gear(dT, particles);

    }

}
