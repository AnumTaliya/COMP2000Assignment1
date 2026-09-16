import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Hawk extends Predator {
    private double reproduceCooldown = 0;
    private static final double CONTACT_DISTANCE = 12;
    private static final double COOLDOWN_TICKS = 400;
    private static final double MIN_HEALTH_TO_REPRODUCE = 70;

    public Hawk(double x, double y) {
        super(x, y, 100, 3.0, 120);
    }

    @Override
    protected void act(World world) {
        if (reproduceCooldown > 0) {
            reproduceCooldown--;
        }

        List<Prey> targets = new ArrayList<>();
        targets.addAll(world.getMice());
        targets.addAll(world.getRabbits());
        Prey target = findNearest(targets);
        if (target != null) {
            moveToward(target, world);
            tryEat(target);
        } else {
            wander(world);
        }

        tryReproduce(world);
    }

    private void tryReproduce(World world) {
        if (reproduceCooldown > 0) return;
        if (health < MIN_HEALTH_TO_REPRODUCE) return;

        List<Hawk> hawks = world.getHawks();
        for (Hawk other : hawks) {
            if (other == this || !other.isAlive()) continue;
            if (other.reproduceCooldown > 0) continue;
            if (other.health < MIN_HEALTH_TO_REPRODUCE) continue;

            if (distanceTo(other) <= CONTACT_DISTANCE) {
                try {
                    world.spawnHawkNear(this);
                    this.reproduceCooldown = COOLDOWN_TICKS;
                    other.reproduceCooldown = COOLDOWN_TICKS;
                } catch (SpawnException e) {
                    System.out.println("Hawk reproduction skipped: " + e.getMessage());
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
        g.fillOval(x - 3, y - 5, 6, 10);
        g.fillOval(x - 2, y - 8, 4, 4);

        g.fillPolygon(new int[]{x - 3, x - 12, x - 2}, new int[]{y - 1, y - 4, y + 3}, 3);
        g.fillPolygon(new int[]{x + 3, x + 12, x + 2}, new int[]{y - 1, y - 4, y + 3}, 3);
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }
}