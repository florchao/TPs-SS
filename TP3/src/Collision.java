import java.util.Objects;

public class Collision implements Comparable<Collision>{

    private Particle p1;
    private Particle p2;

    private double time;


    private CollisionType collisionType;

    public Collision(Particle p1, Particle p2, CollisionType collisionType, double time) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.collisionType = collisionType;
    }

    public Particle getP1() {
        return p1;
    }

    public Particle getP2() {
        return p2;
    }

    public CollisionType getCollisionType(){
        return collisionType;
    }

    public Double getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collision collision = (Collision) o;
        if (p1.equals(collision.getP1()) && p2.equals(collision.getP2())) {
            if (p2 == null) {
                return collisionType.equals(((Collision) o).getCollisionType());
            }
            return true;
        }
        else return p1.equals(collision.getP2()) && p2.equals(collision.getP1());
    }

    @Override
    public int hashCode() {
        return p2 == null ? Objects.hash(p1.getId()) : Objects.hash(p1.getId(), p2.getId());
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(this.time, o.time);
    }
}
