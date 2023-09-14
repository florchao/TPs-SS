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


    public Double timeCollisionAgainstHorizontalWall(){
        Double time = Double.POSITIVE_INFINITY;
        if (p1.getVy() > 0){
            time = (1-p1.getRadius()-p1.getyPos())/ p1.getVy();
        } else if ( p1.getVy() < 0) {
            time = (p1.getRadius() - p1.getyPos())/p1.getVy();
        }
        return time;
    }

    public Double timeCollisionAgainstVerticalWall(){
        Double time = Double.POSITIVE_INFINITY;
        if (p1.getVx() > 0){
            time = (1-p1.getRadius()-p1.getxPos())/ p1.getVx();
        } else if ( p1.getVy() < 0) {
            time = (p1.getRadius() - p1.getxPos())/p1.getVx();
        }
        return time;
    }

    public Double timeCollisionAgainstParticle () {
        Double time = Double.POSITIVE_INFINITY;

        double sigma = Math.pow(p1.getRadius() + p2.getRadius(), 2);

        double deltaVx = p1.getVx() - p2.getVx();
        double deltaVy = p1.getVy() - p2.getVy();

        double deltaRx = p1.getxPos() - p2.getxPos();
        double deltaRy = p1.getyPos() - p2.getyPos();

        double deltaR2 = Math.pow(deltaRx, 2) + Math.pow(deltaRy, 2);
        double deltaV2 = Math.pow(deltaVx, 2) + Math.pow(deltaVy, 2);
        double deltaRV = deltaVx*deltaRx + deltaRy*deltaVy;

        if (deltaRV >= 0)
            return time;
        
        double d = Math.pow(deltaRV, 2) - (deltaV2)*(deltaR2-sigma);

        if (d < 0)
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
        return p2 == null ? Objects.hash(p1.getId()) : Objects.hash(p1.getId(), p2.getId());
    }

}
