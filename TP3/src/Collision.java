import java.util.Objects;

public class Collision {

    private Particle p1;
    private Particle p2;


    private boolean isHorizontalWall;

    private boolean isVerticalWall;

    public Collision(Particle p1, Particle p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Collision(Particle p1, boolean isHorizontalWall, boolean isVerticalWall) {
        this.p1 = p1;
        if (isHorizontalWall == isVerticalWall)
            throw new IllegalArgumentException("isHorizontalWall cannot be equal to isVerticalWall");
        this.isHorizontalWall = isHorizontalWall;
        this.isVerticalWall = isVerticalWall;
    }

    public Double timeCollisionAgainstHorizontalWall(double width, double L){
        double time = Double.POSITIVE_INFINITY;
        if (p1.getVy() > 0){
            if (p1.getxPos() < width)
                time = (width - p1.getRadius() - p1.getyPos())/ p1.getVy();
            else
                time = ((width + L) - p1.getRadius() - p1.getyPos())/ p1.getVy();
        } else if ( p1.getVy() < 0) {
            if (p1.getxPos() < width)
                time = (p1.getRadius() - p1.getyPos())/p1.getVy();
            else
                time = ((width - L) + p1.getRadius() - p1.getyPos())/p1.getVy();
        }
        System.out.println("Horizontal time: " + time);
        return time;
    }

    public Double timeCollisionAgainstVerticalWall(double width, double L){
        double time = Double.POSITIVE_INFINITY;
        if (p1.getVx() > 0){
            if (p1.getxPos() > width)
                time = ((width+L) - p1.getRadius() - p1.getxPos())/ p1.getVx();
            else{
                double auxTime = (width - p1.getRadius() - p1.getxPos())/ p1.getVx();
                double middleY = p1.getyPos() + p1.getVy() * auxTime;
                if (middleY + p1.getRadius() < (L + width) / 2 || middleY - p1.getRadius() > (L - width) / 2) {
                    time = ((width+L) - p1.getRadius()-p1.getxPos())/ p1.getVx();
                }
            }
        } else if ( p1.getVx() < 0) {
            time = (p1.getRadius() - p1.getxPos())/p1.getVx();
        }
        System.out.println("Vertical time: " + time);
        return time;
    }

    public Double timeCollisionAgainstParticle () {
        Double time = Double.POSITIVE_INFINITY;

        double sigma = p1.getRadius() + p2.getRadius();

        double deltaVx = p2.getVx() - p1.getVx();
        double deltaVy = p2.getVy() - p1.getVy();

        double deltaRx = p2.getxPos() - p1.getxPos();
        double deltaRy = p2.getyPos() - p1.getyPos();

        double deltaR2 = deltaRx * deltaRx + deltaRy * deltaRy;
        double deltaV2 = deltaVx * deltaVx + deltaVy * deltaVy;
        double deltaRV = deltaVx*deltaRx + deltaRy*deltaVy;

        double d = (deltaRV * deltaRV) - deltaV2 * (deltaR2 - (sigma * sigma));

        if (deltaRV >= 0 || d < 0)
            return time;

        time = -1*(deltaRV + Math.sqrt(d))/deltaV2;

        return time;
    }

    public Particle getP1() {
        return p1;
    }

    public Particle getP2() {
        return p2;
    }

    public boolean isHorizontalWall() {
        return isHorizontalWall;
    }

    public boolean isVerticalWall() {
        return isVerticalWall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collision collision = (Collision) o;
        if (p1.equals(collision.getP1()) && p2.equals(collision.getP2())) {
            if (p2 == null) {
                return (isHorizontalWall && collision.isHorizontalWall()) || (isVerticalWall && collision.isVerticalWall());
            }
            return true;
        } else return p1.equals(collision.getP2()) && p2.equals(collision.getP1());
    }

    @Override
    public int hashCode() {
        if (p2 == null) {
            return Objects.hash(p1.getId());
        }
        if (p1.getId() < p2.getId()) {
            return Objects.hash(p1.getId(), p2.getId());
        }
        return Objects.hash(p2.getId(), p1.getId());
    }

}
