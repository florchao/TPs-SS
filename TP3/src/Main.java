import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static int N;
    public static double L;
    private static final Set<Particle> particles = new HashSet<>();

    private static final int MAX_TIME = 500000;

    private static void generateParticles(FileWriter inputFile) throws IOException {
        double weight = 1.00;
        double radius = 0.0015;
        double v = 0.01;
        double length = 0.09;
        inputFile.write("     " + N);

        double rx, ry, theta, vx, vy;
        for (int i = 0; i < N; i++) {
            rx = length * Math.random();
            ry = length * Math.random();
            theta = 2 * Math.PI * Math.random();
            vx = v * Math.cos(theta);
            vy = v * Math.sin(theta);
            inputFile.write("\n   " + rx + "    " + ry + "    " + vx + "    " + vy + "    " + weight + "    " + radius);
        }
        inputFile.close();
    }

    public static void main(String[] args) throws IOException {

        File inputFile = new File("./files/input.txt");
        File outputFile = new File("./files/output.txt");
        FileWriter inputWriter = new FileWriter(inputFile.getPath());
        L = Double.parseDouble(args[0]);
        N = Integer.parseInt(args[1]);
        generateParticles(inputWriter);


        List<String> data;
        try {
            data = Files.readAllLines(Path.of(inputFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read lines");
        }
        N = Integer.parseInt(data.get(0).split("     ")[1]);

        for (int i = 1; i < data.size(); i++){
            String[] line = data.get(i).split("    ");
            particles.add(new Particle(i-1, Double.parseDouble(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]), Double.parseDouble(line[4]), Double.parseDouble(line[5])));
        }

        if (particles.size() != N) {
            System.out.println("Error reading file data");
        }

        double timeElapsed = 0;
        Container container = new Container(L, particles);
        generateOutputFile(outputFile, container.getParticles(), 0.0);

        for (double i = 0; i < MAX_TIME; i++) {
            double time = container.executeCollisions();
            timeElapsed += time;
            generateOutputFile(outputFile, container.getParticles(), timeElapsed);
        }
    }

    public static void generateOutputFile(File outputFile, Set<Particle> particles, double time) throws IOException {
        FileWriter outputWriter = new FileWriter(outputFile.getPath());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time).append("\n");
        for (Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US, "%f\t%f\t%f\t%f\t%f\t%d\n",
                    particle.getxPos(),
                    particle.getyPos(),
                    particle.getVx(),
                    particle.getVy(),
                    particle.getRadius(),
                    particle.getId()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();
    }
}
