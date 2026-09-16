import java.util.ArrayList;
import java.util.List;

public class World {
    private double width, height;

    private List<Hawk> hawks = new ArrayList<>();
    private List<Fox> foxes = new ArrayList<>();
    private List<Rabbit> rabbits = new ArrayList<>();
    private List<Mouse> mice = new ArrayList<>();
    private List<Food> food = new ArrayList<>();

    private double zoneX, zoneY, zoneWidth, zoneHeight;

    private static final int FOOD_CAP = 40;
    private static final double FOOD_SPAWN_CHANCE = 0.08;

    public World(double width, double height) {
        this.width = width;
        this.height = height;

        zoneWidth = width * 0.25;
        zoneHeight = height * 0.25;
        zoneX = width - zoneWidth - 20;
        zoneY = height - zoneHeight - 20;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getZoneX() { return zoneX; }
    public double getZoneY() { return zoneY; }
    public double getZoneWidth() { return zoneWidth; }
    public double getZoneHeight() { return zoneHeight; }

    public boolean isInSafeZone(double x, double y) {
        return x >= zoneX && x <= zoneX + zoneWidth && y >= zoneY && y <= zoneY + zoneHeight;
    }

    public List<Hawk> getHawks() { return hawks; }
    public List<Fox> getFoxes() { return foxes; }
    public List<Rabbit> getRabbits() { return rabbits; }
    public List<Mouse> getMice() { return mice; }
    public List<Food> getFood() { return food; }

    public void addHawk(Hawk h) { hawks.add(h); }
    public void addFox(Fox f) { foxes.add(f); }
    public void addRabbit(Rabbit r) { rabbits.add(r); }
    public void addMouse(Mouse m) { mice.add(m); }
    public void addFood(Food f) { food.add(f); }

    public List<Entity> allEntities() {
        List<Entity> all = new ArrayList<>();
        all.addAll(hawks);
        all.addAll(foxes);
        all.addAll(rabbits);
        all.addAll(mice);
        all.addAll(food);
        return all;
    }

    public void update() {
        for (Entity e : allEntities()) {
            e.update(this);
        }

        removeDead(hawks);
        removeDead(foxes);
        removeDead(rabbits);
        removeDead(mice);
        removeDead(food);

        spawnFoodIfNeeded();
    }

    private void removeDead(List<? extends Entity> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!list.get(i).isAlive()) {
                list.remove(i);
            }
        }
    }

    private void spawnFoodIfNeeded() {
        if (food.size() >= FOOD_CAP) return;
        if (Math.random() < FOOD_SPAWN_CHANCE) {
            double x = Math.random() * width;
            double y = Math.random() * height;
            food.add(new Food(x, y));
        }
    }

    public void spawnMouseNear(Mouse parent) throws SpawnException {
        double[] p = nearbyPoint(parent);
        mice.add(new Mouse(p[0], p[1]));
    }

    public void spawnRabbitNear(Rabbit parent) throws SpawnException {
        double[] p = nearbyPoint(parent);
        rabbits.add(new Rabbit(p[0], p[1]));
    }

    public void spawnHawkNear(Hawk parent) throws SpawnException {
        double[] p = nearbyPoint(parent);
        hawks.add(new Hawk(p[0], p[1]));
    }

    public void spawnFoxNear(Fox parent) throws SpawnException {
        double[] p = nearbyPoint(parent);
        foxes.add(new Fox(p[0], p[1]));
    }

    private double[] nearbyPoint(Entity parent) throws SpawnException {
        double x = parent.getX() + (Math.random() - 0.5) * 20;
        double y = parent.getY() + (Math.random() - 0.5) * 20;

        if (x < 0 || x > width || y < 0 || y > height) {
            throw new SpawnException("No valid spot near " + parent);
        }
        return new double[]{x, y};
    }
}