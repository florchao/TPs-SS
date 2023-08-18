import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Particle> particleList;

    public Cell(){
        this.particleList = new ArrayList<>();
    }

    public void addParticle(Particle particle) {
        particleList.add(particle);
    }

    public void addAllParticle(List<Particle> particles) {
        particleList.addAll(particles);
    }

    public List<Particle> getParticleList() {
        return particleList;
    }

    public void setParticleList(List<Particle> particleList) {
        this.particleList = particleList;
    }
}
