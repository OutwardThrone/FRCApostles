package UI;

import javax.swing.*;

public class Main {
    public static int HEIGHT = 600;
    public static int WIDTH = 800;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        AppPanel app = new AppPanel();
        frame.add(app);

    }
}
