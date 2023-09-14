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

    public void collisionAgainstHorizontalWall(Double time){
        setVy(-getVy());
        updatePosition(time);
    }

    public void updatePosition(double time) {
        setxPos(getxPos()+time*getVx());
        setyPos(getyPos()+time*getVy());
    }

    public boolean collisionAgainstVerticalWall(Double time, double middle) {
        //returns false if it passes through wall, true if it collides
        double newxpos = getxPos()+time*getVx();
        //passes through wall
        if (getxPos() <= 0 &&  newxpos > 0 && getyPos() <= middle && getyPos() >= middle){
            updatePosition(time);
            return false;
        }
        setVx(-getVx());
        updatePosition(time);
        return true;
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
}
