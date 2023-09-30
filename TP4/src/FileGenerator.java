package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class FileGenerator {

    public void generateStaticFile(String staticFileName, double radius, int n, double mass, double L) throws IOException {
        File file = new File(staticFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
        staticWriter.printf("0.0\n");

        List<Particle> particles = new ArrayList<>();

        if (n == 25) {
            double prevX = 0;
            double spacing = L / n;
            for (int j = 0; j < n; j++) {
                double y = 0;
                Random random = new Random();
                double x = prevX + spacing;
                double vy = 0;
                double u = random.nextDouble(9,12);
                staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, u, vy, u, radius, mass);

                particles.add(new Particle(j, x, y, u, vy, u, radius, mass));
                prevX = x;
            }
        } else {

            for (int j = 0; j < n; j++) {
                boolean overlapping = true;
                double x = 0;
                double y = 0;
                Random random = new Random();
                while (overlapping) {
                    overlapping = false;
                    for (Particle particle : particles) {
                        double distance = Math.abs(x - particle.getX());
                        if (distance < 2 * radius) {
                            overlapping = true;
                            x = random.nextDouble() * L;
                            break;
                        }
                    }
                }
                double vy = 0;
                double u = random.nextDouble(9,12);
                staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, u, vy, u, radius, mass);

                particles.add(new Particle(j, x, y, u, vy, u, radius, mass));
            }
        }

        staticWriter.close();
    }

    private static FileWriter positionsFileWriter;

    public static void createFiles(String positionsFile) throws IOException {
        positionsFileWriter = new FileWriter(positionsFile);
    }

    public static void writeOutput(List<Particle> particles, double time) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(time);
        for (Particle particle : particles) {
            sb.append("\n");
            String sb_line = particle.getId() + "\t" +
                    particle.getX() + "\t" +
                    particle.getY() + "\t" +
                    particle.getVx() + "\t" +
                    particle.getVy() + "\t" +
                    particle.getU() + "\t" +
                    particle.getRadius() + "\t" +
                    particle.getM() + "\t";
            sb.append(sb_line);
        }
        positionsFileWriter.write(sb.toString());
    }

    public static void closeFiles() throws IOException {
        positionsFileWriter.close();
    }
}
