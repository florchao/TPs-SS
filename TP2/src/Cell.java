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

    public List<Particle> getParticleList() {
        return particleList;
    }

}
