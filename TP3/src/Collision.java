public class Collision implements Comparable<Collision> {
    private final Particle p1;
    private final Particle p2;
    private final double time;
    private final CollisionType type;

    public Collision(Particle p1, Particle p2, double time, CollisionType type) {
        this.p1 = p1;
        this.p2 = p2;
        this.time = time;
        this.type = type;
    }

    public Collision(Particle p1, double time, CollisionType type) {
        this.p1 = p1;
        this.p2 = null;
        this.time = time;
        this.type = type;
    }

    public void collide(double width, double L) {
        if (type == null){
            System.out.println(p1.getId());
            System.out.println(time);
        }
        switch (type) {
            case MIDDLE_WALL:
                p1.setVx(-p1.getVx());
                break;
            case RIGHT_VERTICAL_WALL:
                p1.setVx(-p1.getVx());
                break;
            case LEFT_VERTICAL_WALL:
                p1.setVx(-p1.getVx());
                break;
            case LEFT_HORIZONTAL_WALL:
            case RIGHT_HORIZONTAL_WALL:
                p1.setVy(-p1.getVy());
                break;
            case UPPER_MIDDLE_CORNER:

                if (p1.getXpos() > width)
                    p1.setVy(-p1.getVy());
                else {
                    if (! (p1.getYpos() > (width+L)/2))
                        p1.setVy(-p1.getVy());
                    p1.setVx(-p1.getVx());
                }
                break;

            case LOWER_MIDDLE_CORNER:

                if (p1.getXpos() > width)
                    p1.setVy(-p1.getVy());
                else {
                    if (! (p1.getYpos() < (width-L)/2))
                        p1.setVy(-p1.getVy());
                    p1.setVx(-p1.getVx());
                }
                break;

            case PARTICLE:
                double x1 = p1.getXpos();
                double y1 = p1.getYpos();
                double vx1 = p1.getVx();
                double vy1 = p1.getVy();
                double radius1 = p1.getRadius();

                if (p2 == null) {
                    throw new IllegalArgumentException("p2 cannot be null if Collision is of type PARTICLE");
                }

                double x2 = p2.getXpos();
                double y2 = p2.getYpos();
                double vx2 = p2.getVx();
                double vy2 = p2.getVy();
                double radius2 = p2.getRadius();

                double dx = x2 - x1;
                double dy = y2 - y1;
                double dvx = vx2 - vx1;
                double dvy = vy2 - vy1;
                double dvdr = dx * dvx + dy * dvy;
                double dist = Math.sqrt(dx * dx + dy * dy);

                double sigma = radius1 + radius2;
                double tolerance = 1E-6;
                double J;

                if (p2.getWeight() == Double.POSITIVE_INFINITY) {
                    J = (2 * p1.getWeight() * dvdr) / ((p1.getWeight()) * sigma);
                } else {
                    J = (2 * p1.getWeight() * p2.getWeight() * dvdr) / ((p1.getWeight() + p2.getWeight()) * sigma);
                }

                double Jx = J * dx / dist;
                double Jy = J * dy / dist;
                double newVx1 = vx1 + Jx / p1.getWeight();
                double newVy1 = vy1 + Jy / p1.getWeight();
                double newVx2 = vx2 - Jx / p2.getWeight();
                double newVy2 = vy2 - Jy / p2.getWeight();
                p1.setVelocity(newVx1, newVy1);
                p2.setVelocity(newVx2, newVy2);
                break;
            default:
                throw new IllegalArgumentException("Invalid CollisionType");

        }
    }

    public Particle getP1() {
        return this.p1;
    }

    public Particle getP2() {
        return this.p2;
    }

    public double getTime() {
        return this.time;
    }

    public CollisionType getType() { return this.type; }

    @Override
    public String toString() {
        return "Collision{" +
                "p1=" + p1 +
                ", p2=" + (p2==null? "wall":p2) +
                ", time=" + time +
                '}';
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(this.time, o.time);
    }

    // Override equals method so that a collision is equal to another if they're both wall collisions involving the same particle or they're both ParticlCollisions involving the same two particles in any order
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Collision))
            return false;
        Collision collision = (Collision) o;
        if (type != collision.type)
            return false;
        if (type != CollisionType.PARTICLE) {
            return (p1.equals(collision.p1));
        } else {
            return (p1.equals(collision.p1) && (p2 != null && p2.equals(collision.p2))) || (p1.equals(collision.p2) && (p2 != null && p2.equals(collision.p1)));
        }
    }
}