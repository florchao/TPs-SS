import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Main {
    private static final int MAX_TIME = 200;
    private static final double width = 0.09;
    private static Container container;
    private static TreeSet<Particle> particles;
    private static int N;
    private static double L;

    private static Set<Limit> boundaries = new HashSet<>();

    public static void main(String[] args) {

        L = Double.parseDouble(args[0]);
        N = Integer.parseInt(args[1]);
        particles = new TreeSet<>();

        if (L > width) {
            throw new IllegalArgumentException("L must be smaller than width");
        }

        ParticleGenerator.generateFile(N);
        readParticlesFiles("./files/input.txt");
        container = new Container(L, particles);
        setBoundaries();

        double timeElapsed = 0.0;
        double stationary = 0;
        int frame = 0;
        double timeOfNextCollision = container.executeCollisions(timeElapsed);
        List<Double> pressures = new ArrayList<>();

        while (timeElapsed < MAX_TIME && timeElapsed - stationary < 25) {
            Collision next = container.getCollisions().first();
            moveParticles(timeOfNextCollision - timeElapsed);

            if (next.getType() != CollisionType.PARTICLE) {
                double collisionV;
                if (next.getType() == CollisionType.LEFT_HORIZONTAL_WALL || next.getType() == CollisionType.RIGHT_HORIZONTAL_WALL)
                    collisionV = next.getP1().getVy();
                else
                    collisionV = next.getP1().getVx();
                container.addPressure(collisionV, next.getType(), timeOfNextCollision);
            }

            double leftPressure = container.getLeftSidePressure(timeOfNextCollision, 0);
            double rightPressure = container.getRightSidePressure(timeOfNextCollision, 0);
            if (rightPressure != 0 && Math.abs(leftPressure - rightPressure) < 0.025) {
                pressures.add(leftPressure);
            } else if (next.getType() != CollisionType.PARTICLE) {
                stationary = timeElapsed;
            }

            next.collide(container.getWidth(), container.getL());
            if (frame % 25 == 0) {
                writeDynamicFile(particles, boundaries, "./files/output/simulation" + N + "L" + L +".dump", timeElapsed);
            }
            writePressureFile(leftPressure, rightPressure, timeElapsed, "./files/output/pressures" + N + "L" + L+".txt");

            container.getCollisions().removeIf(c -> c.getP1().equals(next.getP1()) ||
                    (c.getP2() != null && c.getP2().equals(next.getP1())) ||
                    c.getP1().equals(next.getP2()) ||
                    (c.getP2() != null && c.getP2().equals(next.getP2()))
                            &&
                            (
                                    !c.getP1().equals(container.getUpperCorner()) &&
                                            !c.getP2().equals(container.getUpperCorner()) &&
                                            !c.getP1().equals(container.getLowerCorner()) &&
                                            !c.getP2().equals(container.getLowerCorner())
                            ));

            timeElapsed = timeOfNextCollision;
            if (next.getP1() != container.getUpperCorner() && next.getP1() != container.getLowerCorner())
                container.calculateCollisions(next.getP1(), timeElapsed);
            if (next.getP2() != null && next.getP2() != container.getUpperCorner() && next.getP2() != container.getLowerCorner()) {
                container.calculateCollisions(next.getP2(), timeElapsed);
            }
            timeOfNextCollision = container.getCollisions().first().getTime();
            frame++;
        }
        OptionalDouble average = pressures
                .stream()
                .mapToDouble(a -> a)
                .average();
        double pre = average.isPresent() ? average.getAsDouble() : 0;
        writeFinalPressure(pre, "./files/output/finalPressure" + N + "L" + L +".txt");

    }

    private static void setBoundaries(){
        for (double i = 0; i < 0.09; i += 0.0005) {

            boundaries.add(new Limit(i, 0));
            boundaries.add(new Limit(i, 0.09));
            boundaries.add(new Limit(0, i));
            boundaries.add(new Limit(0.09+i, (0.09-L)/2));
            boundaries.add(new Limit(0.09+i, (0.09+L)/2));
            if(i > (0.09+L)/2 || i < (0.09-L)/2){
                boundaries.add(new Limit(0.09, i));
            }
            else{
                boundaries.add(new Limit(0.18, i));
            }
        }
    }

    public static void moveParticles(double time){
        particles.forEach(p -> {
            p.updatePosition(time);
        });
    }

    private static void readParticlesFiles(String dynamicPath) {
        try {
            File dynamicFile = new File(dynamicPath);

            Scanner dynamicScanner = new Scanner(dynamicFile);
            dynamicScanner.nextInt();
            dynamicScanner.nextInt();

            while (dynamicScanner.hasNext()) {
                int id = dynamicScanner.nextInt();
                float x = dynamicScanner.nextFloat();
                float y = dynamicScanner.nextFloat();
                double vx = dynamicScanner.nextDouble();
                double vy = dynamicScanner.nextDouble();
                double radius = dynamicScanner.nextFloat();
                double mass = dynamicScanner.nextFloat();

                Particle p = new Particle(radius, vx, vy, x, y, mass);
                particles.add(p);
            }
            dynamicScanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

     public static void writePressureFile(double left, double right, double time, String filePath){
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            StringBuilder data = new StringBuilder();
            data.append(time + " " + left + " " + right + "\n");
            writer.write(data.toString());
            writer.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFinalPressure(double pressure, String filePath){
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            StringBuilder data = new StringBuilder();
            data.append(pressure + "\n");
            writer.write(data.toString());
            writer.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeDynamicFile(Set<Particle> particles, Set<Limit> limits, String filePath, double time) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file, true);
            if (file.createNewFile()) {
                int id = 1;
                File startingFile = new File("./files/input.txt");
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
                int n = N + limits.size();
                data.append(n + "\n");
                data.append("Frame: " + time + '\n');
                for (Particle p : particles) {
                    data.append(p.getId() + " " + p.getXpos() + " " + p.getYpos() + " " + p.getVx() + " " + p.getVy()
                            + " " + 255
                            + " " + 0
                            + " " + 255
                            + " " + p.getRadius()
                            + "\n");
                }
                for (Limit p : limits) {
                    data.append(p.getId() + " " + p.getXpos() + " " + p.getYpos() + " " + p.getVx() + " " + p.getVy()
                            + " " + 255
                            + " " + 255
                            + " " + 255
                            + " " + p.getRadius()
                            + "\n");
                }

                writer.write(data.toString());
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
