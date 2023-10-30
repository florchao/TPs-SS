package src;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Double mass = 1.0;
        Double W = Double.valueOf(args[0]);
        Double L = 70.0;
        Double dt = 0.001;
        Double maxTime = 1000.0;
        Double holeSize = Double.valueOf(args[1]);
        int N = 200;
        List<Particle> particleList = Utils.generateParticles(W, L, N, mass, dt);

        VaryWidth varyWidth = new VaryWidth(particleList, W, L, dt, maxTime, holeSize);

        varyWidth.run();

        VaryHoleSize varyHoleSize = new VaryHoleSize(particleList, varyWidth.getBestFrequency(), W, L, dt, maxTime);

        varyHoleSize.run();


    }

}
