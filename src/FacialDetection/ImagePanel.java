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
        Rect r = new Rect();
        double d = 0;
        try {
            for (int i = 0; i < allR.length; i++) {
                r = allR[i];
                g.drawRect(r.x, r.y, r.width, r.height);
                d = conf[i];
                g.drawString("Confidence " + String.format("%.2f", d), r.x, r.y + r.height + 10);
            }
        } catch(NullPointerException npe) { }
    }
}
