package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static src.Euler.*;
import static src.Beeman.*;
import static src.Gear.*;
import static src.Verlet.verletVelocity;
import static src.Verlet.verletX;

public class Oscilator {
    public static double m = 70;
    public static double k = 10000;
    public static double gamma = 100;
    public static double tf = 5;
    public static void main(String[] args) {

        Particle particle = new Particle(1,0,-(100.0 / (2*m)),0,m);

        double dt = Double.parseDouble(args[0]);

        String method = args[1];

        List<ArrayList<Double>> frames = switch (method) {
            case "Beeman" -> beeman(particle, dt);
            case "Verlet" -> verlet(particle, dt);
            case "Gear" -> gear(particle, dt);
            default -> null;
        };

        outputGenerator(frames, args[2]);
    }

    private static void outputGenerator(List<ArrayList<Double>> frames, String outputFile){
        try {
            java.io.FileWriter myWriter = new java.io.FileWriter("./output/" + outputFile);
            for (ArrayList<Double> frame : frames) {
                myWriter.write(frame.get(0) + "\t" + frame.get(1) + "\t" + frame.get(2) + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    private static void addCurrentStatus(Particle particle, double t, List<ArrayList<Double>> status){
        ArrayList<Double> currentStatus = new ArrayList<>();
        currentStatus.add(t);
        currentStatus.add(particle.getX());
        currentStatus.add(particle.getVx());
        status.add(currentStatus);
    }
    public static List<ArrayList<Double>> verlet(Particle particle, double dt){

        double f = -k*particle.getX() - gamma*particle.getVx();
        double initialX = eulerX(particle.getX(), particle.getVx(), -dt, f, particle.getM());

        double x;
        double v;
        double t = 0;
        List<ArrayList<Double>> status = new ArrayList<>();

        while(t <= tf){
            addCurrentStatus(particle, t, status);

            f = -k*particle.getX() - gamma*particle.getVx();
            x = verletX(particle.getX(), initialX, f, particle.getM(), dt);

            if(t != 0) {
                v = verletVelocity(x, initialX, dt);
                particle.setVx(v);
            }

            initialX = particle.getX();
            particle.setX(x);
            t += dt;
        }
        return status;
    }

    public static List<ArrayList<Double>> beeman(Particle particle, double dt){

        double f = -k*particle.getX() - gamma*particle.getVx();
        double initialX = eulerX(particle.getX(), particle.getVx(),-dt,f,particle.getM());
        double initialV = eulerVelocity(particle.getVx(),f,particle.getM(),-dt);
        double initialA = (-k*initialX - gamma*initialV)/particle.getM();

        double x;
        double v;
        double newV;
        double t = 0;
        double aAfter;
        List<ArrayList<Double>> status = new ArrayList<>();

        while(t <= tf){
            addCurrentStatus(particle, t, status);

            f = -k*particle.getX() - gamma*particle.getVx();
            x = beemanX(particle.getX(), particle.getVx(), f/particle.getM(),initialA,dt);
            f = -k*x - gamma*particle.getVx();
            v = beemanVelocityP(particle.getVx(),f/particle.getM(),initialA,dt);
            aAfter = (-k*x - gamma*v)/particle.getM();
            f = -k* particle.getX() - gamma*particle.getVx();
            newV = beemanVelocityC(particle.getVx(),initialA,f/particle.getM(),aAfter,dt);

            initialA = (-k*particle.getX() - gamma*particle.getVx())/particle.getM();

            particle.setVx(newV);
            particle.setX(x);
            t += dt;
        }
        return status;

    }

    public static List<ArrayList<Double>> gear(Particle particle, double dt){

        List<ArrayList<Double>> status = new ArrayList<>();
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

            List<Double> newDerivatives = gearP(derivatives, dt);
            dA = (-k*newDerivatives.get(0) - gamma*newDerivatives.get(1))/particle.getM() - newDerivatives.get(2);
            dR2 = dA * dt*dt / 2;
            derivatives = gearC(newDerivatives, dt, alpha, dR2);

            particle.setX(derivatives.get(0));
            particle.setVx(derivatives.get(1));

            t += dt;
        }
        return status;
    }

}