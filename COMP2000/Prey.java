import java.util.ArrayList;
import java.util.List;

public abstract class Prey extends Animal implements Edible {
    protected static final double HUNGRY_SPEED = 1.0;
    protected static final double FED_SPEED = 2.0;
    protected static final int FED_DURATION = 80;

    protected double eatDistance = 6;
    protected double nutritionValue;
    protected int fedTicks = 0;

    public Prey(double x, double y, double health, double visionRadius, double nutritionValue) {
        super(x, y, health, HUNGRY_SPEED, visionRadius);
        this.nutritionValue = nutritionValue;
    }

    @Override
    public double getNutritionValue() {
        return nutritionValue;
    }

    protected void updateSpeed() {
        if (fedTicks > 0) {
            fedTicks--;
            speed = FED_SPEED;
        } else {
            speed = HUNGRY_SPEED;
        }
    }

    protected List<Predator> nearbyPredators(World world) {
        List<Predator> predators = new ArrayList<>();
        predators.addAll(world.getHawks());
        predators.addAll(world.getFoxes());
        return predators;
    }

    protected boolean tryEatFood(Food food) {
        if (food != null && food.isAlive() && distanceTo(food) <= eatDistance) {
            food.kill();
            health = Math.min(health + food.getNutritionValue(), 100);
            fedTicks = FED_DURATION;
            return true;
        }
        return false;
    }
}