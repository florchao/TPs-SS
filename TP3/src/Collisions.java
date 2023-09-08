public class Collisions {

    public void collisionAgainstHorizontalWall(Particle p){
        p.setVy(-p.getVy());
    }

    public void collisionAgainstVerticalWall(Particle p){
        p.setVx(-p.getVx());
    }

    public void collisionAgainstParticle(Particle p1, Particle p2){
        double deltaX = p2.getxPos() - p1.getxPos();
        double deltaY = p2.getyPos() - p1.getyPos();
        double deltaVx = p2.getVx() - p1.getVx();
        double deltaVy = p2.getVy() - p1.getVy();
        double deltaR = deltaX*deltaVx + deltaY*deltaVy;
        double dist = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        double sigma = p1.getRadius() + p2.getRadius();
        double J = 2*p1.getWeight()*p2.getWeight()*deltaR / ((p1.getWeight() + p2.getWeight())*sigma);
        double Jx = J*deltaX / dist;
        double Jy = J*deltaY / dist;

        p1.setVx(p1.getVx() + Jx/p1.getWeight());
        p1.setVy(p1.getVy() + Jy/p1.getWeight());

        p2.setVx(p2.getVx() - Jx/p2.getWeight());
        p2.setVy(p2.getVy() - Jy/p2.getWeight());
    }


    public Double timeCollisionAgainstHorizontalWall(Particle p){
        Double time = null;
        if (p.getVy() > 0){
            time = (1-p.getRadius()-p.getyPos())/ p.getVy();
        } else if ( p.getVy() < 0) {
            time = (p.getRadius() - p.getyPos())/p.getVy();
        }
        return time;
    }

    public Double timeCollisionAgainstVerticalWall(Particle p){
        Double time = null;
        if (p.getVx() > 0){
            time = (1-p.getRadius()-p.getxPos())/ p.getVx();
        } else if ( p.getVy() < 0) {
            time = (p.getRadius() - p.getxPos())/p.getVx();
        }
        return time;
    }

    public Double timeCollisionAgainstParticle (Particle p1, Particle p2) {

        double sigma = Math.pow(p1.getRadius() + p2.getRadius(), 2);

        double deltaVx = p1.getVx() - p2.getVx();
        double deltaVy = p1.getVy() - p2.getVy();

        double deltaRx = p1.getxPos() - p2.getxPos();
        double deltaRy = p1.getyPos() - p2.getyPos();

        double deltaR2 = Math.pow(deltaRx, 2) + Math.pow(deltaRy, 2);
        double deltaV2 = Math.pow(deltaVx, 2) + Math.pow(deltaVy, 2);
        double deltaRV = deltaVx*deltaRx + deltaRy*deltaVy;

        if (deltaRV >= 0)
            return null;
        
        double d = Math.pow(deltaRV, 2) - (deltaV2)*(deltaR2-sigma);

        if (d < 0)
            return null;

        return -1*(deltaRV + Math.sqrt(d))/deltaV2;
    }
}
