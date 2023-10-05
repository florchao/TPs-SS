package src;

import java.util.ArrayList;
import java.util.List;

public class Algorithms {

    public static double beemanX(double x, double v, double a, double aBefore, double dT) {
        return x + v * dT + (2f/3)*a*dT*dT-(1f/6)*aBefore*dT*dT;
    }

    public static double beemanVelocityC(double v,double aBefore, double a, double aAfter, double dT ) {
        return v + (1f/3)*aAfter*dT + (5f/6)*a*dT - (1f/6)*aBefore*dT;
    }

    public static double beemanVelocityP(double v, double a, double aBefore, double dT) {
        return v + (3f/2)*a*dT - (1f/2)*aBefore*dT;
    }

    public static double eulerX(double x, double v, double dT, double f, double mass){
        return x + v * dT + dT*dT * (f/(2*mass));
    }

    public static double eulerVelocity(double v, double f, double m, double dT){
        return v + dT * f / m;
    }

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

    public static double verletX(double x, double xBefore,double f, double mass, double dT){
        return (2 * x) - xBefore + ((dT*dT) * (f/mass));
    }

    public static double verletVelocity(double xAfter, double xBefore, double dT){
        return (xAfter - xBefore) / (2 * dT);
    }
}
