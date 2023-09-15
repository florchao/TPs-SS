import java.util.*;

public class Container {

    private final static double width = 0.09;
    private double L;

    private int particlesA;

    private int particlesB;


    private Set<Particle> particles;

    // need to order map by key
    private TreeSet<Collision> particleCollisionTimes;

    public Container(double L, Set<Particle> particles) {
        this.L = L;

        this.particles = particles;

        this.particlesA = particles.size();
        this.particlesB = 0;

        this.particleCollisionTimes = new TreeSet<> ((o1,  o2) -> {
            int c = 0;
            if (o1.getTime() > o2.getTime())
                c = 1;
            else if (o1.getTime() < o2.getTime())
                c = -1;
            return c;
        });
    }

    //For each particle, adds the first collision thats going to happen
    private void setCollisionTimes(Particle p1) {
        Collision newCollision = null;
        for (Particle p2 : particles) {
            if (!p1.equals(p2)) {
                Double time = p1.timeCollisionAgainstParticle(p2);
                if (time != Double.POSITIVE_INFINITY && time > 0) {
                    newCollision = new Collision(p1, p2, time);
                }
            }
        }
        Double verticalTime = p1.timeCollisionAgainstVerticalWall(width, L);
        if (verticalTime != Double.POSITIVE_INFINITY && verticalTime > 0) {
            if (newCollision == null || newCollision.getTime() > verticalTime){
                newCollision = new Collision(p1, false, true, verticalTime);
            }
        }

        Double horizontalTime = p1.timeCollisionAgainstHorizontalWall(width, L);
        if (horizontalTime != Double.POSITIVE_INFINITY && horizontalTime > 0) {
            if (newCollision == null || newCollision.getTime() > horizontalTime){
                newCollision = new Collision(p1, true, false, horizontalTime);
            }
        }
        if (newCollision != null)
            particleCollisionTimes.add(newCollision);
    }

    public TreeSet<Collision> updateCollisionTimes(Collision oldCollision) {
        TreeSet<Collision> newParticleCollisionTimes = new TreeSet<>(particleCollisionTimes.comparator());
        while (!particleCollisionTimes.isEmpty()){
            Collision newCollision = particleCollisionTimes.pollFirst();
            if (Math.abs(oldCollision.getTime() - newCollision.getTime()) >= 0.1) {
                newParticleCollisionTimes.add(newCollision);
            }
        }
        return newParticleCollisionTimes;
    }

    // 1- Calculo todas las colisiones posibles
    // 2- Sucede colision
    // 3- descarto todas las colisiones que ya tenian
    // 4- Sucede una colision -> recalculo todas las colisiones posibles para ambas
    // particulas


    public double executeCollisions() {
        for (Particle p : particles){
            setCollisionTimes(p);
        }
        Collision lastCollision = particleCollisionTimes.pollFirst();
        //particleCollisionTimes = updateCollisionTimes(lastCollision);
        Collision newCollision = lastCollision;
        Particle p1 = newCollision.getP1();
        Particle p2 = newCollision.getP2();

        for (Particle p : particles) {
            p.setxPos(p.getxPos() + p.getVx() * newCollision.getTime());
            p.setyPos(p.getyPos() + p.getVy() * newCollision.getTime());
        }

        if (newCollision.isHorizontalWall()) {
            p1.collisionAgainstHorizontalWall();
        } else if (newCollision.isVerticalWall()) {
            //passes from A container to B container
            int res = p1.collisionAgainstVerticalWall(newCollision.getTime(), L, width);
            if (res  == 1) {
                particlesB++;
                particlesA--;
            }
            //passes from B container to A container
            else if (res == 2) {
                particlesB--;
                particlesA++;
            }
        } else {
            p1.collisionAgainstParticle(p2, newCollision.getTime());
        }
        return newCollision.getTime();
    }

    public void moveParticles(double time) {
        particles.forEach(p -> {
            p.updatePosition(time);
        });
    }

    public Set<Particle> getParticles() {
        return particles;
    }

//    public Collision getNextCollision() {
//        Optional<Collision> nextCollision = particleCollisionTimes.get(getFirstCollisionTime()).stream().findFirst();
//        return nextCollision.orElse(null);
//    }
}
