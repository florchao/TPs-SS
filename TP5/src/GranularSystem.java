package src;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GranularSystem implements Runnable{

    private final double dt;
    private final double frequency;
    private final List<Particle> particles;
    private final Grid grid;

    private final List<Double> limitsX;
    private final List<Double> limitsY;

    private final int iterations;
    private final String path;
    private final List<Double> times = new ArrayList<>();
    private final List<Double> energy = new ArrayList<>();

    public GranularSystem(double l, double w, double dt, double d, double maxTime, double frequency, String outputFileName, List<Particle> particles) {
        this.dt = dt;
        this.iterations = (int)(maxTime/dt);
        this.frequency = frequency;
        this.particles = particles.stream().map(Particle::copy).collect(Collectors.toList());

        this.limitsX = new ArrayList<>();
        this.limitsY = new ArrayList<>();

        this.limitsX.add(w);
        this.limitsY.add(l + l /10);

        this.limitsX.add(0.0);
        this.limitsY.add(l /10);

        this.limitsX.add(w);
        this.limitsY.add(0.0);

        this.limitsX.add(w / 2 - d / 2);
        this.limitsY.add(l/10);

        this.limitsX.add(w / 2 + d / 2);
        this.limitsY.add(l/10);

        this.grid = new Grid(limitsX.get(0), limitsY.get(0), limitsX.get(1), limitsY.get(1), d);

        grid.addAll(this.particles);

        this.path = Ovito.createFile(outputFileName, "xyz");
        Ovito.writeParticlesToFileXyz(path, this.particles, this.limitsX, this.limitsY);
        
    }

    @Override
    public void run() {

        for (int i = 0; i < iterations; i++) {

            grid.shake(i * dt, frequency);

            particles.forEach(Particle::prediction);
            particles.forEach(Particle::resetForce);

            for (int j = 0; j < grid.update(); j++)
                times.add(i * dt);

            grid.updateForces();

            particles.forEach(Particle::correction);

            particles.forEach(Particle::resetForce);
            grid.updateForces();

            if (i % 100 == 0) {
                if (i % 100000 == 0)
                    System.out.println(hashCode() + ": iteración-" + i);
                energy.add(particles.stream().mapToDouble(Particle::getEnergy).sum());
                Ovito.writeParticlesToFileXyz(path, particles, limitsX, limitsY);
            }
        }

    }

    public List<Double> getTimes() {
        return times;
    }

    public double getCaudal(){
        return times.size() / (iterations * dt);
    }

    public List<Double> getEnergy() {
        return energy;
    }
}
