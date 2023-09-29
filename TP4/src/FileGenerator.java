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
            staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, vx, vy, radius, mass, angle);

            particles.add(new Particle(x, y, vx, vy, radius, mass, angle));
        }

        staticWriter.close();
    }
}
