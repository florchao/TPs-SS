package src;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Silo implements Runnable {

    private final double dt;
    private final double holeSize;
    private final double angularFrequency;
    private final int iterations;
    private final String path;
    private final List<Double> times = new ArrayList<>();

    private final Grid grid;
    private final List<Double> limitsX;
    private final List<Double> limitsY;
    private final List<Particle> particles;

    public Silo(double l, double w, double dt, double d, double maxTime, double angularFrequency, String outputFileName, List<Particle> particles) {
        this.dt = dt;
        this.holeSize = d;
        this.angularFrequency = angularFrequency;
        this.iterations = (int) (maxTime / dt);
        this.particles = particles.stream().map(Particle::copy).collect(Collectors.toList());

        this.limitsX = List.of(w, 0.0, w, w / 2 - d / 2, w / 2 + d / 2);
        this.limitsY = List.of(l + l / 10, l / 10, 0.0, l / 10, l / 10);

        this.grid = new Grid(limitsX.get(0), limitsY.get(0), limitsX.get(1), limitsY.get(1), d);
        grid.addAll(this.particles);

        this.path = Utils.createFile(outputFileName, "xyz");
        Utils.writeParticlesToFileXyz(path, this.particles, this.limitsX, this.limitsY);
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            grid.shake(i * dt, angularFrequency);
            updateParticles(i);
            updateGrid();
            updateForces();
            updateParticles(i);
            updateForces();

            if (i % 100 == 0) {
                Utils.writeParticlesToFileXyz(path, particles, limitsX, limitsY);
            }
        }
    }

    private void updateParticles(int iteration) {
        particles.forEach(Particle::prediction);
        particles.forEach(Particle::resetForce);
        for (int j = 0; j < grid.update(); j++)
            times.add(iteration * dt);
    }

    private void updateGrid() {
        grid.addAll(particles);
    }

    private void updateForces() {
        grid.updateForces(dt);
        particles.forEach(Particle::correction);
        particles.forEach(Particle::resetForce);
        grid.updateForces(dt);
    }

    public List<Double> getTimes() {
        return times;
    }

    public double getFlow() {
        return times.size() / (iterations * dt);
    }

    public double getHoleSize() {
        return holeSize;
    }
}
