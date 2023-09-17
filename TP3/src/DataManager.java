import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataManager {
    private double max_radius = 0;
    private float L = (float) 0.03;
    private int N = 200;
    private float time;
    private Set<Particle> particles = new HashSet<>();

    public DataManager() {}

    public void writeDynamicFile(Set<Particle> particles, Set<Limit> limits, String filePath, double time) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            if (file.createNewFile()) {
                int id = 1;
                File startingFile = new File("./files/dynamic.txt");
                Scanner scanner = new Scanner(startingFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    writer.write(id + " " + line + "\n");
                    id++;
                }
                scanner.close();
                System.out.println("File created successfully");
            } else {
                StringBuilder data = new StringBuilder();
                int n = getN() + limits.size();
                data.append(n + "\n");
                data.append("Frame: " + time + '\n');
                for (Particle p : particles) {
                    data.append(p.getId() + " " + p.getxPos() + " " + p.getyPos() + " " + p.getVx() + " " + p.getVy()
                            + " " + 245
                            + " " + 128
                            + " " + 146
                            + " " + p.getRadius()
                            /* " " + p.getTheta() +*/+ "\n");
                }
                for (Limit p : limits) {
                    data.append(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy()
                            + " " + 255
                            + " " + 255
                            + " " + 255
                            + " " + p.getRadius()
                            /* " " + p.getTheta() +*/+ "\n");
                }

                writer.write(data.toString());
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public float getL() {
        return L;
    }

    public int getN() {
        return N;
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public double getMaxRadius() {
        return max_radius;
    }
}
