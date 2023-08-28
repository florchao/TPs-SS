import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

public class OffLatice {
    private static double radius;
    private static double N;
    private static double L;
    private static double noise;

    private static void generateParticles(FileWriter staticFile, FileWriter dynamicFile) throws IOException {
        double v = 0.03;
        radius = 1;
        staticFile.write("   " + N + "\n   " + L);
        dynamicFile.write("   " + 0);

        double x, y, theta;
        for (int i = 0; i < N; i++) {
            x = L * Math.random();
            y = L * Math.random();
            theta = 2 * Math.PI * Math.random();
            staticFile.write("\n   " + radius);
            dynamicFile.write(String.format("\n   %.7e   %.7e   %f   %.7e", x,y,v,theta));
        }
        staticFile.close();
        dynamicFile.close();
    }

    public static void getDynamicData(Scanner reader, List<Particle> particles) {
        List<Double> props = new ArrayList<>();
        int counter = 1;
        reader.nextLine();

        while (reader.hasNextLine()) {
            String data = reader.nextLine().trim().replaceAll("\\s+", " ");
            StringTokenizer tokenizer = new StringTokenizer(data);
            while (tokenizer.hasMoreElements()) {
                props.add(Double.parseDouble(tokenizer.nextToken()));
            }
            if (props.size() == 4) {
                particles.get(counter - 1).setX(props.get(0));
                particles.get(counter - 1).setY(props.get(1));
                particles.get(counter - 1).setV(props.get(2));
                particles.get(counter - 1).setTheta(props.get(3));
                props.clear();
                counter++;
            } else {
                System.out.printf("Invalid dynamic properties for particle %d\n", counter);
                exit(-1);
            }
        }
    }

    public static void getStaticData(Scanner reader, List<Particle> particles) {
        int counter = 1;

        while (reader.hasNextLine()) {
            reader.nextLine().trim().replaceAll("\\s+", " ");
            particles.add(new Particle(counter++));
        }
    }

    private static void writeFileForOutput(List<Particle> particles, int step, int N, FileWriter output, FileWriter va_output) throws IOException {

        output.write(String.format("%d\n", step));

        double sin= 0, cos=0;
        for(Particle particle: particles){
            output.write(String.format("%d\t%.7e\t%.7e\t%f\t%.7e\n", particle.getId(),particle.getX(), particle.getY(), particle.getV(), particle.getTheta()));
            sin+=(Math.sin(particle.getTheta()));
            cos+=(Math.cos(particle.getTheta()));
        }

        double va = Math.sqrt(Math.pow(sin,2) + Math.pow(cos,2)) / N;
        va_output.write(String.format("%d\t%g\t\n",step,va));
    }

    public static void updateTheta(List<Particle> particles, HashMap<Integer, List<Integer>> neighboursMap){
        for (Particle p: particles){
            double newX = p.getX() + p.getV() * Math.cos(p.getTheta());
            double newY = p.getY() + p.getV() * Math.sin(p.getTheta());

            if (newX > L)
                newX = newX - L;
            else if (newX < 0)
                newX = newX + L;
            if (newY > L)
                newY = newY - L;
            else if (newY < 0)
                newY = newY + L;

            p.setX(newX);
            p.setY(newY);

            double finalNoise = ((Math.random() * 2*noise) - noise)/2;
            double sumSin = Math.sin(p.getTheta());
            double sumCos = Math.cos(p.getTheta());
            for (Integer id : neighboursMap.get(p.getId())){
                sumSin += Math.sin(particles.get(id - 1).getTheta());
                sumCos += Math.cos(particles.get(id - 1).getTheta());
            }
            double newTheta = Math.atan2(sumSin,sumCos);

            p.setTheta((newTheta + finalNoise) % (2*Math.PI));
        }
    }
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        if (args.length != 3) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        FileWriter staticWriter = new FileWriter("./output/static.txt");
        FileWriter dynamicWriter = new FileWriter("./output/dynamic.txt");
        N = Integer.parseInt(args[0]);
        L = Double.parseDouble(args[1]);
        noise  = Double.parseDouble(args[2]);
        double iterations = 500;

        File staticFile = new File("./output/static.txt");
        Scanner myStaticReader = new Scanner(staticFile);
        File dynamicFile = new File("./output/dynamic.txt");
        Scanner myDynamicReader = new Scanner(dynamicFile);

        generateParticles(staticWriter, dynamicWriter);

        List<Particle> particles = new ArrayList<>();
        getStaticData(myStaticReader, particles);
        getDynamicData(myDynamicReader, particles);


        FileWriter output = new FileWriter(new File("./output/", "dynamicOutput_order5.txt"));
        FileWriter va_output = new FileWriter(new File("./output/", "va_Output_order5.txt"));
        va_output.write("Step\tVa\n");

        Grid grid = new Grid(L, radius);

        for (int i = 0; i <= iterations; i++) {
            writeFileForOutput(new ArrayList<>(particles), i, particles.size(), output, va_output);

            grid.fill(particles);
            HashMap<Integer, List<Integer>> neighbours = grid.getAllNeighbours();

            updateTheta(particles, neighbours);
        }

        output.close();
        va_output.close();

        System.out.printf("Time consumed: %d ms\n", System.currentTimeMillis() - startTime);
    }
}