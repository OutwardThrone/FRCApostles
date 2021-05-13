import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;
    private Rect[] allR;

    public void uploadImage(Image img) {
        this.img = img;
        this.repaint();
    }

    public void uploadRect(Rect[] allR) {
        this.allR = allR;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img,0,0, null);
        g.setColor(Color.CYAN);
        try {
            for (Rect r : allR) {
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        } catch(NullPointerException npe) {
            System.out.println("empty rect");
        }
    }
}
