package src;

public class MainEx1 {
    public static void main(String[] args) {
        double[] dT = {0.005, 0.001, 0.0005, 0.0001, 0.00005, 0.00001};
        String[] methods = {"Beeman", "Verlet", "Gear"};
        for (String method : methods) {
            for (int j = 0; j < dT.length; j++) {
                String[] aux = {String.valueOf(dT[j]), method, String.format("%s_%d.txt", method, j + 1)};
                Oscilator.main(aux);
            }
        }
    }
}
