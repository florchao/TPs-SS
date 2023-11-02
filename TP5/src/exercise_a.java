package src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class exercise_a {
    private final List<Particle> particleList;
    private final Double W;
    private final Double L;
    private final Double dt;
    private final Double maxTime;
    private final Double holeSize;
    private Double bestAngularFrequency;

    public exercise_a(List<Particle> particleList, Double w, Double l, Double dt, Double maxTime, Double holeSize) {
        this.particleList = particleList;
        W = w;
        L = l;
        this.dt = dt;
        this.maxTime = maxTime;
        this.holeSize = holeSize;
    }

    public void run() throws InterruptedException {
        double[] frequencies = {5, 10, 15, 20, 30, 50};
        List<Silo> systems = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(frequencies.length);

        for (double freq : frequencies) {
            Silo system = new Silo(
                    getL(),
                    getW(),
                    getDt(),
                    getHoleSize(),
                    getMaxTime(),
                    freq,
                    "output_F_" + freq + "_",
                    this.particleList
            );
            systems.add(system);
            executor.execute(system);
        }

        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.HOURS))
            throw new IllegalStateException("Threads timeout");

        List<Double> flows = systems.stream().map(Silo::getFlow).collect(Collectors.toList());

        this.bestAngularFrequency = flows.get(flows.indexOf(flows.stream().max(Double::compareTo).get()));

        Utils.writeListToFile(
                flows,
                Utils.createFile("Flows_F", "txt"),
                true
        );

        for (Silo system : systems) {
            Utils.writeListToFile(
                    system.getTimes(),
                    Utils.createFile("times_F", "txt"),
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

    public Double getHoleSize() {
        return holeSize;
    }

    public Double getBestAngularFrequency() {
        return bestAngularFrequency;
    }
}
