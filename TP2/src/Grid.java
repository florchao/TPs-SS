import java.util.List;

public class Grid {
    private final double L;
    private int M;
    private final double rc;
    private Cell[][] cells;

    public Grid(double L, double rc) {
        this.L = L;
        this.rc = rc;
        this.M = (int) Math.floor(L/rc);
        if (this.M == L/rc) {
            this.M -=1;
        } else if (this.M == 0) {
            this.M = 1;
        }
        this.cells = new Cell[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public int getSize() {
        return M;
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

}
