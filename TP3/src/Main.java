import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static int N;
    public static double L;
    private static final Set<Particle> particles = new HashSet<>();
    private static Set<Limit> limits = new HashSet<>();

    private static final int MAX_TIME = 60;

    private static void generateParticles(FileWriter inputFile) throws IOException {
        double weight = 1.00;
        double radius = 0.0015;
        double v = 0.01;
        double length = 0.09;
        inputFile.write("     " + N);

        double rx, ry, theta, vx, vy;

        Set<Particle> addedParticles = new HashSet<>();
        for (int i = 0; addedParticles.size() < N; i++) {
            rx = length * Math.random();
            if (rx - radius < 0)
                rx += radius;
            if (rx + radius > length)
                rx -= radius;
            ry = length * Math.random();
            if (ry - radius < 0)
                ry += radius;
            if (ry + radius > length)
                ry -= radius;
            theta = 2 * Math.PI * Math.random();
            vx = v * Math.cos(theta);
            vy = v * Math.sin(theta);
            if (addedParticles.isEmpty()){
                inputFile.write("\n   " + rx + "    " + ry + "    " + vx + "    " + vy + "    " + weight + "    " + radius);
                addedParticles.add(new Particle(i, rx, ry, vx, vy, weight, radius));
            }
            else {
                boolean added = false;
                while (!added) {
                    rx = length * Math.random();
                    if (rx - radius < 0)
                        rx += radius;
                    if (rx + radius > length)
                        rx -= radius;
                    ry = length * Math.random();
                    if (ry - radius < 0)
                        ry += radius;
                    if (ry + radius > length)
                        ry -= radius;
                    Set<Particle> arrayCopy = new HashSet<>(addedParticles);
                    for (Particle p : arrayCopy) {
                        if (Math.sqrt(Math.pow((rx - p.getxPos()), 2) + Math.pow((ry - p.getyPos()), 2)) >= 2 * radius + 0.001) {
                            added = true;
                        }
                    }
                    if (added){
                        inputFile.write("\n   " + rx + "    " + ry + "    " + vx + "    " + vy + "    " + weight + "    " + radius);
                        addedParticles.add(new Particle(i, rx, ry, vx, vy, weight, radius));
                    }
                }
            }

        }
        System.out.println(addedParticles.size());

        inputFile.close();
    }

    public static void main(String[] args) throws IOException {

        File inputFile = new File("./files/input.txt");
        File outputFile = new File("./files/output.txt");
        FileWriter inputWriter = new FileWriter(inputFile.getPath());
        FileWriter outputWriter = new FileWriter(outputFile.getPath());
        L = Double.parseDouble(args[0]);
        N = Integer.parseInt(args[1]);
        generateParticles(inputWriter);
        for (double i = 0; i < 0.09; i += 0.0005) {

            limits.add(new Limit(i, 0));
            limits.add(new Limit(i, 0.09));
            limits.add(new Limit(0, i));
            limits.add(new Limit(0.09+i, (0.09-L)/2));
            limits.add(new Limit(0.09+i, (0.09+L)/2));
            if(i > (0.09+L)/2 || i < (0.09-L)/2){
                limits.add(new Limit(0.09, i));
            }
            else{
                limits.add(new Limit(0.18, i));
            }
        }


        List<String> data;
        try {
            data = Files.readAllLines(Path.of("./files/input.txt"));
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

        Container container = new Container(L, particles);
        double timeElapsed = 0.0;
        writeToOutputFile(outputWriter, container.getParticles(), timeElapsed);
        String[] outputs = {
                "./data/output/dynamic/Dynamic_N_" + N + "_L_" + L +"_v" +"1.dump",
                "./data/output/VaN_" + N + "_L_" + L + "_v" +"1.txt",
                "./data/output/VaN_" + N + "_L_" + L +"_v" +"1.txt",
                "./data/output/VaN_" + N + "_L_" + L +"_v" +"1.txt",
        };
        DataManager dm =  new DataManager();
        int stationary = 0;

        while (timeElapsed < MAX_TIME && stationary < 50) {
            double time = container.executeCollisions(timeElapsed);
            timeElapsed += time;
            double leftPressure = container.getLeftSidePressure(timeElapsed, timeElapsed - time);
            double rightPressure = container.getRightSidePressure(timeElapsed, timeElapsed - time);
            if (rightPressure != 0 && Math.abs(leftPressure-rightPressure) < 0.0001){
                stationary++;
            }
            writeToOutputFile(outputWriter, container.getParticles(), timeElapsed);
            dm.writeDynamicFile(container.getParticles(), limits,"./files/outputDM.dump", timeElapsed);
        }
        outputWriter.close();
    }

    public static void writeToOutputFile(FileWriter outputFile, Set<Particle> particles, Double time) throws IOException {
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

        outputFile.write(stringBuilder.toString());
    }
}
