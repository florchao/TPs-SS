package src;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static src.Utils.*;

public class Grid {
    private static final double A = 0.15;
    private static final double GRAVITY = 9.81;
    private static final double ZERO = 0.0;
    private static final double DIM_X = 20;
    private static final double DIM_Y = 77;
    private static final int cols = 8;
    private static final int rowsInside = 30;
    private static final int rowsTotal = 33;
    private static final double CELL_DIMENSION_Y = DIM_Y / (double) rowsTotal;
    private static final double CELL_DIMENSION_X = DIM_X / (double) cols;

    private final double topRightLimitX;
    private double topRightLimitY;

    private final double bottomLeftLimitX;
    private double bottomLeftLimitY;

    private final double topRightLimitInitialY;
    private final double bottomLeftLimitInitialY;
    private final double leftLimitHole;
    private final double rightLimitHole;
    private double movement;

    private final Cell[][] cells;

    public Grid(double topRightLimitX, double topRightLimitY, double bottomLeftLimitX, double bottomLeftLimitY, double holeSize) {
        cells = new Cell[rowsTotal][cols];
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell();
            }
        }
        this.bottomLeftLimitX = bottomLeftLimitX;
        this.bottomLeftLimitY = bottomLeftLimitY;

        this.topRightLimitX = topRightLimitX;
        this.topRightLimitY = topRightLimitY;

        this.bottomLeftLimitInitialY = bottomLeftLimitY;
        this.topRightLimitInitialY = topRightLimitY;
        leftLimitHole = topRightLimitX / 2 - holeSize / 2;
        rightLimitHole = topRightLimitX / 2 + holeSize / 2;
    }

    public void shake(double t, double w) {
        movement = A * Math.sin(w * t);
        bottomLeftLimitY = bottomLeftLimitInitialY + movement;
        topRightLimitY = topRightLimitInitialY + movement;
    }

    public void add(Particle particle) {
        Cell cell = getCell(particle.getPosition().getX(), particle.getPosition().getY());
        if (cell != null) {
            cell.add(particle);
        } else throw new IllegalStateException("Invalid cell");
    }

    public void addAll(List<Particle> particles) {
        particles.forEach(this::add);
    }

    private static final Coordinates FloorNormalVersor = new Coordinates(ZERO, -1.0);
    private static final Coordinates TopNormalVector = new Coordinates(ZERO, 1.0);
    private static final Coordinates LeftNormalVector = new Coordinates(-1.0, ZERO);
    private static final Coordinates RightNormalVector = new Coordinates(1.0, ZERO);

    private boolean outsideHole(Particle particle) {
        return particle.getPosition().getX() < leftLimitHole || particle.getPosition().getX() > rightLimitHole;
    }

    public void updateForces(double dt) {
        for (int row = 0; row < rowsTotal; row++) {
            for (int col = 0; col < cols; col++) {
                List<Particle> neighbours = getNeighbours(row, col);
                List<Particle> current = cells[row][col].getParticles();

                current.forEach(
                        p -> {
                            p.addToForce(ZERO, p.getMass() * GRAVITY);

                            current.forEach(n -> {
                                if (!n.equals(p)) {
                                    double diff = p.getPosition().distance(n.getPosition());
                                    double addRad = p.getRadius() + n.getRadius();

                                    if (diff < addRad) {
                                        Coordinates normalVersor = n.getPosition().subtract(p.getPosition()).normalize();
                                        Coordinates relativeVelocity = p.getVelocity().subtract(n.getVelocity());
                                        double superpositionB = relativeVelocity.getX() + relativeVelocity.getY();
                                        p.addToForce(getNormalForce(addRad - diff, normalVersor, superpositionB));
                                        p.addToForce(getTangentialForce(addRad - diff, relativeVelocity, normalVersor, superpositionB, dt));
                                    }
                                }
                            });

                            neighbours.forEach(n -> {
                                double diff = p.getPosition().distance(n.getPosition());
                                double superposition = p.getRadius() + n.getRadius() - diff;

                                if (superposition > ZERO && !n.equals(p)) {
                                    Coordinates normalVersor = n.getPosition().subtract(p.getPosition()).normalize();
                                    Coordinates relativeVelocity = p.getVelocity().subtract(n.getVelocity());
                                    double superpositionB = relativeVelocity.getX() + relativeVelocity.getY();

                                    Coordinates normalForce = getNormalForce(superposition, normalVersor, superpositionB);
                                    Coordinates tangentialForce = getTangentialForce(superposition, relativeVelocity, normalVersor, superpositionB, dt);

                                    p.addToForce(normalForce);
                                    n.addToForce(normalForce.scale(-1.0));
                                    p.addToForce(tangentialForce);
                                    n.addToForce(tangentialForce.scale(-1.0));
                                }
                            });
                        }
                );

                if (row <= (rowsTotal - rowsInside)) {
                    updateForceFloor(current, dt);
                }

                if (row == rowsTotal - 1) {
                    updateForceTop(current, dt);
                }

                if (col == 0) {
                    updateForceLeftWall(current, dt);
                }

                if (col == cols - 1) {
                    updateForceRightWall(current, dt);
                }
            }
        }
    }

    private void updateForceFloor(List<Particle> particles, double dt) {
        particles.forEach(p -> {
            if (outsideHole(p) && !p.isGone()) {
                floorForce(p, dt);
            }
        });
    }

    private void floorForce(Particle p, double dt) {
        double superposition = p.getRadius() - (p.getPosition().getY() - bottomLeftLimitY);
        if (superposition > ZERO) {
            Coordinates auxVelocity = p.getVelocity();
            p.setFloorRelativeVelocity(p.getFloorRelativeVelocity().add(p.getVelocity()));
            double superpositionB = auxVelocity.getX() + auxVelocity.getY();
            p.addToForce(getWallForce(superposition, p.getFloorRelativeVelocity(), FloorNormalVersor, superpositionB, dt));
        } else {
            p.setFloorRelativeVelocity(Coordinates.ZERO);
        }
    }

    private void updateForceTop(List<Particle> particles, double dt) {
        particles.forEach(p -> topForce(p, dt));
    }

    private void topForce(Particle p, double dt) {
        double superposition = p.getRadius() - (topRightLimitY - p.getPosition().getY());
        if (superposition > ZERO) {
            Coordinates auxVelocity = p.getVelocity();
            double superpositionB = auxVelocity.getX() + auxVelocity.getY();
            p.addToForce(getWallForce(superposition, p.getVelocity(), TopNormalVector, superpositionB, dt));
        }
    }

    private void updateForceLeftWall(List<Particle> particles, double dt) {
        particles.forEach(p -> leftForce(p, dt));
    }

    private void leftForce(Particle p, double dt) {
        double superposition = p.getRadius() - (p.getPosition().getX() - bottomLeftLimitX);
        if (superposition > ZERO) {
            Coordinates auxVelocity = p.getVelocity();
            p.setLeftRelativeVelocity(p.getLeftRelativeVelocity().add(p.getVelocity()));
            double superpositionB = auxVelocity.getX() + auxVelocity.getY();
            p.addToForce(getWallForce(superposition, p.getLeftRelativeVelocity(), LeftNormalVector, superpositionB, dt));
        } else {
            p.setLeftRelativeVelocity(Coordinates.ZERO);
        }
    }

    private void updateForceRightWall(List<Particle> particles, double dt) {
        particles.forEach(p -> rightForce(p, dt));
    }

    private void rightForce(Particle p, double dt) {
        double superposition = p.getRadius() - (topRightLimitX - p.getPosition().getX());
        if (superposition > ZERO) {
            Coordinates auxVelocity = p.getVelocity();
            p.setRightRelativeVelocity(p.getRightRelativeVelocity().add(p.getVelocity()));
            double superpositionB = auxVelocity.getX() + auxVelocity.getY();
            p.addToForce(getWallForce(superposition, p.getVelocity(), RightNormalVector, superpositionB, dt));
        } else {
            p.setRightRelativeVelocity(Coordinates.ZERO);
        }
    }

    private List<Particle> getNeighbours(int row, int col) {
        List<Particle> particles = new ArrayList<>();

        if (row < rowsTotal - 1) particles.addAll(cells[row + 1][col].getParticles());
        if (row < rowsTotal - 1 && col < cols - 1) particles.addAll(cells[row + 1][col + 1].getParticles());
        if (col < cols - 1) particles.addAll(cells[row][col + 1].getParticles());
        if (row > 0 && col < cols - 1) particles.addAll(cells[row - 1][col + 1].getParticles());

        return particles;
    }

    public int update() {
        int goneParticles = 0;
        for (int i = 0; i < rowsTotal; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < cells[i][j].getParticles().size(); k++) {
                    if (!updateParticleCell(cells[i][j].getParticles().get(k), i, j)) {
                        goneParticles += 1;
                    }
                }
            }
        }
        return goneParticles;
    }

    private Cell getCell(double x, double y) {
        if (x >= DIM_X || x < 0 || y < 0 || y >= DIM_Y) throw new IllegalStateException();
        int row = getIndexY(y);
        int col = getIndexX(x);
        return cells[row][col];
    }

    private int getIndexX(double value) {
        return (int) (value / CELL_DIMENSION_X);
    }

    private int getIndexY(double value) {
        return (int) (value / CELL_DIMENSION_Y);
    }

    private boolean moveFromCell(Particle particle, int row, int col, int newRow, int newCol) {
        try {
            if (newRow < 0) {
                particle.reInject();
                cells[row][col].remove(particle);
                boolean overlap;
                int c, r;
                do {
                    overlap = false;
                    particle.getPosition().setX(particle.getRadius() + Math.random() * (DIM_X - 2.0 * particle.getRadius()));
                    particle.getPosition().setY(40 + 70 / 10 + Math.random() * ((70 - 40) - particle.getRadius()));
                    c = getIndexX(particle.getPosition().getX());
                    r = getIndexY(particle.getPosition().getY());
                    for (Particle existingParticle : getAllNeighbours(r, c)) {
                        if (checkOverlap(particle, existingParticle)) overlap = true;
                    }
                } while (overlap);
                cells[r][c].add(particle);
                particle.setGone(false);
                return false;
            } else {
                cells[newRow][newCol].add(particle);
                cells[row][col].remove(particle);
                if (particle.isGone() && particle.getColor().equals(Color.RED)) particle.setColor(Color.WHITE);
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    public List<Particle> getAllNeighbours(int row, int col) {
        List<Particle> neighbours = new ArrayList<>();

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (isValidCell(i, j) && !(i == row && j == col)) {
                    neighbours.addAll(cells[i][j].getParticles());
                }
            }
        }

        return neighbours;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rowsTotal && col >= 0 && col < cols;
    }

    private boolean updateParticleCell(Particle particle, int row, int col) {
        Coordinates inferiorLimit = new Coordinates(((double) col) * CELL_DIMENSION_X, ((double) row) * CELL_DIMENSION_Y + movement);
        Coordinates superiorLimit = new Coordinates(((double) (col + 1)) * CELL_DIMENSION_X, ((double) (row + 1)) * CELL_DIMENSION_Y + movement);
        Coordinates inferiorDiff = particle.getPosition().subtract(inferiorLimit);
        Coordinates superiorDiff = particle.getPosition().subtract(superiorLimit);
        return moveFromCell(particle, row, col,
                inferiorDiff.getY() < 0 ? row - 1 : superiorDiff.getY() >= 0 ? row + 1 : row,
                inferiorDiff.getX() < 0 ? col - 1 : superiorDiff.getX() >= 0 ? col + 1 : col
        );
    }
}
