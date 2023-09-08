import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static int N;
    public static double L;
    private static Set<Particle> particles = new HashSet<>();

    private static void generateParticles(FileWriter inputFile) throws IOException {
        double weight = 1.00;
        double radius = 0.0015;
        double v = 0.01;
        double length = 0.09;
        inputFile.write("   " + N);

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
        FileWriter inputWriter = new FileWriter(inputFile.getPath());
        L = Integer.parseInt(args[0]);

        generateParticles(staticWriter);

        List<String> data;
        try {
            data = Files.readAllLines(Path.of(inputFile.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Error trying to read lines");
        }
        N = Integer.parseInt(data.get(0).split("    ")[0]);

        for (int i = 1; i < data.size(); i++){
            String[] line = data.get(i).split("    ");
            particles.add(new Particle(i-1, Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]), Double.parseDouble(line[4]), Double.parseDouble(line[5]), Double.parseDouble(line[6])));
        }

        if (particles.size() != N) {
            System.out.println("Error reading file data");
        }

    }

}
