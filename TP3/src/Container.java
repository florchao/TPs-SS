import java.util.*;

public class Container {

    private final static double width = 0.09;
    private double L;

    private int particlesA;

    private int particlesB;

    private Set<Particle> particles;

    // need to order map by key
    private Map<Double, Collision> particleCollisionTimes = new TreeMap<>(Comparator.naturalOrder());

    public Container(double L, Set<Particle> particles) {
        this.L = L;

        this.particles = particles;

        this.particlesA = particles.size();
        this.particlesB = 0;

        for (Particle p1 : particles)
            setCollisionTimes(p1);

    }

    private void setCollisionTimes(Particle p1) {
        for (Particle p2 : particles) {
            if (!p1.equals(p2)) {
                Collision particleCollision = new Collision(p1, p2);
                Double time = particleCollision.timeCollisionAgainstParticle();
                if (time != Double.POSITIVE_INFINITY) {
                    particleCollisionTimes.putIfAbsent(time, particleCollision);
                }
            }
        }
        Collision verticalCollision = new Collision(p1, false, true);
        Double verticalTime = verticalCollision.timeCollisionAgainstVerticalWall();
        if (verticalTime != Double.POSITIVE_INFINITY) {
            particleCollisionTimes.putIfAbsent(verticalTime, verticalCollision);
        }
        Collision horizontalCollision = new Collision(p1, true, false);
        Double horizontalTime = horizontalCollision.timeCollisionAgainstHorizontalWall();
        if (horizontalTime != Double.POSITIVE_INFINITY) {
            particleCollisionTimes.putIfAbsent(horizontalTime, verticalCollision);
        }
    }

    public void updateCollisionTimes(Particle p1, Particle p2, Double time) {
        for (Double d : particleCollisionTimes.keySet()) {
            if (d > time) {
                Collision c = particleCollisionTimes.get(d);
                if (c.getP1().equals(p1) || c.getP2().equals(p1)) {
                    particleCollisionTimes.remove(d);
                }
                if (c.getP1().equals(p2) || c.getP2().equals(p2)) {
                    particleCollisionTimes.remove(d);
                }
            }
        }
        setCollisionTimes(p1);
        if (p2 != null) {
            setCollisionTimes(p2);
        }
    }

    // 1- Calculo todas las colisiones posibles
    // 2- Sucede colision
    // 3- descarto todas las colisiones que ya tenian
    // 4- Sucede una colision -> recalculo todas las colisiones posibles para ambas
    // particulas

    public Double getFirstCollisionTime() {
        Optional<Double> time = particleCollisionTimes.keySet().stream().findFirst();
        return time.orElse(Double.POSITIVE_INFINITY);
    }

    public void executeCollisions(Double time) {
        Collision c = particleCollisionTimes.get(time);
        Particle p1 = c.getP1();
        Particle p2 = c.getP2();
        if (c.isHorizontalWall()) {
            p1.collisionAgainstHorizontalWall(time);
            updateCollisionTimes(p1, p2, time);
        } else if (c.isVerticalWall()) {
            if (!p1.collisionAgainstVerticalWall(time, (width - L) / 2))
                particlesB++;
            updateCollisionTimes(p1, p2, time);
        } else {
            p1.collisionAgainstParticle(p2, time);
            p2.collisionAgainstParticle(p1, time);
            updateCollisionTimes(p1, p2, time);
        }
        particleCollisionTimes.remove(time);
    }

    public void moveParticles(double time) {
        particles.forEach(p -> {
            p.updatePosition(time);
        });
    }

//    public Collision getNextCollision() {
//        Optional<Collision> nextCollision = particleCollisionTimes.get(getFirstCollisionTime()).stream().findFirst();
//        return nextCollision.orElse(null);
//    }
}
