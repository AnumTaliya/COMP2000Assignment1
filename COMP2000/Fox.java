import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Fox extends Predator {
    private double reproduceCooldown = 0;
    private static final double CONTACT_DISTANCE = 12;
    private static final double COOLDOWN_TICKS = 400;
    private static final double MIN_HEALTH_TO_REPRODUCE = 70;

    public Fox(double x, double y) {
        super(x, y, 100, 2.2, 90);
    }

    @Override
    protected void act(World world) {
        if (reproduceCooldown > 0) {
            reproduceCooldown--;
        }

        List<Prey> targets = new ArrayList<>();
        targets.addAll(world.getRabbits());
        targets.addAll(world.getMice());
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

        List<Fox> foxes = world.getFoxes();
        for (Fox other : foxes) {
            if (other == this || !other.isAlive()) continue;
            if (other.reproduceCooldown > 0) continue;
            if (other.health < MIN_HEALTH_TO_REPRODUCE) continue;

            if (distanceTo(other) <= CONTACT_DISTANCE) {
                try {
                    world.spawnFoxNear(this);
                    this.reproduceCooldown = COOLDOWN_TICKS;
                    other.reproduceCooldown = COOLDOWN_TICKS;
                } catch (SpawnException e) {
                    System.out.println("Fox reproduction skipped: " + e.getMessage());
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
        g.fillOval(x - 6, y - 8, 14, 16);
        g.fillOval(x + 2, y - 14, 10, 10);
        g.fillOval(x - 14, y - 2, 12, 12);

        g.fillPolygon(new int[]{x + 3, x + 6, x + 9}, new int[]{y - 14, y - 20, y - 14}, 3);
        g.fillPolygon(new int[]{x + 7, x + 10, x + 13}, new int[]{y - 14, y - 20, y - 14}, 3);

        g.setColor(Color.WHITE);
        g.fillOval(x - 8, y + 4, 6, 6);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }
}