import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;
    private Rect[] allR;
    private double[] conf;

    public void uploadImage(Image img) {
        this.img = img;
        this.repaint();
    }

    public void uploadRect(Rect[] allR) {
        this.allR = allR;
    }

    public void uploadConfidence(double[] conf) {
        this.conf = conf;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img,0,0, null);
        g.setColor(Color.CYAN);
        try {
            for (int i = 0, j = 0; i < allR.length && j < conf.length; i++, j++) {
                double d = conf[j];
                Rect r = allR[i];
                if(d > 1.5) {
                    g.drawString("Confidence " + d, r.x, r.y + r.height + 10);
                    g.drawRect(r.x, r.y, r.width, r.height);
                }
            }
        } catch(NullPointerException npe) {
            System.out.println("empty rect");
        }
    }
}
