package src;

public abstract class Beeman {
    public static double beemanX(double x, double v, double a, double aBefore, double dT) {
        return x + v * dT + (2f/3)*a*dT*dT-(1f/6)*aBefore*dT*dT;
    }

    public static double beemanVelocityC(double v,double aBefore, double a, double aAfter, double dT ) {
        return v + (1f/3)*aAfter*dT + (5f/6)*a*dT - (1f/6)*aBefore*dT;
    }

    public static double beemanVelocityP(double v, double a, double aBefore, double dT) {
        return v + (3f/2)*a*dT - (1f/2)*aBefore*dT;
    }
}
