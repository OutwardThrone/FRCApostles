import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FacialTesting {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    private static VideoCapture capture = new VideoCapture();
    private static Mat frame = new Mat();
    private static JFrame displayFrame = new JFrame();
    private static ImagePanel ip = new ImagePanel();
    public static void main (String[] args) {
        capture.open(0);

        if(!capture.isOpened()) {
            System.out.println("Camera failed!");
        } else {
            File f = new File("img.jpg");
            displayFrame.add(ip);
            displayFrame.pack();
            displayFrame.setBounds(0,0, 640, 480);
            displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            displayFrame.setVisible(true);
            Timer t = new Timer(25, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    capture.grab();
                    capture.retrieve(frame, 0);
                    Core.flip(frame, frame, 1);
                    getFaces(frame);
                    showWindow(frame, ip, f);
                }
            });
            t.start();
        }
        System.out.println("Type \"quit\" to quit");
        Scanner sc = new Scanner(System.in);
        if(sc.nextLine().equals("quit")) {
            capture.release();
            displayFrame.dispose();
            System.exit(0);
        }
    }

    private static void getFaces(Mat frame) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        long absoluteFaceSize = Math.round(480*0.2);
        MatOfRect faces = new MatOfRect();
        CascadeClassifier cc = new CascadeClassifier("cascades\\haarcascade_frontalface_default.xml");
        cc.detectMultiScale(grayFrame, faces, 1.1, 4, 0, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        Rect[] facesPlace = faces.toArray();
        ip.uploadRect(facesPlace);
    }

    private static void showWindow(Mat image, ImagePanel ip, File f) {
        Imgcodecs.imwrite("img.jpg", image);
        Image img = null;
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ip.uploadImage(img);
    }
}