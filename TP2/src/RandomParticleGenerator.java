import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.lang.Math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RandomParticleGenerator {

    private static List<Particle> particles = new ArrayList<>();
    private static int N;
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

    private static double calculateTotalVelocity(){
        double totalVx=0;
        double totalVy=0;
        for (Particle p : particles){
            totalVx += (p.getV()*cos(p.getTheta()));
            totalVy += (p.getV()*sin(p.getTheta()));
        }
        return Math.sqrt(Math.pow(totalVx, 2) + Math.pow(totalVy,2));
    }

    public static Grid populateGrid (){
        Grid populatedGrid = new Grid(L, radius);

        for (Particle p : particles){
            populatedGrid.addParticle(p);
        }
        return populatedGrid;
    }

    public static double distance(Particle p1, Particle p2){
        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());
        deltaX -= deltaX > ((L*1.0f) / 2 ) ? L : 0;
        deltaY -= deltaY > ((L*1.0f) / 2) ? L : 0;

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }

    public static HashMap<Particle, List<Integer>> checkNeighbours(int M, int L, double rc, Grid grid, boolean roundNeighbours) {
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


    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Three parameters are expected");
        }

        int iterations = 10;

        N = Integer.parseInt(args[0]);
        L = Double.parseDouble(args[1]);
        v = Double.parseDouble(args[2]);
        radius = 1;

        FileWriter staticFile = new FileWriter("./output/static.txt");
        FileWriter dynamicFile = new FileWriter ("./output/dynamic.txt");

        //todo VER QUE NECESITAMOS PARA EL DYNAMIC Y EL STATIC
        generateParticles();
        double averageV = calculateTotalVelocity() / (N*v);
        Grid grid = populateGrid();

        FileWriter output = new FileWriter(new File("../output/", "dynamicOutput.txt"));
        FileWriter va_output = new FileWriter(new File("../output/", "va_Output.txt"));

        for (int i = 0; i < iterations; i++) {
            output.write(String.format("%d\n",i));
            for(Particle p : particles){
                output.write(String.format("%d %f %f %f %f %f %f %f\n",
                        p.getId(),
                        p.getX(),
                        p.getY(),
                        0*1.0,
                        p.getX()*cos(p.getTheta()),
                        p.getY()*sin(p.getTheta()),
                        p.getTheta(),
                        radius));
            }
        }

        staticFile.close();
        dynamicFile.close();

    }

}

