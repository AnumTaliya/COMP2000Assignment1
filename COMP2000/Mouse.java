import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Mouse extends Prey {
    private double reproduceCooldown = 0;
    private static final double CONTACT_DISTANCE = 10;
    private static final double COOLDOWN_TICKS = 150;
    private static final double MIN_HEALTH_TO_REPRODUCE = 15;

    public Mouse(double x, double y) {
        super(x, y, 40, 70, 20);
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

        List<Mouse> mice = world.getMice();
        for (Mouse other : mice) {
            if (other == this || !other.isAlive()) continue;
            if (other.reproduceCooldown > 0) continue;
            if (other.health < MIN_HEALTH_TO_REPRODUCE) continue;

            if (distanceTo(other) <= CONTACT_DISTANCE) {
                try {
                    world.spawnMouseNear(this);
                    this.reproduceCooldown = COOLDOWN_TICKS;
                    other.reproduceCooldown = COOLDOWN_TICKS;
                } catch (SpawnException e) {
                    System.out.println("Reproduction skipped: " + e.getMessage());
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
        g.fillOval(x - 6, y - 4, 14, 10);
        g.fillOval(x + 2, y - 8, 5, 5);
        g.drawLine(x - 6, y + 2, x - 16, y + 6);
    }

    @Override
    public Color getColor() {
        return Color.GRAY;
    }
}