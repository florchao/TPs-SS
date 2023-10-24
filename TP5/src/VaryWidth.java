package src;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class VaryWidth {
    private final List<Particle> particleList;
    private final Double W;
    private final Double L;
    private final Double dt;
    private final Double maxTime;
    private final  Double holeSize;
    private Double bestFrequency;

    public VaryWidth(List<Particle> particleList, Double w, Double l, Double dt, Double maxTime, Double holeSize) {
        this.particleList = particleList;
        W = w;
        L = l;
        this.dt = dt;
        this.maxTime = maxTime;
        this.holeSize = holeSize;
    }

    public void run() throws InterruptedException {

        double[] frequencies = {20};

        List<GranularSystem> systems = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(frequencies.length);

        for (double freq : frequencies) {
            GranularSystem system = new GranularSystem(
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

        List<Double> caudals = systems.stream().map(GranularSystem::getCaudal).collect(Collectors.toList());

        this.bestFrequency = caudals.get(caudals.indexOf(caudals.stream().max(Double::compareTo).get()));

        Ovito.writeListToFIle(
                caudals,
                Ovito.createFile("caudals_F", "txt"),
                true
        );

        for (GranularSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getTimes(),
                    Ovito.createFile("times_F", "txt"),
                    true
            );
        }

        for (GranularSystem system :
                systems) {
            Ovito.writeListToFIle(
                    system.getEnergy(),
                    Ovito.createFile("energy_F", "txt"),
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

    public Double getBestFrequency() {
        return bestFrequency;
    }
}
