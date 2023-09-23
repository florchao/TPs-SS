package src;

public abstract class Euler {

    public static double eulerX(double x, double v, double dT, double f, double mass){
        return x + v * dT + dT*dT * (f/(2*mass));
    }

    public static double eulerVelocity(double v, double f, double m, double dT){
        return v + dT * f / m;
    }

}
