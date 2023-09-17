import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Container {

    private final static double width = 0.09;

    private final double L;

    private int particlesA;

    private int particlesB;

    private Set<Particle> particles;

    private TreeSet<Collision> particleCollisionTimes;

    public Container(double L, Set<Particle> particles) {
        this.L = L;

        this.particles = particles;

        this.particlesA = particles.size();
        this.particlesB = 0;

        this.particleCollisionTimes = new TreeSet<> ();
    }

    //For each particle, adds the first collision thats going to happen
    private void setCollisionTimes(Particle p1, Double currentTime) {
        Collision newCollision = null;
        for (Particle p2 : particles) {
            if (!p1.equals(p2)) {
                double time = p1.timeCollisionAgainstParticle(p2);
                if (time != Double.POSITIVE_INFINITY && time > 0) {
                    newCollision = new Collision(p1, p2, CollisionType.PARTICLE, time);
                }
            }
        }
        Double verticalTime = p1.timeCollisionAgainstVerticalWall(width, L);
        if (verticalTime != Double.POSITIVE_INFINITY && verticalTime > 0) {
            if (newCollision == null || newCollision.getTime() > verticalTime){
                newCollision = new Collision(p1, null, CollisionType.VERTICAL_WALL, verticalTime);
            }
        }

        Double horizontalTime = p1.timeCollisionAgainstHorizontalWall(width, L);
        if (horizontalTime != Double.POSITIVE_INFINITY && horizontalTime > 0) {
            if (newCollision == null || newCollision.getTime() > horizontalTime){
                newCollision = new Collision(p1, null, CollisionType.HORIZONTAL_WALL, horizontalTime);
            }
        }
        if (newCollision != null) {
            particleCollisionTimes.add(newCollision);
        }
    }

    // 1- Calculo todas las colisiones posibles
    // 2- Sucede colision
    // 3- descarto todas las colisiones que ya tenian
    // 4- Sucede una colision -> recalculo todas las colisiones posibles para ambas
    // particulas


    public Double executeCollisions(Double currentTime) {
        particleCollisionTimes = new TreeSet<> ();
        for (Particle p : particles){
            setCollisionTimes(p, currentTime);
        }
        if (particleCollisionTimes.isEmpty()) {
            throw new IllegalStateException("No particle collisions found");
        }
        Collision newCollision = particleCollisionTimes.pollFirst();
        Particle p1 = newCollision.getP1();
        Particle p2 = newCollision.getP2();

        for (Particle p : particles) {
            p.updatePosition(newCollision.getTime());
        }

        if (p2 == null) {
            switch (newCollision.getCollisionType()) {
                case HORIZONTAL_WALL: {
                    p1.collisionAgainstHorizontalWall();
                }
                case VERTICAL_WALL: {
                    int res = p1.collisionAgainstVerticalWall(newCollision.getTime(), L, width);
                    //passes from A container to B container
                    if (res == 1) {
                        particlesB++;
                        particlesA--;
                    }
                    //passes from B container to A container
                    else if (res == 2) {
                        particlesB--;
                        particlesA++;
                    }
                }
            }
        }
        else
            p1.collisionAgainstParticle(p2);

        TreeSet<Collision> updatedParticleCollisionTimes = new TreeSet<>();

        for (Collision aux : particleCollisionTimes) {
            if (!aux.getP1().equals(aux.getP1()) && (aux.getP2() == null || !aux.getP2().equals(aux.getP1())) &&
                    !aux.getP1().equals(aux.getP2()) && (aux.getP2() == null || !aux.getP2().equals(aux.getP2()))) {
                updatedParticleCollisionTimes.add(aux);
            }
        }

        // Replace the original particleCollisionTimes with the updated set
        particleCollisionTimes = updatedParticleCollisionTimes;

        return newCollision.getTime();
    }

    public Set<Particle> getParticles() {
        return particles;
    }

}
