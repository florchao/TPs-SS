package src;

import java.util.ArrayList;
import java.util.List;

public abstract class Gear {

    public static List<Double> gearP(List<Double> der, double dT){
        List<Double> newDerivatives = new ArrayList<>();
        newDerivatives.add(der.get(0) + der.get(1)*dT + der.get(2)*dT*dT/2 + der.get(3)*dT*dT*dT/6 + der.get(4)*dT*dT*dT*dT/24 + der.get(5)*dT*dT*dT*dT*dT/120);
        newDerivatives.add(der.get(1) + der.get(2)*dT + der.get(3)*dT*dT/2 + der.get(4)*dT*dT*dT/6 + der.get(5)*dT*dT*dT*dT/24);
        newDerivatives.add(der.get(2) + der.get(3)*dT + der.get(4)*dT*dT/2 + der.get(5)*dT*dT*dT/6);
        newDerivatives.add(der.get(3) + der.get(4)*dT + der.get(5)*dT*dT/2);
        newDerivatives.add(der.get(4) + der.get(5)*dT);
        newDerivatives.add(der.get(5));
        return newDerivatives;
    }

    public static List<Double> gearC(List<Double> der, double dT, double[] alpha, double dR2){
        List<Double> newDerivatives = new ArrayList<>();
        newDerivatives.add(der.get(0) + (alpha[0] * dR2 * 1));
        newDerivatives.add(der.get(1) + (alpha[1] * dR2 * 1) / (dT));
        newDerivatives.add(der.get(2) + (alpha[2] * dR2 * 2) / (dT*dT));
        newDerivatives.add(der.get(3) + (alpha[3] * dR2 * 6) / (dT*dT*dT));
        newDerivatives.add(der.get(4) + (alpha[4] * dR2 * 24) / (dT*dT*dT*dT));
        newDerivatives.add(der.get(5) + (alpha[5] * dR2 * 120) / (dT*dT*dT*dT*dT));
        return newDerivatives;
    }
}
