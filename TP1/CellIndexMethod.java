import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CellIndexMethod {

    private static List<Particle> particles = new ArrayList<>();

    public static double distance(Particle p1, Particle p2, double L, boolean roundNeighbours){
        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());

        if(roundNeighbours) {
            deltaX -= deltaX > ((L*1.0f) / 2 ) ? L : 0;
            deltaY -= deltaY > ((L*1.0f) / 2) ? L : 0;
        }

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2)) - p1.getRadius() - p2.getRadius();
    }

    public static Grid populateGrid (List<Particle> particles, int L, int M){
        Grid populatedGrid = new Grid(M);

        for (Particle p : particles){
            populatedGrid.addParticle(p);
        }
        return populatedGrid;
    }

    public static HashMap<Particle, List<Integer>> checkNeighbours(int M, int L, double rc, Grid grid, boolean roundNeighbours) {
        HashMap<Particle, List<Integer>> neighbours = new HashMap<>();
        for (int i=0; i< M; i++){
            for (int j=0; j< M ; j++){
                    for(Particle particle : grid.getParticles(i, j)) {
                        if (!neighbours.containsKey(particle)) {
                            neighbours.put(particle, new ArrayList<>());
                        }
                        List<Particle> possibleNeighbours = new ArrayList<>();
                        if (roundNeighbours) {
                            if (i < M-1) {
                                possibleNeighbours.addAll(grid.getParticles(i+1, j));
                            }
                            if (j < M-1) {
                                possibleNeighbours.addAll(grid.getParticles(i, j+1));
                                if (i > 0) {
                                    possibleNeighbours.addAll(grid.getParticles(i-1, j+1));
                                }
                                if (i < M-1) {
                                    possibleNeighbours.addAll(grid.getParticles(i+1, j+1));
                                }
                            }
                        } else {
                            possibleNeighbours.addAll(grid.getParticles(i, (j+1) % M));
                            possibleNeighbours.addAll(grid.getParticles((i+1) % M, j));
                            possibleNeighbours.addAll(grid.getParticles((i + M - 1) % M, (j+1) % M));
                            possibleNeighbours.addAll(grid.getParticles((i + 1) % M, (j+1) % M));
                        }

                        for (Particle n :  grid.getParticles(i, j)) {
                            if (n.getId() != particle.getId()){
                                if (distance(particle, n, L ,roundNeighbours) <= rc) {
                                    neighbours.get(particle).add(n.getId());
                                }
                            }
                        }

                        for (Particle n :  possibleNeighbours) {
                            double distance = distance(particle, n, L ,roundNeighbours);
                            if (distance <= rc) {
                                neighbours.get(particle).add(n.getId());
                                if (!neighbours.containsKey(n)) {
                                    neighbours.put(n, new ArrayList<>());
                                }
                                neighbours.get(n).add(particle.getId());
                            }
                        }
                    }
            }
        }
        return neighbours;
    }


    public static void generateOutput(HashMap<Particle, List<Integer>> neighbours, long startTime) throws IOException {
        try {
            File file = new File("./outputNeighbours.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred creating file.");
            e.printStackTrace();
        }
        FileWriter writer = new FileWriter("./outputNeighbours.txt");

        StringBuilder s = new StringBuilder();
        for (Particle particle : neighbours.keySet()) {
            s.append(particle.getId()).append(";");
            s.append(particle.getRadius()).append(";");
            s.append(particle.getX()).append(";");
            s.append(particle.getY()).append(";");
            for (int neighbourId : neighbours.get(particle)) {
                s.append(neighbourId).append(",");
            }
            s.append("\n");
        }
        writer.write(s.toString());
        writer.close();

        long endTime = System.currentTimeMillis();
        System.out.printf("Time: %d ms\n", endTime - startTime);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        if(args.length != 5){
            throw new IllegalArgumentException("Invalid parameters");
        }

        File staticFile = new File(args[0]);
        File dynamicFile = new File (args[1]);
        int M = Integer.parseInt(args[2]);
        int rc = Integer.parseInt(args[3]);
        boolean roundNeighbours = Boolean.parseBoolean(args[4]);

        List<String> data;
        try {
            data = Files.readAllLines(Path.of(staticFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read lines");
        }
        int N = Integer.parseInt(data.get(0).split("    ")[1]);
        int L = Integer.parseInt(data.get(0).split("    ")[1]);

        if(L/M <= rc){
            throw new RuntimeException("Grid radius must be greater than r_c.");
        }

        for (int i = 2; i < data.size(); i++){
            String[] line = data.get(i).split("    ");
            particles.add(new Particle(i-1, Double.parseDouble(line[1]), Double.parseDouble(line[2]), 0, 0));
        }

        List<String> dataDynamic;
        try {
            dataDynamic = Files.readAllLines(Path.of(dynamicFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read lines");
        }

        for (int i = 1; i < dataDynamic.size(); i++){
            Particle particle = particles.get(i-1);
            String[] line = dataDynamic.get(i).split("   ");
            particle.setX(Double.parseDouble(line[1]));
            particle.setY(Double.parseDouble(line[2]));
        }

        if (particles.size() != N) {
            System.out.println("Error reading file data");
        }

        Grid grid = populateGrid(particles, L, M);
        HashMap<Particle, List<Integer>> neighbours = checkNeighbours(M, L, rc, grid, roundNeighbours);
        try {
            generateOutput(neighbours, startTime);
        } catch (IOException e) {
            throw new RuntimeException("Error trying to generate output");
        }
    }
}