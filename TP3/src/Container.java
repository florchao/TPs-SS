import java.util.TreeSet;
public class Container {
    private final double width = 0.09;
    private double L;
    private TreeSet<Particle> particles;
    private TreeSet<Collision> collisions;
    private Particle upperCorner;
    private Particle lowerCorner;
    private double leftPerimeter;
    private double rightPerimeter;
    private double totalPerimeter;
    double leftImpulses=0;
    double rightImpulses= 0;

    public Container(double l, TreeSet<Particle> particles) {
        this.L = l;
        this.particles = particles;
        this.collisions = new TreeSet<>();
        this.upperCorner = new Particle(0, 0,0, width, (L+ width)/2, Double.POSITIVE_INFINITY);
        this.lowerCorner = new Particle(0, 0,0, width, (width -L)/2, Double.POSITIVE_INFINITY);
        this.leftPerimeter = 4* width - L;
        this.rightPerimeter = 2* width + L;
        this.totalPerimeter = leftPerimeter + rightPerimeter;
    }

    public double executeCollisions(double time) {
        double timeOfFirstCollision = Double.POSITIVE_INFINITY;
        for (Particle p : particles) {
            timeOfFirstCollision = Math.min(timeOfFirstCollision, calculateCollisions(p, time));
        }
        if (timeOfFirstCollision == Double.POSITIVE_INFINITY) {
            throw new RuntimeException("No collisions found");
        }
        return timeOfFirstCollision;
    }

    public double calculateCollisions(Particle p1, double currentTime) {
        double timeOfFirstCollision = Double.POSITIVE_INFINITY;
        for (Particle p2 : particles) {
            if (p1 != p2) {
                double timeToCollision = p1.timeToCollision(p2);
                if (timeToCollision >= 0) {
                    Collision particleCollision = new Collision(p1, p2, timeToCollision + currentTime, CollisionType.PARTICLE);
                    collisions.add(particleCollision);
                    timeOfFirstCollision = Math.min(timeOfFirstCollision, particleCollision.getTime());
                }
            }
        }

        Collision wallCollision = getNextWallCollision(p1, currentTime);
        if (wallCollision.getTime() >= 0 && wallCollision.getTime() != Double.POSITIVE_INFINITY) {
            timeOfFirstCollision = Math.min(timeOfFirstCollision, wallCollision.getTime());
            collisions.add(wallCollision);
        }

        return timeOfFirstCollision;
    }

    public Collision getNextWallCollision(Particle p, double currentTime) {
        double time = Double.POSITIVE_INFINITY;
        CollisionType type = null;

        double timeToUpperCorner = p.timeToCollision(this.upperCorner);
        double timeToLowerCorner = p.timeToCollision(this.lowerCorner);

        if (timeToUpperCorner >= 0 && timeToUpperCorner < time) {
            time = timeToUpperCorner;
            type = CollisionType.UPPER_MIDDLE_CORNER;
        }
        if (timeToLowerCorner >= 0 && timeToLowerCorner < time ) {
            time = timeToLowerCorner;
            type = CollisionType.LOWER_MIDDLE_CORNER;
        }

        if (p.getVx() > 0) {
            double timeToMidWall = (width - p.getXpos() - p.getRadius()) / p.getVx();
            double upperMidWallY = p.getYpos() + p.getVy() * timeToMidWall;
            double lowerMidWallY = p.getYpos() + p.getVy() * timeToMidWall;
            if (p.getXpos() < width && (upperMidWallY > (width + L) / 2 || lowerMidWallY < (width - L) / 2)) {
                if (timeToMidWall > 0 && timeToMidWall < time) {
                    time = timeToMidWall;
                    type = CollisionType.MIDDLE_WALL;
                }

            } else {
                double timeToRightVertical = (2 * width - p.getXpos() - p.getRadius()) / p.getVx();
                if (timeToRightVertical > 0 && timeToRightVertical < time) {
                    time = timeToRightVertical;
                    type = CollisionType.RIGHT_VERTICAL_WALL;
                }
            }
        } else if (p.getVx() < 0) {
            double timeToLeftVertical = (p.getRadius() - p.getXpos()) / p.getVx();
            if (timeToLeftVertical > 0 && timeToLeftVertical < time) {
                time = timeToLeftVertical;
                type = CollisionType.LEFT_VERTICAL_WALL;
            }
        }
        if (p.getVy() > 0) {
            double timeToMidUpper = ((width + L) / 2 - p.getYpos() - p.getRadius()) / p.getVy();
            double midUpperX = p.getXpos() + p.getRadius() + p.getVx() * timeToMidUpper;
            if (timeToMidUpper < 0 || midUpperX < width) {
                double timeToCeiling = (width - p.getYpos() - p.getRadius()) / p.getVy();
                if (timeToCeiling > 0 && timeToCeiling < time) {
                    time = timeToCeiling;
                    type = CollisionType.LEFT_HORIZONTAL_WALL;
                }
            } else if (timeToMidUpper > 0 && midUpperX > width) {
                if (timeToMidUpper < time) {
                    time = timeToMidUpper;
                    type = CollisionType.RIGHT_HORIZONTAL_WALL;
                }
            }
        } else if (p.getVy() < 0) {
            double timeToMidLower = ((width - L) / 2 - p.getYpos() + p.getRadius()) / p.getVy();
            double midLowerX = p.getXpos() + p.getRadius() + p.getVx() * timeToMidLower;
            if (timeToMidLower < 0 || midLowerX < width) {
                double timeToFloor = (p.getRadius() - p.getYpos()) / p.getVy();
                if (timeToFloor > 0 && timeToFloor < time ) {
                    time = timeToFloor;
                    type = CollisionType.LEFT_HORIZONTAL_WALL;
                }
            } else if (timeToMidLower > 0 && midLowerX > width) {
                if (timeToMidLower < time) {
                    time = timeToMidLower;
                    type = CollisionType.RIGHT_HORIZONTAL_WALL;
                }
            }
        }
        Particle p2 = null;
        if (type == CollisionType.UPPER_MIDDLE_CORNER)
            p2 = this.upperCorner;
        else if (type == CollisionType.LOWER_MIDDLE_CORNER)
            p2 = this.lowerCorner;
        return new Collision(p, p2, time + currentTime, type);
    }


    public void addPressure(double v, CollisionType type, double timeOfCollision) {
        if (type == null){
            return;
        }
        switch (type) {
            case LEFT_HORIZONTAL_WALL:
                leftImpulses += 2*Math.abs(v);
            case LEFT_VERTICAL_WALL:
                leftImpulses += 2*Math.abs(v);
            case MIDDLE_WALL:
                leftImpulses += 2*Math.abs(v);
                break;
            case RIGHT_HORIZONTAL_WALL:
                rightImpulses += 2*Math.abs(v);
            case RIGHT_VERTICAL_WALL:
                rightImpulses += 2*Math.abs(v);
                break;
            case UPPER_MIDDLE_CORNER:
            case LOWER_MIDDLE_CORNER:
                break;
            default:
                throw new RuntimeException("Cannot compute pressure for collision of type " + type);
        }
    }

    public double getWidth() {
        return width;
    }

    public double getL() {
        return L;
    }

    public Particle getUpperCorner() {
        return upperCorner;
    }

    public Particle getLowerCorner() {
        return lowerCorner;
    }

    public TreeSet<Collision> getCollisions() {
        return collisions;
    }


}