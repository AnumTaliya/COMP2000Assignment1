import java.util.List;

public abstract class Animal extends Entity {
    protected double health;
    protected double speed;
    protected double visionRadius;

    public Animal(double x, double y, double health, double speed, double visionRadius) {
        super(x, y);
        this.health = health;
        this.speed = speed;
        this.visionRadius = visionRadius;
    }

    public double getHealth() { return health; }

    protected boolean canEnterSafeZone() {
        return true;
    }

    protected void moveToward(Entity target, World world) {
        moveToward(target.getX(), target.getY(), world);
    }

    protected void moveToward(double targetX, double targetY, World world) {
        double dx = targetX - getX();
        double dy = targetY - getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0.001) {
            double newX = getX() + (dx / dist) * speed;
            double newY = getY() + (dy / dist) * speed;
            move(newX, newY, world);
        }
    }

    protected void moveAwayFrom(Entity threat, World world) {
        moveAwayFrom(threat.getX(), threat.getY(), world);
    }

    protected void moveAwayFrom(double threatX, double threatY, World world) {
        double dx = getX() - threatX;
        double dy = getY() - threatY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0.001) {
            double newX = getX() + (dx / dist) * speed;
            double newY = getY() + (dy / dist) * speed;
            move(newX, newY, world);
        }
    }

    protected void wander(World world) {
        double newX = getX() + (Math.random() - 0.5) * speed;
        double newY = getY() + (Math.random() - 0.5) * speed;
        move(newX, newY, world);
    }

    private void move(double newX, double newY, World world) {
        if (newX < 0) newX = 0;
        if (newX > world.getWidth()) newX = world.getWidth();
        if (newY < 0) newY = 0;
        if (newY > world.getHeight()) newY = world.getHeight();

        if (!canEnterSafeZone() && world.isInSafeZone(newX, newY)) {
            return;
        }

        setPosition(newX, newY);
    }

    protected <T extends Entity> T findNearest(List<T> candidates) {
        T nearest = null;
        double bestDist = Double.MAX_VALUE;
        for (T candidate : candidates) {
            if (!candidate.isAlive()) continue;
            double d = this.distanceTo(candidate);
            if (d < bestDist) {
                bestDist = d;
                nearest = candidate;
            }
        }
        return (bestDist <= visionRadius) ? nearest : null;
    }

    @Override
    public void update(World world) {
        health -= 0.1;
        if (health <= 0) {
            kill();
            return;
        }
        act(world);
    }

    protected abstract void act(World world);

    @Override
    public String toString() {
        return super.toString() + String.format(", health %.0f", health);
    }
}