import java.util.Objects;

public class Particle {

    private double v;
    private int id;
    private double vx;
    private double vy;
    private String collision; // ?
    private boolean isBorder; // ?
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
        this.v = 0.09;
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

    public String getCollision() {
        return collision;
    }

    public void setCollision(String collision) {
        this.collision = collision;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public void setBorder(boolean border) {
        isBorder = border;
    }

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
        double newxPos = getxPos()+time*getVx();
        setxPos(newxPos);
        setyPos(getyPos()+time*getVy());
        System.out.println("New Pos: x: " + getxPos() + " y: " + getyPos());
    }

    public int collisionAgainstVerticalWall(Double time, double L, double width) {
        //returns false if it passes through wall, true if it collides
        double newxpos = getxPos()+time*getVx();
        double lowerOpening = (width-L)/2;
        double upperOpening = width - ((width-L)/2);
        //passes through wall from A to B
        setVx(-getVx());
        /*
        if (getxPos() <= L && newxpos > L && getyPos() < upperOpening && getyPos() > lowerOpening){
            return 1;
        }
        //passes through wall from B to A
        if (getxPos() >= L && newxpos < L && getyPos() < upperOpening && getyPos() > lowerOpening){
            return 2;
        }
        */
        return 0;
    }

    public void collisionAgainstParticle(Particle p2, Double time){
        double deltaX = Math.abs(p2.getxPos() - getxPos());
        double deltaY = Math.abs(p2.getyPos() - getyPos());
        double deltaVx = p2.getVx() - getVx();
        double deltaVy = p2.getVy() - getVy();
        double deltaV_R = deltaX*deltaVx + deltaY*deltaVy;
        double sigma = getRadius() + p2.getRadius();
        double J = 2*getWeight()*p2.getWeight()*deltaV_R / ((getWeight() + p2.getWeight())*sigma);
        double Jx = J*deltaX / sigma;
        double Jy = J*deltaY / sigma;

        setVx(getVx() + Jx/getWeight());
        setVy(getVy() + Jy/getWeight());

        setxPos(getxPos()+time*getVx());
        setyPos(getyPos()+time*getVy());

        p2.setVx(p2.getVx() - Jx/p2.getWeight());
        p2.setVy(p2.getVy() - Jy/p2.getWeight());

        p2.setxPos(p2.getxPos()+time*p2.getVx());
        p2.setyPos(p2.getyPos()+time*p2.getVy());

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

    public Double timeCollisionAgainstParticle (Particle p2) {
        double time = Double.POSITIVE_INFINITY;

        double sigma = this.getRadius() + p2.getRadius();

        double deltaVx = p2.getVx() - this.getVx();
        double deltaVy = p2.getVy() - this.getVy();

        double deltaRx = p2.getxPos() - this.getxPos();
        double deltaRy = p2.getyPos() - this.getyPos();

        double deltaR2 = deltaRx * deltaRx + deltaRy * deltaRy;
        double deltaV2 = deltaVx * deltaVx + deltaVy * deltaVy;
        double deltaRV = deltaVx*deltaRx + deltaRy*deltaVy;

        double d = (deltaRV * deltaRV) - deltaV2 * (deltaR2 - (sigma * sigma));

        if (deltaRV >= 0 || d < 0)
            return time;

        time = -1*(deltaRV + Math.sqrt(d))/deltaV2;

        return time;
    }

    public Double timeCollisionAgainstHorizontalWall(double width, double L){
        double time = Double.POSITIVE_INFINITY;
        if (this.getVy() > 0){
            if (this.getxPos() < width)
                time = (width - this.getRadius() - this.getyPos())/ this.getVy();
            else
                time = ((width + L) - this.getRadius() - this.getyPos())/ this.getVy();
        } else if ( this.getVy() < 0) {
            if (this.getxPos() < width)
                time = (this.getRadius() - this.getyPos())/this.getVy();
            else
                time = ((width - L) + this.getRadius() - this.getyPos())/this.getVy();
        }
        return time;
    }

    public Double timeCollisionAgainstVerticalWall(double width, double L){
        double time = Double.POSITIVE_INFINITY;
        if (this.getVx() > 0){
            if (this.getxPos() > width)
                time = ((width+L) - this.getRadius() - this.getxPos())/ this.getVx();
            else{
                double auxTime = (width - this.getRadius() - this.getxPos())/ this.getVx();
                double middleY = this.getyPos() + this.getVy() * auxTime;
                if (middleY + this.getRadius() < (L + width) / 2 || middleY - this.getRadius() > (L - width) / 2) {
                    time = ((width+L) - this.getRadius()-this.getxPos())/ this.getVx();
                }
            }
        } else if ( this.getVx() < 0) {
            time = (this.getRadius() - this.getxPos())/this.getVx();
        }
        return time;
    }
}
