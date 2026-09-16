import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        int width = 800, height = 600;
        World world = new World(width, height);

        for (int i = 0; i < 2; i++) {
            double[] p = randomPointOutsideZone(world);
            world.addHawk(new Hawk(p[0], p[1]));
        }
        for (int i = 0; i < 3; i++) {
            double[] p = randomPointOutsideZone(world);
            world.addFox(new Fox(p[0], p[1]));
        }
        for (int i = 0; i < 15; i++) world.addRabbit(new Rabbit(Math.random() * width, Math.random() * height));
        for (int i = 0; i < 20; i++) world.addMouse(new Mouse(Math.random() * width, Math.random() * height));
        for (int i = 0; i < 30; i++) world.addFood(new Food(Math.random() * width, Math.random() * height));

        SimPanel panel = new SimPanel(world);
        panel.setPreferredSize(new Dimension(width, height));

        JFrame frame = new JFrame("Predator and Prey Simulation - COMP2000");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Timer timer = new Timer(50, e -> {
            world.update();
            panel.repaint();
        });
        timer.start();
    }

    private static double[] randomPointOutsideZone(World world) {
        double x, y;
        do {
            x = Math.random() * world.getWidth();
            y = Math.random() * world.getHeight();
        } while (world.isInSafeZone(x, y));
        return new double[]{x, y};
    }
}