import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid {
    private final double L;
    private int M;
    private final double radius;
    private Cell[][] cells;

    public Grid(double L, double rc) {
        this.L = L;
        this.radius = rc;
        this.M = (int) Math.floor(L/rc);
        if (this.M == L/rc) {
            this.M -=1;
        } else if (this.M == 0) {
            this.M = 1;
        }
        this.cells = new Cell[M][M];
    }

    public HashMap<Integer, List<Integer>> getAllNeighbours(){
        HashMap<Integer,List<Integer>> neighbours = new HashMap<>();

        for (int i = 0; i < M; i++){
            for (int j = 0; j < M; j++) {
                for (Particle particle : cells[i][j].getParticles()) {

                    if(!neighbours.containsKey(particle.getId())) {
                        neighbours.put(particle.getId(), new ArrayList<>());
                    }

                    checkMyNeighbours(neighbours.get(particle.getId()), particle, cells[i][j].getParticles());
                    checkNeighbours(particle,neighbours, cells[(i+ M -1)% M][(j+1)% M].getParticles());
                    checkNeighbours(particle,neighbours, cells[i][(j+1)% M].getParticles());
                    checkNeighbours(particle,neighbours, cells[(i+1)% M][(j+1)% M].getParticles());
                    checkNeighbours(particle,neighbours, cells[(i+1)% M][j].getParticles());
                }
            }
        }
        return neighbours;
    }

    private void checkMyNeighbours(List<Integer> neighbours, Particle particle, List<Particle> neighbourCell){
        for(Particle neighbour : neighbourCell){
            if(neighbour.getId() != particle.getId()){
                if(getDistance(particle, neighbour) <= radius){
                    neighbours.add(neighbour.getId());
                }
            }
        }
    }

    private void checkNeighbours(Particle particle, HashMap<Integer, List<Integer>> neighbours, List<Particle> neighbourCell){
        for (Particle neighbour : neighbourCell) {
            double distance = getDistance(particle, neighbour);
            if(distance <= radius){
                neighbours.get(particle.getId()).add(neighbour.getId());
                if(!neighbours.containsKey(neighbour.getId())){
                    neighbours.put(neighbour.getId(), new ArrayList<>());
                }
                neighbours.get(neighbour.getId()).add(particle.getId());
            }
        }
    }
    public void fill(List<Particle> particles) {
        int rowIndex;
        int colIndex;

        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                cells[i][j] = new Cell();
            }
        }

        for (Particle particle : particles) {
            rowIndex = ((int) (particle.getX() / ((float) L / M))) % M;
            colIndex = ((int) (particle.getY() / ((float) L / M))) % M;
            cells[rowIndex][colIndex].addParticle(particle);
        }
    }

    public double getDistance(Particle p1, Particle p2){
        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());
        deltaX -= deltaX > ((L*1.0f) / 2 ) ? L : 0;
        deltaY -= deltaY > ((L*1.0f) / 2) ? L : 0;

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }
}
