import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static int N;
    public static double L;
    private static Set<Particle> particles = new HashSet<>();

    public static void main(String[] args) {

        File inputFile = new File(args[0]);

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
