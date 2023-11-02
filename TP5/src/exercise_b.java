package src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class exercise_b {
    private final List<Particle> particleList;
    private final double freq;
    private final Double W;
    private final Double L;
    private final Double dt;
    private final Double maxTime;

    public exercise_b(List<Particle> particleList, double freq, Double w, Double l, Double dt, Double maxTime) {
        this.particleList = particleList;
        this.freq = freq;
        this.W = w;
        this.L = l;
        this.dt = dt;
        this.maxTime = maxTime;
    }

    public void run() throws InterruptedException {

        double[] holeSizes = {3, 4, 5, 6};
        List<Silo> systems = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(holeSizes.length);

        for (double holeSize : holeSizes) {
            Silo system = new Silo(
                    getL(),
                    getW(),
                    getDt(),
                    holeSize,
                    getMaxTime(),
                    freq,
                    "output_D_" + holeSize + "_",
                    particleList
            );
            systems.add(system);
            executor.execute(system);
        }

        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.HOURS)) {
            throw new IllegalStateException("Threads timeout");
        }

        List<Double> flows = systems.stream().map(Silo::getFlow).collect(Collectors.toList());
        Utils.writeListToFile(flows, Utils.createFile("Flows_D", "txt"), true);

        for (Silo system : systems) {
            Utils.writeListToFile(
                    system.getTimes(),
                    Utils.createFile("times_D_" + system.getHoleSize(), "txt"),
                    true
            );
        }
    }

    public Double getW() {
        return W;
    }

    public Double getL() {
        return L;
    }

    public Double getDt() {
        return dt;
    }

    public Double getMaxTime() {
        return maxTime;
    }
}
