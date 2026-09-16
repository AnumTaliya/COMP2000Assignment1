import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Rabbit extends Prey {
    private double reproduceCooldown = 0;
    private static final double CONTACT_DISTANCE = 10;
    private static final double COOLDOWN_TICKS = 150;
    private static final double MIN_HEALTH_TO_REPRODUCE = 15;

    public Rabbit(double x, double y) {
        super(x, y, 60, 80, 35);
    }

    @Override
    protected void act(World world) {
        updateSpeed();

        if (reproduceCooldown > 0) {
            reproduceCooldown--;
        }

        Predator threat = findNearest(nearbyPredators(world));
        if (threat != null) {
            moveAwayFrom(threat, world);
        } else {
            Food food = findNearest(world.getFood());
            if (food != null) {
                moveToward(food, world);
                tryEatFood(food);
            } else {
                wander(world);
            }
        }

        tryReproduce(world);
    }

    private void tryReproduce(World world) {
        if (reproduceCooldown > 0) return;
        if (health < MIN_HEALTH_TO_REPRODUCE) return;

        List<Rabbit> rabbits = world.getRabbits();
        for (Rabbit other : rabbits) {
            if (other == this || !other.isAlive()) continue;
            if (other.reproduceCooldown > 0) continue;
            if (other.health < MIN_HEALTH_TO_REPRODUCE) continue;

            if (distanceTo(other) <= CONTACT_DISTANCE) {
                try {
                    world.spawnRabbitNear(this);
                    this.reproduceCooldown = COOLDOWN_TICKS;
                    other.reproduceCooldown = COOLDOWN_TICKS;
                } catch (SpawnException e) {
                    System.out.println("Rabbit reproduction skipped: " + e.getMessage());
                }
                break;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        int x = (int) getX();
        int y = (int) getY();

        g.setColor(getColor());
        g.fillOval(x - 5, y - 6, 12, 16);
        g.fillOval(x - 2, y - 14, 9, 9);
        g.fillOval(x - 1, y - 24, 3, 12);
        g.fillOval(x + 4, y - 24, 3, 12);
        g.fillOval(x + 4, y + 6, 4, 4);
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }
}