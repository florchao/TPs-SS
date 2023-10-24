package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Ovito {

    private static final String RESOURCES_PATH = "./output/";

    public static String createFile(String name, String extension) {
        try {
            int count = 1;
            String file_path = RESOURCES_PATH + name + count + "." + extension;
            File file = new File(file_path);
            while (!file.createNewFile()) {
                count += 1;
                file_path = RESOURCES_PATH + name + count + "." + extension;
                file = new File(RESOURCES_PATH + name + count + "." + extension);
            }
            System.out.println("File created: " + file_path);
            return file_path;
        } catch (IOException e) {
            throw new RuntimeException("Error writing random particles to file (" + name + ") in ParticleUtils.createFile.");
        }
    }

    public static void writeParticlesToFileXyz(String filePath, List<Particle> particles, List<Double> fixedX, List<Double> fixedY, String comment) {
        try {
            FileWriter myWriter = new FileWriter(filePath, true);
            myWriter.write((fixedX.size() + particles.size()) + "\n" + comment + "\n");
            for (int i=0; i< fixedX.size(); i++){
                myWriter.write(fixedX.get(i) + " " + fixedY.get(i) + " " + 0.0 + " " + 0.0 + " " + 0.0001 + " " + 0 + " " + 0 + " " + 0 + "\n");

            }
            for (Particle particle : particles)
                myWriter.write(particle.toString() + "\n");
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing particles to file (" + filePath + ") in ParticlesUtils.writeParticlesToFile.");
        }
    }

    public static void writeParticlesToFileXyz(String filePath, List<Particle> particles, List<Double> fixedX, List<Double> fixedY) {
        writeParticlesToFileXyz(filePath, particles, fixedX, fixedY, "");
    }

//    public static void writeParticlesToFileXyz(String filePath, List<Particle> particles) {
//        try {
//            FileWriter myWriter = new FileWriter(filePath, true);
//            myWriter.write(particles.size() + "\n\n");
//            for (Particle particle : particles)
//                myWriter.write(particle.toString() + "\n");
//            myWriter.close();
//        } catch (IOException e) {
//            throw new RuntimeException("Error writing particles to file (" + filePath + ") in ParticlesUtils.writeParticlesToFile.");
//        }
//    }

    public static <T> void writeListToFIle(List<T> list, String file_path, boolean end) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).toString());
                if (!end || i < list.size() - 1) {
                    sb.append("\n");
                }
            }
            FileWriter myWriter = new FileWriter(file_path, true);
            myWriter.write(sb.toString());
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static <T> void writeToFIle(T o, String file_path) {
//        try {
//            FileWriter myWriter = new FileWriter(file_path, true);
//            myWriter.write(o.toString());
//            myWriter.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


}
