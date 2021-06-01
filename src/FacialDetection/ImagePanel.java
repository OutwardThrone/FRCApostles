import org.opencv.core.Rect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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


                BufferedImage dst = new BufferedImage(r.width,r.height,BufferedImage.TYPE_INT_ARGB);
                dst.getGraphics().drawImage(img, 0,0,r.width,r.height,r.x,r.y,r.x+r.width,r.y+r.height,null);
                //the filepath that the saved face is stored to on the local drive
                double scaleRatio = r.height/224;
                int newWidth  = (int)(r.width/scaleRatio);
                Image resizedImg = dst.getScaledInstance(224,newWidth, Image.SCALE_SMOOTH);
                BufferedImage resizedBImg = new BufferedImage(224, newWidth,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedBImg.createGraphics();
                g2d.drawImage(resizedImg,0,0,null);
                g2d.dispose();

                File f = new File("C:\\Users\\myPC\\Documents\\saved_face.jpg");

                ImageIO.write(resizedBImg, "jpg", f);
            }
        } catch(NullPointerException | IOException npe) { }
    }
}
