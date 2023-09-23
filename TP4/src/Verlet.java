package src;

public abstract class Verlet {
    public static double verletX(double x, double xBefore,double f, double mass, double dT){
        return (2 * x) - xBefore + ((dT*dT) * (f/mass));
    }

    public static double verletVelocity(double xAfter, double xBefore, double dT){
        return (xAfter - xBefore) / (2 * dT);
    }
}
