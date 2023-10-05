package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainEx2 {
    private static final double r = 2.25;
    private static final double k = 2500;
    private static final double DT_MAX = 0.1;
    private static final double L = 135;
    private static final double mass = 25;
    private static final double tf = 180.01;
    private static List<Particle> particles;
    private static double dt = 0;
    public static void main(String[] args) throws IOException {

        int N = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        String outputFile = "output/positionN" + N;

        dt = Math.pow(10, -k);
        generateStaticFile("input/StaticN" + N + ".txt", r, N, mass, L);
        Parameters parameters = generateParticles("input/StaticN" + N + ".txt");
        writeOutput(outputFile + "K" + k + ".txt", parameters.getParticles(), 0.0);

        createCollision(parameters.getParticles(), dt);

        double t = dt;
        double totalTime = 0;
        int itPerFrame = (int) Math.ceil(DT_MAX / dt);
        int frames = 0;
        while (t <= tf) {
            frames++;
            setParticles(gear());
            totalTime+=dt;
            if (frames == itPerFrame) {
                writeOutput(outputFile + "K" + k + ".txt", getParticles(), totalTime);
                frames = 0;
            }
            t += dt;
        }
    }

    private static List<Particle> gear() {
        List<Particle> aux = new ArrayList<>();
        for(Particle p1 : particles) {
            Particle particle = new Particle(p1.getId(), p1.getX(), p1.getVx(), p1.getU(), p1.getR(), p1.getM(), p1.getFx(), p1.getFy(), p1.getX2(), p1.getX3(), p1.getX4(), p1.getX5(), p1.getRealX());

            double[] predictionPositionX = gearPredictor(particle.getX()%L, p1.getVx(), p1.getX2(), p1.getX3(), p1.getX4(), p1.getX5(), particle.getRealX());

            particle.setX(predictionPositionX[0]%L);
            particle.setVx(predictionPositionX[1]);
            particle.setRealX(predictionPositionX[6]);

            double deltaA = movementEquation(particle, particles) - predictionPositionX[2];
            double deltaR2 = deltaA * Math.pow(dt, 2) / 2;

            double[] alpha = {3/16.0, 251/360.0, 1, 11/18.0, 1/6.0, 1/60.0};
            particle.setX((predictionPositionX[0] + alpha[0] * deltaR2) % L);
            particle.setRealX((predictionPositionX[6] + alpha[0] * deltaR2));
            particle.setVx(predictionPositionX[1] + alpha[1] * deltaR2 / dt);
            particle.setX2(predictionPositionX[2] + alpha[2] * deltaR2 * 2 / Math.pow(dt, 2));
            particle.setX3(predictionPositionX[3] + alpha[3] * deltaR2 * 6 / Math.pow(dt, 3));
            particle.setX4(predictionPositionX[4] + alpha[4] * deltaR2 * 24 / Math.pow(dt, 4));
            particle.setX5(predictionPositionX[5] + alpha[5] * deltaR2 * 120 / Math.pow(dt, 5));

            aux.add(particle);
        }
        return aux;
    }

    private static double collisionForce(Particle p1, Particle p2) {
        return k * (Math.abs(p1.getX()-p2.getX()) - (2*p1.getR())) * (Math.signum(p1.getX()-p2.getX()));
    }

    private static double propulsionForce(Particle p) {
        return (p.getU() - p.getVx());
    }
    private static double movementEquation(Particle p, List<Particle> particles) {
        double sum = 0.0;
        for(Particle particle: particles) {
            if(!particle.equals(p) && p.collides(particle, dt)) {
                sum += collisionForce(particle, p);
            }
        }
        return (propulsionForce(p) + sum) / p.getM();
    }

    private static double[] gearPredictor(double r, double r1, double r2, double r3, double r4, double r5, double rNoPeriodic) {
        double rp = r + r1 * dt + r2 * Math.pow(dt, 2) / 2 + r3 * Math.pow(dt, 3) / 6 + r4 * Math.pow(dt, 4) / 24 + r5 * Math.pow(dt, 5) / 120;
        double rpNoPeriodic = rNoPeriodic + r1 * dt + r2 * Math.pow(dt, 2) / 2 + r3 * Math.pow(dt, 3) / 6 + r4 * Math.pow(dt, 4) / 24 + r5 * Math.pow(dt, 5) / 120;
        double r1p = r1 + r2 * dt + r3 * Math.pow(dt, 2) / 2 + r4 * Math.pow(dt, 3) /6 + r5 * Math.pow(dt, 4) / 24;
        double r2p = r2 + r3 * dt + r4 * Math.pow(dt, 2) / 2 + r5 * Math.pow(dt, 3) / 6;
        double r3p = r3 + r4 * dt + r5 * Math.pow(dt, 2) / 2;
        double r4p = r4 + r5 * dt;
        return new double[]{rp % L, r1p, r2p, r3p, r4p, r5, rpNoPeriodic};

    }

    public static void setParticles(List<Particle> particles) {
        MainEx2.particles = particles;
    }

    public static void setDt(double dt) {
        MainEx2.dt = dt;
    }

    public static List<Particle> getParticles() {
        return particles;
    }
    private static void createCollision(List<Particle> particles, double dt){
        setParticles(particles);
        setDt(dt);
    }

    public static void generateStaticFile(String staticFileName, double particleRadius, int n, double mass, double lineLength) throws IOException {
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

    public static void writeOutput(String fileName, List<Particle> particles, double time) throws IOException {
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
