import java.util.Objects;

public class Collision {

    private Particle p1;
    private Particle p2;

    private double time;


    private boolean isHorizontalWall;

    private boolean isVerticalWall;

    public Collision(Particle p1, Particle p2, double time) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
    }

    public Collision(Particle p1, boolean isHorizontalWall, boolean isVerticalWall, double time) {
        this.p1 = p1;
        if (isHorizontalWall == isVerticalWall)
            throw new IllegalArgumentException("isHorizontalWall cannot be equal to isVerticalWall");
        this.isHorizontalWall = isHorizontalWall;
        this.isVerticalWall = isVerticalWall;
        this.time = time;
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

    public double getTime() {
        return time;
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
