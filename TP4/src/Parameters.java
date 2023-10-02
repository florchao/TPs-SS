package src;
import java.util.List;

public class Parameters {
    private List<Particle> particles;

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }
}