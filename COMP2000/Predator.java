public abstract class Predator extends Animal {
    protected double eatDistance = 8;

    public Predator(double x, double y, double health, double speed, double visionRadius) {
        super(x, y, health, speed, visionRadius);
    }

    @Override
    protected boolean canEnterSafeZone() {
        return false;
    }

    protected boolean tryEat(Prey prey) {
        if (prey != null && prey.isAlive() && distanceTo(prey) <= eatDistance) {
            prey.kill();
            health = Math.min(health + prey.getNutritionValue(), 100);
            return true;
        }
        return false;
    }
}