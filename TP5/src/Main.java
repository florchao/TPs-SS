package src;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Double mass = 0.001;
        //Double W = 0.2;
        Double W = Double.valueOf(args[0]);
        Double L = 0.7;
        Double dt = 0.001;
        Double maxTime = 50.0;
        Double holeSize = Double.valueOf(args[1]);
        //Double holeSize = 0.03;
        int N = 200;
        List<Particle> particleList = Utils.generateParticles(W, L, N, mass, dt);

//        GranularSystem system = new GranularSystem(
//                    L,
//                    W,
//                    dt,
//                    0.0,
//                    maxTime,
//                0.0,
//                "no_D_no_W",
//                particleList
//        );
//        system.run();

        VaryWidth varyWidth = new VaryWidth(particleList, W, L, dt, maxTime, holeSize);

        varyWidth.run();

        VaryHoleSize varyHoleSize = new VaryHoleSize(particleList, varyWidth.getBestFrequency(), W, L, dt, maxTime);

        varyHoleSize.run();


    }

}
