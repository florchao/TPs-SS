import java.util.Objects;
import java.util.TreeMap;

public class Particle {

//    private double v;
    private int id;
    private double vx;
    private double vy;
//    private String collision; // ?
//    private boolean isBorder; // ?
    private double weight;
    private double xPos;
    private double yPos;
    private double radius;

    public Particle(int id, double xPos, double yPos, double vx, double vy, double weight, double radius) {
        this.id = id;
        this.vx = vx;
        this.vy = vy;
        this.weight = weight;
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
//        this.v = 0.09;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

//    public String getCollision() {
//        return collision;
//    }
//
//    public void setCollision(String collision) {
//        this.collision = collision;
//    }

//    public boolean isBorder() {
//        return isBorder;
//    }
//
//    public void setBorder(boolean border) {
//        isBorder = border;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void collisionAgainstHorizontalWall(){
        setVy(-getVy());
    }

    public void updatePosition(double time) {
        setxPos(getxPos()+time*getVx());
        setyPos(getyPos()+time*getVy());
    }

    public int collisionAgainstVerticalWall(Double time, double L, double width) {
        //returns false if it passes through wall, true if it collides
        double oldxpos = getxPos();
        setVx(-getVx());
        double newxpos = getxPos()+time*getVx();
        double lowerOpening = (width-L)/2;
        double upperOpening = width - ((width-L)/2);
        //passes through wall from A to B
        if (oldxpos <= width && newxpos > width && getyPos() < upperOpening && getyPos() > lowerOpening){
            return 1;
        }
        //passes through wall from B to A
        if (oldxpos >= width && newxpos < width && getyPos() < upperOpening && getyPos() > lowerOpening){
            return 2;
        }
        return 0;
    }

    public void collisionAgainstParticle(Particle p2){

        double deltaX = p2.getxPos() - getxPos();
        double deltaY = p2.getyPos() - getyPos();
        double deltaVx = p2.getVx() - getVx();
        double deltaVy = p2.getVy() - getVy();
        double deltaVR = deltaX * deltaVx + deltaY * deltaVy;
        double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        double sigma = getRadius() + p2.getRadius();
        double J;

        if (p2.getWeight() == Double.POSITIVE_INFINITY) {
            J = (2 * getWeight() * deltaVR) / ((getWeight()) * sigma);
        } else {
            J = (2 * getWeight() * p2.getWeight() * deltaVR) / ((getWeight() + p2.getWeight()) * sigma);
        }

        double Jx = J * deltaX / dist;
        double Jy = J * deltaY / dist;

        double newVx1 = getVx() + Jx / getWeight();
        double newVy1 = getVy() + Jy / getWeight();
        double newVx2 = p2.getVx() - Jx / p2.getWeight();
        double newVy2 = p2.getVy() - Jy / p2.getWeight();

        setVx(newVx1);
        setVy(newVy1);
        p2.setVx(newVx2);
        p2.setVy(newVy2);
    }

    public boolean isInsideBoundaries(double containerWidth, double L) {
        if (xPos < 0 || yPos < 0) {
            return false;
        }
        if (yPos > containerWidth) {
            return false;
        }
        if (xPos > 2 * containerWidth) {
            return false;
        }
        if (xPos > containerWidth && xPos < 2 * containerWidth) {
            if (yPos > (containerWidth / 2) - (L / 2) && yPos < (containerWidth / 2) + (L / 2)) {
                return true;
            }
            return false;
        }
        return true;
    }

    public double timeCollisionAgainstParticle (Particle p2) {
        double time = Double.POSITIVE_INFINITY;

        double sigma = getRadius() + p2.getRadius();

        double deltaVx = p2.getVx() - getVx();
        double deltaVy = p2.getVy() - getVy();

        double deltaRx = p2.getxPos() - getxPos();
        double deltaRy = p2.getyPos() - getyPos();

        double deltaR2 = deltaRx * deltaRx + deltaRy * deltaRy;
        double deltaV2 = deltaVx * deltaVx + deltaVy * deltaVy;
        double deltaRV = deltaVx*deltaRx + deltaRy*deltaVy;

        double d = (deltaRV * deltaRV) - deltaV2 * (deltaR2 - sigma * sigma);
        double tolerance = 1E-9;

        if (deltaRV >= 0 || d < 0)
            return time;

        if (deltaR2 - sigma * sigma < tolerance){
            System.out.println("Particles in contact");
        }

        if (deltaR2 + tolerance < sigma * sigma) {
            System.out.println("overlapping particles for particles " + this.getId() + " and " + p2.getId());
            System.out.println("Exception --------------------------------------------");
        }

        time = -1*(deltaRV + Math.sqrt(d))/deltaV2;

        return time;
    }

    public Double timeCollisionAgainstHorizontalWall(double width, double L){
        double time = Double.POSITIVE_INFINITY;
        double x = getxPos();
        double y = getyPos();
        double vx = getVx();
        double vy = getVy();
        double radius = getRadius();
        if (vy > 0) {
            double timeToMidUpper = ((width + L) / 2 - y - radius) / vy;
            double midUpperX = x + radius + vx * timeToMidUpper;
            if (timeToMidUpper < 0 || midUpperX < width) {
                double timeToCeiling = (width - y - radius) / vy;
                time = Math.min (time, timeToCeiling);
            } else if (timeToMidUpper > 0 && midUpperX > width) {
                time = Math.min(time, timeToMidUpper);
            }
        } else if (vy < 0) {
            double timeToMidLower = ((width - L) / 2 - y + radius) / vy;
            double midLowerX = x + radius + vx * timeToMidLower;
            if (timeToMidLower < 0 || midLowerX < width) {
                double timeToFloor = (radius - y) / vy;
                time = Math.min(time, timeToFloor);
            } else if (timeToMidLower > 0 && midLowerX > width) {
                time = Math.min(time, timeToMidLower);
            }
        }
        return time;
    }

    public Double timeCollisionAgainstVerticalWall(double width, double L, TreeMap<Double, Double> leftSideImpulses, TreeMap<Double, Double> totalImpulses, TreeMap<Double, Double> rightSideImpulses){
        double time = Double.POSITIVE_INFINITY;
        double x = getxPos();
        double y = getyPos();
        double vx = getVx();
        double vy = getVy();
        double radius = getRadius();
        if (vx > 0) {
            double timeToMidWall = (width - x - radius) / vx;
            double upperMidWallY = y + vy * timeToMidWall;
            double lowerMidWallY = y + vy * timeToMidWall;
            if (x < width && (upperMidWallY > (width + L) / 2 || lowerMidWallY < (width - L) / 2)) {
                if (timeToMidWall > 0) {
                    time = Math.min(time, timeToMidWall);
                    leftSideImpulses.put(time, Math.abs(getVy())/(4*width - L));
                    totalImpulses.put(time, Math.abs(getVy())/(4*width + 2*width));
                }
            } else {
                time = Math.min(time, (2 * width - x - radius) / vx);
                rightSideImpulses.put(time, Math.abs(getVy())/(2*width + L));
                totalImpulses.put(time, Math.abs(getVy())/(4*width + 2*width));
            }
        } else if (vx < 0) {
            time = Math.min(time, (radius - x) / vx);
        }
            return time;
        }
}
