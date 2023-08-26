import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.lang.Math;

public class RandomParticleGenerator {

    private static List<Particle> particles = new ArrayList<>();
    private static int N;
    private static double noise;
    private static double L;
    private static double v;
    private static double radius;

    private static void generateParticles(){
        for(int i = 0; i < N; i++) {
            double theta = Math.random() * Math.PI * (Math.random() < 0.5? -1:1);
            double x = Math.random() * L;
            double y = Math.random() * L;
            Particle particle = new Particle(i+1, x, y, v, theta);
            particles.add(particle);
        }
    }

    public static Grid initGrid() {
        return new Grid(L, radius);

    }
    public static void populateGrid (Grid grid){
        for (Particle p : particles){
            grid.addParticle(p);
        }
    }

    public static double distance(Particle p1, Particle p2){
        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());
        deltaX -= deltaX > ((L*1.0f) / 2 ) ? L : 0;
        deltaY -= deltaY > ((L*1.0f) / 2) ? L : 0;

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }

    public static HashMap<Particle, List<Integer>> checkNeighbours(int M, double rc, Grid grid) {
        HashMap<Particle, List<Integer>> neighbours = new HashMap<>();
        for (int i=0; i< M; i++){
            for (int j=0; j< M ; j++){
                for(Particle particle : grid.getParticles(i, j)) {
                    if (!neighbours.containsKey(particle)) {
                        neighbours.put(particle, new ArrayList<>());
                    }
                    for (Particle n :  grid.getParticles(i, j)) {
                        if (n.getId() != particle.getId()){
                            if (distance(particle, n) <= rc) {
                                neighbours.get(particle).add(n.getId());
                            }
                        }
                    }
                    List<Particle> possibleNeighbours = new ArrayList<>();
                    possibleNeighbours.addAll(grid.getParticles(i, (j+1) % M));
                    possibleNeighbours.addAll(grid.getParticles((i+1) % M, j));
                    possibleNeighbours.addAll(grid.getParticles((i + M - 1) % M, (j+1) % M));
                    possibleNeighbours.addAll(grid.getParticles((i + 1) % M, (j+1) % M));

                    for (Particle n :  possibleNeighbours) {
                        double distance = distance(particle, n);
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

    public static void updateTheta(HashMap<Particle, List<Integer>> neighboursMap){
        for (Particle p: particles){
            double newX = p.getX() + p.getV() * Math.cos(p.getTheta());
            double newY = p.getY() + p.getV() * Math.sin(p.getTheta());

            if (newX > L)
                newX = newX - L;
            else if (newX < 0)
                newX = newX + L;
            if (newY > L)
                newY = newY - L;
            else if (newY < 0)
                newY = newY + L;

            p.setX(newX);
            p.setY(newY);

            double finalNoise = ((Math.random() * 2*noise) - noise)/2;
            double sumSin = Math.sin(p.getTheta());
            double sumCos = Math.cos(p.getTheta());
            for (Integer id : neighboursMap.get(p)){
                sumSin += Math.sin(particles.get(id - 1).getTheta());
                sumCos += Math.cos(particles.get(id - 1).getTheta());
            }
            double newTheta = Math.atan2(sumSin,sumCos);

            p.setTheta((newTheta + finalNoise) % (2*Math.PI));
        }
    }

    public static void fillFiles(FileWriter staticFile, FileWriter dynamicFile) throws IOException{
        staticFile.write("   " + N + "\n   " + L);
        dynamicFile.write("   " + 0);

        double x, y, theta;
        for (int i = 0; i < N; i++) {
            x = L * Math.random();
            y = L * Math.random();
            theta = 2 * Math.PI * Math.random();
            staticFile.write("\n   " + radius);
            dynamicFile.write(String.format("\n   %.7e   %.7e   %f   %.7e", x,y,v,theta));
        }
    }

    public static void writeFiles(FileWriter output, FileWriter va_output, int iterations) throws IOException{
        output.write(String.format("%d\n", iterations));

        double sin= 0,cos=0;
        for(Particle p: particles){
            output.write(String.format("%d\t%.7e\t%.7e\t%f\t%.7e\n", p.getId(),p.getX(), p.getY(), p.getV(), p.getTheta()));
            sin+=(Math.sin(p.getTheta()));
            cos+=(Math.cos(p.getTheta()));
        }

        double averageV = Math.sqrt(Math.pow(sin, 2) + Math.pow(cos,2)) / (N*v);
        va_output.write(String.format("%d\t%g\t\n",iterations,averageV));
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            throw new IllegalArgumentException("Four parameters are expected");
        }

        int iterations = 50;

        N = Integer.parseInt(args[0]);
        L = Double.parseDouble(args[1]);
        v = Double.parseDouble(args[2]);
        noise = Double.parseDouble(args[3]);
        radius = 1;

        FileWriter staticFile = new FileWriter("./output/static.txt");
        FileWriter dynamicFile = new FileWriter ("./output/dynamic.txt");
        fillFiles(staticFile, dynamicFile);
        staticFile.close();
        dynamicFile.close();

        generateParticles();
        Grid grid = initGrid();

        FileWriter output = new FileWriter(new File("./output/", "dynamicOutput_order5.txt"));
        FileWriter va_output = new FileWriter(new File("./output/", "va_Output_order5.txt"));
        va_output.write("Step\nVa\n");

        for (int i=0; i < iterations; i++) {
            writeFiles(output, va_output, i);
            populateGrid(grid);
            HashMap<Particle, List<Integer>> neighbours = checkNeighbours(grid.getSize(), radius, grid);
            updateTheta(neighbours);
        }

        output.close();
        va_output.close();
    }

}

