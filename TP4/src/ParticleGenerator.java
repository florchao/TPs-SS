package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleGenerator {
    public static List<Particle> generateParticles(String staticFileName) {
        List<Particle> particles = new ArrayList<>();
        Random random = new Random();
        try (BufferedReader reader = new BufferedReader(new FileReader(staticFileName))) {
            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] input = line.split("\t");
                double[] values = new double[input.length];
                for (int i = 0; i < input.length; i++) {
                    values[i] = Double.parseDouble(input[i]);
                }

                double u = random.nextDouble(9, 12);
                particles.add(new Particle(values[0], values[1], values[2], values[3], u,values[4], values[5], values[6]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return particles;

    }
}

