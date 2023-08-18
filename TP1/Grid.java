import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {
    private int L;
    private int M;

    private Cell[][] cells;

    public Grid(int size) {
        this.M = size;
        this.cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public int getSize() {
        return M;
    }

    public void setSize(int size) {
        this.M = size;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public void addParticle(Particle particle){
        int row = ((int) (particle.getX() * M / ((float) L))) % M;
        int col = ((int) (particle.getY() * M / ((float) L))) % M;
        cells[row][col].addParticle(particle);
    }

    public List<Particle> getParticles(int row, int column){
        if( row > M || column > M ) {
            return null;
        }
        return cells[row][column].getParticleList();
    }

    public List<Particle> getAllParticles(){
        ArrayList<Particle> allParticles = new ArrayList<>(Collections.emptyList());
        for (int i=0; i < M ; i++) {
            for (int j=0 ; j< M ; j++) {
                allParticles.addAll(cells[i][j].getParticleList());
            }
        }
        return allParticles;
    }

}
