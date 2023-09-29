package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class FileGenerator {

    private void generatePositions(double x, double y, double circleR){
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        x = circleR * Math.cos(angle);
        y = circleR * Math.sin(angle);
    }

    private void generateV(double vx, double vy, double v){
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        vx = v * Math.cos(angle);
        vy = v * Math.sin(angle);
    }

    public void generateStaticFile(String staticFileName, double radius, int n, double mass, double v, double circleR) throws IOException {
        File file = new File(staticFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
        staticWriter.printf("0.0\n");

        List<Particle> particles = new ArrayList<>();

        for (int j = 0; j < n; j++) {
            boolean overlapping = true;
            double x = 0;
            double y = 0;
            while (overlapping) {
                overlapping = false;
                generatePositions(x, y, circleR);

                for (Particle particle : particles) {
                    double distance = Math.sqrt(Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2));
                    if (distance < 2 * radius) {
                        overlapping = true;
                        break;
                    }
                }
            }
            double vx = 0;
            double vy = 0;
            generateV(vx, vy, v);

            staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\n", x, y, vx, vy, radius, mass);

            particles.add(new Particle(x, y, vx, vy, radius, mass));
        }

        staticWriter.close();
    }
}
