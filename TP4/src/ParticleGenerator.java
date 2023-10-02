package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParticleGenerator {
    public static Parameters generateParticles(String staticFileName) throws IOException {
        Parameters parameters = new Parameters();
        parameters.setParticles(new ArrayList<>());

        try (BufferedReader reader = new BufferedReader(new FileReader(staticFileName))) {
            String line;
            reader.readLine();
            int j = 0;
            while ((line = reader.readLine()) != null) {
                j++;
                String[] values = line.split("\t");
                double[] aux = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    aux[i] = Double.parseDouble(values[i]);
                }
                parameters.addParticle(new Particle(j, aux[0], aux[1], aux[2], aux[3], aux[4], 0.0, 0.0, aux[1]));
            }
        }
        return parameters;
    }
}

