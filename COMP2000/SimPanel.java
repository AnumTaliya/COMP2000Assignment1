import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

public class SimPanel extends JPanel {
    private final World world;

    public SimPanel(World world) {
        this.world = world;
        setBackground(new Color(30, 30, 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Safe zone: predators cannot enter this rectangle.
        g.setColor(new Color(80, 160, 255, 40));
        g.fillRect((int) world.getZoneX(), (int) world.getZoneY(),
                   (int) world.getZoneWidth(), (int) world.getZoneHeight());
        g.setColor(new Color(80, 160, 255));
        g.drawRect((int) world.getZoneX(), (int) world.getZoneY(),
                   (int) world.getZoneWidth(), (int) world.getZoneHeight());
        g.drawString("Safe zone", (int) world.getZoneX() + 6, (int) world.getZoneY() + 14);

        for (Entity e : world.allEntities()) {
            if (e.isAlive()) {
                e.draw(g);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Hawks: " + world.getHawks().size(), 10, 15);
        g.drawString("Foxes: " + world.getFoxes().size(), 10, 30);
        g.drawString("Rabbits: " + world.getRabbits().size(), 10, 45);
        g.drawString("Mice: " + world.getMice().size(), 10, 60);
        g.drawString("Food: " + world.getFood().size(), 10, 75);
    }
}
