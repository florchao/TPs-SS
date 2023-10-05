package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Oscilator_ex1 {
    public static double m = 70;
    public static double k = 10000;
    public static double gamma = 100;
    public static double tf = 5;
    public static void main(String[] args) {
        Double[] dT = {0.005, 0.001, 0.0005, 0.0001, 0.00005, 0.00001};
        String[] methods = {"Beeman", "Verlet", "Gear"};
        for (String method : methods) {
            ParticleOscilator particle = new ParticleOscilator(1,0,-(100.0 / (2*m)),0,m);
            for (Double dt : dT) {
                List<List<Double>> frames = switch (method) {
                    case "Beeman" -> beeman(particle, dt);
                    case "Verlet" -> verlet(particle, dt);
                    case "Gear" -> gear(particle, dt);
                    default -> null;
                };
                String outputFile = method + "_" + (dt+1);
                if (frames != null) {
                    outputGenerator(frames, outputFile);
                }
            }
        }
    }

    private static void outputGenerator(List<List<Double>> frames, String outputFile){
        try {
            FileWriter outputWriter = new java.io.FileWriter("./output/" + outputFile);
            for (List<Double> frame : frames) {
                outputWriter.write(frame.get(0) + "\t" + frame.get(1) + "\t" + frame.get(2) + "\n");
            }
            outputWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    private static void addCurrentStatus(ParticleOscilator particle, double t, List<List<Double>> status){
        List<Double> newStatus = new ArrayList<>();
        newStatus.add(t);
        newStatus.add(particle.getX());
        newStatus.add(particle.getVx());
        status.add(newStatus);
    }
    public static List<List<Double>> verlet(ParticleOscilator particle, double dt){
        double f = -k*particle.getX() - gamma*particle.getVx();
        double initialX = Algorithms.eulerX(particle.getX(), particle.getVx(), -dt, f, particle.getM());
        double x;
        double v;
        double t = 0;
        List<List<Double>> status = new ArrayList<>();
        while(t <= tf){
            addCurrentStatus(particle, t, status);
            f = -k*particle.getX() - gamma*particle.getVx();
            x = Algorithms.verletX(particle.getX(), initialX, f, particle.getM(), dt);
            if(t != 0) {
                v = Algorithms.verletVelocity(x, initialX, dt);
                particle.setVx(v);
            }
            initialX = particle.getX();
            particle.setX(x);
            t += dt;
        }
        return status;
    }

    public static List<List<Double>> beeman(ParticleOscilator particle, double dt){
        double f = -k*particle.getX() - gamma*particle.getVx();
        double initialX = Algorithms.eulerX(particle.getX(), particle.getVx(),-dt,f,particle.getM());
        double initialV = Algorithms.eulerVelocity(particle.getVx(),f,particle.getM(),-dt);
        double initialA = (-k*initialX - gamma*initialV)/particle.getM();
        double x;
        double v;
        double newV;
        double t = 0;
        double aAfter;
        List<List<Double>> status = new ArrayList<>();
        while(t <= tf){
            addCurrentStatus(particle, t, status);
            f = -k*particle.getX() - gamma*particle.getVx();
            x = Algorithms.beemanX(particle.getX(), particle.getVx(), f/particle.getM(),initialA,dt);
            f = -k*x - gamma*particle.getVx();
            v = Algorithms.beemanVelocityP(particle.getVx(),f/particle.getM(),initialA,dt);
            aAfter = (-k*x - gamma*v)/particle.getM();
            f = -k* particle.getX() - gamma*particle.getVx();
            newV = Algorithms.beemanVelocityC(particle.getVx(),initialA,f/particle.getM(),aAfter,dt);
            initialA = (-k*particle.getX() - gamma*particle.getVx())/particle.getM();
            particle.setVx(newV);
            particle.setX(x);
            t += dt;
        }
        return status;
    }

    public static List<List<Double>> gear(ParticleOscilator particle, double dt){
        List<List<Double>> status = new ArrayList<>();
        double t = 0;
        double[] alpha = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};
        double dA;
        double dR2;
        double f = -k*particle.getX() - gamma*particle.getVx();
        List<Double> derivatives = new ArrayList<>();
        derivatives.add(particle.getX());
        derivatives.add(particle.getVx());
        derivatives.add(f/particle.getM());
        derivatives.add((-k *derivatives.get(1) - gamma*derivatives.get(2))/particle.getM());
        derivatives.add((-k *derivatives.get(2) - gamma*derivatives.get(3))/particle.getM());
        derivatives.add((-k *derivatives.get(3) - gamma*derivatives.get(4))/particle.getM());
        while(t <= tf) {
            addCurrentStatus(particle, t, status);
            List<Double> newDerivatives = Algorithms.gearP(derivatives, dt);
            dA = (-k*newDerivatives.get(0) - gamma*newDerivatives.get(1))/particle.getM() - newDerivatives.get(2);
            dR2 = dA * dt*dt / 2;
            derivatives = Algorithms.gearC(newDerivatives, dt, alpha, dR2);
            particle.setX(derivatives.get(0));
            particle.setVx(derivatives.get(1));
            t += dt;
        }
        return status;
    }
}