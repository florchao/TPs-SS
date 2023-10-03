package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

public class FileGenerator {

    public void generateStaticFile(String staticFileName, double particleRadius, int n, double mass, double lineLength) throws IOException {
       Random random = new Random();
        if (n >= 25) {
            File file = new File(staticFileName);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
            staticWriter.printf("0.0\n");

            List<Particle> particles = new ArrayList<>();
            double requiredSpacing = 2 * particleRadius;

            double unusedSpace = lineLength - (requiredSpacing * n);
            double spacing = unusedSpace > 0 ? unusedSpace / (n - 1) : 0;

            for (int i = 0; i < n; i++) {
                double x = i * (requiredSpacing + spacing);
                double u = random.nextDouble(9,12);

                staticWriter.printf("%f\t%f\t%f\t%f\t%f\n", x, u, u, particleRadius, mass);

                particles.add(new Particle(i, x, u, u, particleRadius, mass, 0.0, 0.0, x));
            }

            staticWriter.close();
        } else {
            File file = new File(staticFileName);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
            staticWriter.printf("0.0\n");

            List<Particle> particles = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                double x = random.nextDouble() * lineLength;
                boolean isOverlap;

                do {
                    isOverlap = false;
                    for (Particle existingParticle : particles) {
                        double distance = Math.abs(x - existingParticle.getX());
                        if (distance < 2 * particleRadius) {
                            isOverlap = true;
                            x = random.nextDouble() * lineLength;
                            break;
                        }
                    }
                } while (isOverlap);

                double u = random.nextDouble(9,12);
                staticWriter.printf("%f\t%f\t%f\t%f\t%f\n", x, u, u, particleRadius, mass);
                particles.add(new Particle(i, x, u, u, particleRadius, mass, 0.0, 0.0, x));
            }

            staticWriter.close();
            }
    }

    public void generateStaticFileForEx3(String staticFileName, double particleRadius, int n, double mass, double lineLength) throws IOException {
        Random random = new Random();
        File file = new File(staticFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
        staticWriter.printf("0.0\n");

        List<Particle> particles = new ArrayList<>();
        double requiredSpacing = 2 * particleRadius;

        double unusedSpace = lineLength - (requiredSpacing * n);
        double spacing = unusedSpace > 0 ? unusedSpace / (n - 1) : 0;
        List<Double> doubleList = Arrays.asList(
                9.0, 9.1, 9.2, 9.3, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9,
                10.0, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9,
                11.0, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 11.7, 11.8, 11.9
        );

        ArrayList<Double> u = new ArrayList<>(doubleList);

        Integer aux = Math.floorDiv(u.size(), n);
        for (int i = 0; i < n; i++) {
            double x = i * (requiredSpacing + spacing);

            staticWriter.printf("%f\t%f\t%f\t%f\t%f\n", x, u.get(i*aux), u.get(i*aux), particleRadius, mass);

            particles.add(new Particle(i, x, u.get(i*aux), u.get(i*aux), particleRadius, mass, 0.0, 0.0, x));
        }

        staticWriter.close();
    }

    public void writeOutput(String fileName, List<Particle> particles, double time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time).append("\n");
        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%d\t%f\t%f\t%f\t%f\t%f\n",
                    particle.getId(),
                    particle.getX(),
                    particle.getVx(),
                    particle.getFx(),
                    particle.getFy(),
                    particle.getR()));
        }
        outputWriter.write(stringBuilder.toString());
        outputWriter.close();
    }
}
