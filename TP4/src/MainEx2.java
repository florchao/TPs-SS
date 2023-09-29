package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainEx2 {
    private static double r = 2.25;
    private static double k = 2500;
    static double circleR =21.49;
    static double mass = 0.25;
    public static double tf = 100;
    private static List<List<ArrayList<Double>>> Rs = new ArrayList<>();

    private static final double[] alpha = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};


    //w = v/r
    private static double getForce(Particle particle){
        double v = Math.sqrt(Math.pow(particle.getVx(), 2) + Math.pow(particle.getVy(), 2));
        double w = v/circleR;
        return (particle.getU()/circleR - w);
    }

    //fuerza que le ejerce p1 sobre p2
    private static double collisionForce(Particle p1, Particle p2){
        return k*(Math.abs(p1.getAngle() - p2.getAngle()) - 2*r/circleR)*Math.signum(p1.getAngle() - p2.getAngle());
    }

    //w = movementEquation(p1, list)
    private static double movementEquation(Particle p1, List<Particle> particleList){
        double sumForces = 0;
        for (Particle p2 : particleList) {
            if (p2 != p1) {
                sumForces += collisionForce(p1, p2);
            }
        }
        return (getForce(p1) + sumForces)/p1.getM();
    }


    public static void gear(double dT, List<Particle> particles) throws IOException {

        double t = dT;
        List<List<ArrayList<Double>>> current = Rs;

        while(t <= tf) {
            for (Particle particle: particles) {
                particle.setX(current.get(particle.getId()).get(0).get(0));
                particle.setY(current.get(particle.getId()).get(0).get(1));
                particle.setVx(current.get(particle.getId()).get(1).get(0));
                particle.setVy(current.get(particle.getId()).get(1).get(1));
                particle.setAx(current.get(particle.getId()).get(2).get(0));
                particle.setAy(current.get(particle.getId()).get(2).get(1));

            }
            FileGenerator.writeOutput(particles, t);

            //predictions
            List<List<ArrayList<Double>>> newDerivatives = gearPredictor(current, dT, particles);

            //evaluator
            List<ArrayList<Double>> deltasR2 = getR2(newDerivatives, dT, particles);

            //correction
            current = gearCorrector(newDerivatives, dT, deltasR2);

            t += dT;
        }
    }

    public static void initialRs(List<Particle> particles){
        ArrayList<Double> r;
        for (Particle particle: particles) {
            List<ArrayList<Double>> auxR = new ArrayList<>();
            //r0
            r = new ArrayList<>();
            r.add(particle.getX());
            r.add(particle.getY());
            auxR.add(r);
            //r1
            r = new ArrayList<>();
            r.add(particle.getVx());
            r.add(particle.getVy());
            auxR.add(r);
            //r2
            r = new ArrayList<>();
            Double aux = movementEquation(particle, particles);
            r.add(aux);
            r.add(aux);
            auxR.add(r);
            //r3
            r = new ArrayList<>();
            r.add(0.0);
            r.add(0.0);
            auxR.add(r);
            //r4
            r = new ArrayList<>();
            r.add(0.0);
            r.add(0.0);
            auxR.add(r);
            //r5
            r = new ArrayList<>();
            r.add(0.0);
            r.add(0.0);
            auxR.add(r);

            Rs.add(auxR);
        }
    }

    public static List<List<ArrayList<Double>>> gearPredictor(List<List<ArrayList<Double>>> der, double dT, List<Particle> particles){
        List<List<ArrayList<Double>>> newDerivatives = new ArrayList<>();
        int count = 0;
        for(List<ArrayList<Double>> rs : der) {
            List<ArrayList<Double>> auxNewDerivatives = new ArrayList<>();

            double r0x = rs.get(0).get(0) + rs.get(1).get(0) * dT + rs.get(2).get(0) * Math.pow(dT, 2) / 2 + rs.get(3).get(0) * Math.pow(dT, 3) / 6 + rs.get(4).get(0) * Math.pow(dT, 4) / 24 + rs.get(5).get(0) * Math.pow(dT, 5) / 120;
            double r0y = rs.get(0).get(1) + rs.get(1).get(1) * dT + rs.get(2).get(1) * Math.pow(dT, 2) / 2 + rs.get(3).get(1) * Math.pow(dT, 3) / 6 + rs.get(4).get(1) * Math.pow(dT, 4) / 24 + rs.get(5).get(1) * Math.pow(dT, 5) / 120;
            ArrayList<Double> r0 = new ArrayList<>();
            r0.add(r0x);
            r0.add(r0y);
            particles.get(count).setX(r0x);
            particles.get(count).setY(r0y);
            auxNewDerivatives.add(r0);

            double r1x = rs.get(1).get(0) + rs.get(2).get(0) * dT + rs.get(3).get(0) * Math.pow(dT, 2) / 2 + rs.get(4).get(0) * Math.pow(dT, 3) / 6 + rs.get(5).get(0) * Math.pow(dT, 4) / 24;
            double r1y = rs.get(1).get(1) + rs.get(2).get(1) * dT + rs.get(3).get(1) * Math.pow(dT, 2) / 2 + rs.get(4).get(1) * Math.pow(dT, 3) / 6 + rs.get(5).get(1) * Math.pow(dT, 4) / 24;
            ArrayList<Double> r1 = new ArrayList<>();
            r1.add(r1x);
            r1.add(r1y);
            particles.get(count).setVx(r1x);
            particles.get(count).setVy(r1y);
            auxNewDerivatives.add(r1);

            double r2x = rs.get(2).get(0) + rs.get(3).get(0) * dT + rs.get(4).get(0) * Math.pow(dT, 2) / 2 + rs.get(5).get(0) * Math.pow(dT, 3) / 6;
            double r2y = rs.get(2).get(1) + rs.get(3).get(1) * dT + rs.get(4).get(1) * Math.pow(dT, 2) / 2 + rs.get(5).get(1) * Math.pow(dT, 3) / 6;
            ArrayList<Double> r2 = new ArrayList<>();
            r2.add(r2x);
            r2.add(r2y);
            particles.get(count).setAx(r2x);
            particles.get(count).setAy(r2y);
            auxNewDerivatives.add(r2);

            double r3x = rs.get(3).get(0) + rs.get(4).get(0) * dT + rs.get(5).get(0) * Math.pow(dT, 2) / 2;
            double r3y = rs.get(3).get(1) + rs.get(4).get(1) * dT + rs.get(5).get(1) * Math.pow(dT, 2) / 2;
            ArrayList<Double> r3 = new ArrayList<>();
            r3.add(r3x);
            r3.add(r3y);
            auxNewDerivatives.add(r3);

            double r4x = rs.get(4).get(0) + rs.get(5).get(0) * dT;
            double r4y = rs.get(4).get(1) + rs.get(5).get(1) * dT;
            ArrayList<Double> r4 = new ArrayList<>();
            r4.add(r4x);
            r4.add(r4y);
            auxNewDerivatives.add(r4);

            double r5x = rs.get(5).get(0);
            double r5y = rs.get(5).get(1);
            ArrayList<Double> r5 = new ArrayList<>();
            r5.add(r5x);
            r5.add(r5y);
            auxNewDerivatives.add(r5);

            newDerivatives.add(auxNewDerivatives);
            count++;
        }

        return newDerivatives;
    }

    private static List<ArrayList<Double>> getR2(List<List<ArrayList<Double>>> newDerivatives, double dT, List<Particle> particles){
        List<ArrayList<Double>> deltasR2 = new ArrayList<>();
        for(Particle particle : particles){

            Double F = movementEquation(particle, particles);
            ArrayList<Double> r2 = newDerivatives.get(particle.getId()).get(2);

            double dR2X = (F - r2.get(0)) * dT*dT / 2;
            double dR2Y = (F  - r2.get(1)) * dT*dT / 2;

            ArrayList<Double> deltaR2 = new ArrayList<>();
            deltaR2.add(dR2X);
            deltaR2.add(dR2Y);
            deltasR2.add(deltaR2);
        }
        return deltasR2;
    }

    public static List<List<ArrayList<Double>>> gearCorrector(List<List<ArrayList<Double>>> der, double dT, List<ArrayList<Double>>  dR2){
        List<List<ArrayList<Double>>> newDerivatives = new ArrayList<>();
        int count = 0;
        for(List<ArrayList<Double>> rs : der) {

            List<ArrayList<Double>> auxNewDerivatives = new ArrayList<>();

            double r0x = rs.get(0).get(0) + (alpha[0] * dR2.get(count).get(0));
            double r0y = rs.get(0).get(1) + (alpha[0] * dR2.get(count).get(0));
            ArrayList<Double> r0 = new ArrayList<>();
            r0.add(r0x);
            r0.add(r0y);
            auxNewDerivatives.add(r0);

            double r1x = rs.get(1).get(0) + (alpha[1] * dR2.get(count).get(0) * 1 ) / (dT);
            double r1y = rs.get(1).get(1) + (alpha[1] * dR2.get(count).get(1) * 1 ) / (dT);
            ArrayList<Double> r1 = new ArrayList<>();
            r1.add(r1x);
            r1.add(r1y);
            auxNewDerivatives.add(r1);


            double r2x = rs.get(2).get(0) + (alpha[2] * dR2.get(count).get(0) * 2) / (dT * dT);
            double r2y = rs.get(2).get(1) + (alpha[2] * dR2.get(count).get(1) * 2) / (dT * dT);
            ArrayList<Double> r2 = new ArrayList<>();
            r2.add(r2x);
            r2.add(r2y);
            auxNewDerivatives.add(r2);


            double r3x = rs.get(3).get(0) + (alpha[3] * dR2.get(count).get(0) * 6) / (Math.pow(dT, 3));
            double r3y = rs.get(3).get(1) + (alpha[3] * dR2.get(count).get(0) * 6) / (Math.pow(dT, 3));
            ArrayList<Double> r3 = new ArrayList<>();
            r3.add(r3x);
            r3.add(r3y);
            auxNewDerivatives.add(r3);


            double r4x = rs.get(4).get(0) + (alpha[4] * dR2.get(count).get(0) * 24) / (Math.pow(dT, 4));
            double r4y = rs.get(4).get(1) + (alpha[4] * dR2.get(count).get(0) * 24) / (Math.pow(dT, 4));
            ArrayList<Double> r4 = new ArrayList<>();
            r4.add(r4x);
            r4.add(r4y);
            auxNewDerivatives.add(r4);

            double r5x = rs.get(5).get(0) + (alpha[5] * dR2.get(count).get(0) * 120) / (Math.pow(dT, 5));
            double r5y = rs.get(5).get(1) + (alpha[5] * dR2.get(count).get(0) * 120) / (Math.pow(dT, 5));
            ArrayList<Double> r5 = new ArrayList<>();
            r5.add(r5x);
            r5.add(r5y);
            auxNewDerivatives.add(r5);

            count++;
            newDerivatives.add(auxNewDerivatives);
        }
        return newDerivatives;
    }

    public static void main(String[] args) throws IOException {
        int n = Integer.parseInt(args[0]);
        FileGenerator writeFiles = new FileGenerator();
        writeFiles.generateStaticFile("./input/inputFile.txt", r, n, mass, 0.01, circleR);

        List<Particle> particles = ParticleGenerator.generateParticles("./input/inputFile.txt");

        int P = Integer.parseInt(args[1]);
        double dT = Math.pow(10, -P);
        initialRs(particles);

        FileGenerator.createFiles("./input/positions.txt");
        gear(dT, particles);

    }

}
