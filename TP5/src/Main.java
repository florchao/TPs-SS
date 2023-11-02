package src;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Usage: java Main <width> <holeSize>");
            return;
        }

        Double W = Double.valueOf(args[0]);
        Double holeSize = Double.valueOf(args[1]);
        Double L = 70.0;
        Double dt = 0.001;
        Double maxTime = 1000.0;
        int N = 200;
        double mass = 1.0;

        List<Particle> particleList = Utils.generateParticles(W, L, N, mass, dt);

        exercise_a varyWidth = new exercise_a(particleList, W, L, dt, maxTime, holeSize);
        varyWidth.run();

        exercise_b varyHoleSize = new exercise_b(particleList, varyWidth.getBestAngularFrequency(), W, L, dt, maxTime);
        varyHoleSize.run();
    }
}
