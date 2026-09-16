<<<<<<< Updated upstream
=======
import java.awt.Color;
import java.awt.Graphics;

public abstract class Entity {
    private double x, y;
    private boolean alive = true;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }

    protected void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Entity other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public abstract void update(World world);
    public abstract void draw(Graphics g);
    public abstract Color getColor();

    @Override
    public String toString() {
        return String.format("%s at (%.0f, %.0f)", getClass().getSimpleName(), x, y);
    }
}
>>>>>>> Stashed changes
