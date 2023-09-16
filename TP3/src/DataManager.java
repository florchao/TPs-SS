import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataManager {
    private double max_radius = 0;
    private float L;
    private int N;
    private float time;
    private Set<Particle> particles = new HashSet<>();

    private Set<Limit> limits = new HashSet<>();

    public DataManager() {
        for (double i = 0; i < 0.09; i += 0.0005) {
            this.limits.add(new Limit(i, 0));
            this.limits.add(new Limit(i, 0.09));
            this.limits.add(new Limit(0, i));
            this.limits.add(new Limit(0.09+i, (0.09-L)/2));
            this.limits.add(new Limit(0.09+i, (0.09+L)/2));
            if(i > (0.09+L)/2 || i < (0.09-L)/2){
                this.limits.add(new Limit(0.09, i));
            }
            else{
                this.limits.add(new Limit(0.18, i));
            }
        }
    }

    public void writeDynamicFile(Set<Particle> particles, String filePath, double time) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            if (file.createNewFile()) {
                int id = 1;
                File startingFile = new File("../files/input.txt");
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
                int n = particles.size();
                data.append(n + "\n");
                data.append("Frame: " + time + '\n');
                for (Particle p : particles) {
                    //data.append(time + "\n");
                    int green = p.getId()==77?255:0;
                    int blue = p.getId()==132?0:255;
                    data.append((p.getId()+1) + " " + p.getxPos() + " " + p.getyPos() + " " + p.getVx() + " " + p.getVy()
                    + " " + green
                    + " " + blue
                    + " " + 255
                    + " " + p.getRadius() 
                    /* " " + p.getTheta() +*/+ "\n");
                }
                /*
                for (Limit p : limits) {
                    data.append(p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getVx() + " " + p.getVy()
                    + " " + 255
                    + " " + 255
                    + " " + 255
                    + " " + p.getRadius() );
                    //+ " " + p.getTheta() + "\n");
                }
            */

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
