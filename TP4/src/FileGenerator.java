package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class FileGenerator {

    public void generateStaticFile(String staticFileName, double radius, int n, double mass, double v, double circleR) throws IOException {
        File file = new File(staticFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
        staticWriter.printf("0.0\n");

        List<Particle> particles = new ArrayList<>();

        if (n == 25) {
            for (int j = 0; j < n; j++) {
                boolean overlapping = true;
                double x = 0;
                double y = 0;
                double angle = 0;
                Random random = new Random();
                while (overlapping) {
                    overlapping = false;
                    angle = 2.0 * Math.PI * j / n;
                    x = circleR * Math.cos(angle);
                    y = circleR * Math.sin(angle);

                    for (Particle particle : particles) {
                        double distance = Math.sqrt(Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2));
                        if (distance < 2 * radius) {
                            overlapping = true;
                            break;
                        }
                    }
                }
                angle = random.nextDouble() * 2 * Math.PI;
                double vx = v * Math.cos(angle);
                double vy = v * Math.sin(angle);
                double aux = Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2));
                double w = aux/circleR;
                staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, vx, vy, radius, mass, angle);

                particles.add(new Particle(x, y, w, radius, mass, angle));
            }
        } else {

            for (int j = 0; j < n; j++) {
                boolean overlapping = true;
                double x = 0;
                double y = 0;
                double angle = 0;
                Random random = new Random();
                while (overlapping) {
                    overlapping = false;
                    angle = random.nextDouble() * 2 * Math.PI;
                    x = circleR * Math.cos(angle);
                    y = circleR * Math.sin(angle);

                    for (Particle particle : particles) {
                        double distance = Math.sqrt(Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2));
                        if (distance < 2 * radius) {
                            overlapping = true;
                            break;
                        }
                    }
                }
                angle = random.nextDouble() * 2 * Math.PI;
                double vx = v * Math.cos(angle);
                double vy = v * Math.sin(angle);
                double aux = Math.sqrt((Math.pow(vx, 2) + Math.pow(vy,2)));
                double w = aux/circleR;
                double u = random.nextDouble(9, 12);
                staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, vx, vy, u, radius, mass, angle);

                particles.add(new Particle(x, y, w, radius, mass, angle));
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
