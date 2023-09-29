import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotLinkException;
import java.util.HashSet;
import java.util.Set;

public class ParticleGenerator {
    private static final double DOMAIN_LENGTH = 0.09;
    private static final double radius = 0.0015;
    private static final double v = 0.01;
    private static final int weight = 1;

    public static void generateFile(int N) {
        try {
            FileWriter dynamicFileWriter = new FileWriter("./files/input.txt");
            Set<String> positions = new HashSet<>();
            String position;

            for (int i = 0; i < N; i++) {
                while (true) {
                    double x = Math.random() * (DOMAIN_LENGTH - 2 * radius) + radius;
                    double y = Math.random() * (DOMAIN_LENGTH - 2 * radius) + radius;
                    position = String.format("%.4f %.4f", x, y);

                    boolean validPosition = positions.stream().allMatch(p ->
                            Math.sqrt(Math.pow((x - Double.parseDouble(p.split(" ")[0])), 2) +
                                    Math.pow((y - Double.parseDouble(p.split(" ")[1])), 2)) >= 2 * radius);

                    if (validPosition) {
                        break;
                    }
                }
                positions.add(position);
            }

            dynamicFileWriter.write(N + "\n");
            dynamicFileWriter.write(N + 4 * 900 + "\n");
            int id = 1;
            for (String pos : positions) {
                double x = Double.parseDouble(pos.split(" ")[0]);
                double y = Double.parseDouble(pos.split(" ")[1]);
                double theta = Math.random() * 2 * Math.PI;
                double vx = v * Math.cos(theta);
                double vy = v * Math.sin(theta);
                dynamicFileWriter.write(id + " " + x + "   " + y + "   " + vx + "  " + vy + "  " + radius + "  " + weight +"\n");
                id++;
            }
            dynamicFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
