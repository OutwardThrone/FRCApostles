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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class FacialTesting {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    private static VideoCapture capture = new VideoCapture();
    private static Mat frame = new Mat();
    private static JFrame displayFrame = new JFrame();
    private static ImagePanel ip = new ImagePanel();

    private static Rect[] facesPlace;
    private static double[] conf;

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
    private static int debounceCounter = 0;
    private static int debounceAmount = 10;
    private static HashSet<double[]> summableConf = new HashSet<>();
    private static int mostFaces = 0;
    private static void getFaces(Mat frame) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        long absoluteFaceSize = Math.round(480*0.2);
        MatOfRect faces = new MatOfRect();
        MatOfInt rlv = new MatOfInt();
        MatOfDouble lvw = new MatOfDouble();

        CascadeClassifier cc = new CascadeClassifier("E:\\Apps\\FRCApostles\\src\\FacialDetection\\cascades\\haarcascade_frontalface_default.xml");

        cc.detectMultiScale3(grayFrame, faces, rlv, lvw, 1.1, 3, 0, new Size(absoluteFaceSize, absoluteFaceSize), new Size(), true);
        conf = new double[lvw.rows()];
        for (int i = 0; i < lvw.rows(); i++) {
            for (int j = 0; j < lvw.cols(); j++) {
                conf[i] = lvw.get(i,j)[0];
                ip.uploadConfidence(conf);
                if(conf.length > mostFaces) {
                    mostFaces = conf.length;
                }
            }
        }
        Rect[] facesPlace = faces.toArray();
        ip.uploadRect(facesPlace);
        if(debounceCounter < debounceAmount) {
            summableConf.add(conf);
            debounceCounter++;
        } else {
            //System.out.println("Most faces: " + mostFaces);
            double[] finalConf = new double[mostFaces];
            debounceCounter = 0;
            for(double[] darr : summableConf) {
                if(darr!=null && darr.length>0 && mostFaces>0) {
                    for (int i = 0; i < darr.length; i++) {
                        finalConf[i] += darr[i];
                    }
                }
            }
            for (int i = 0; i < finalConf.length; i++) {
                finalConf[i] = finalConf[i]/debounceAmount;
            }
            summableConf.clear();
            mostFaces = 0;
            System.out.println(Arrays.toString(finalConf));
            for(Rect r : facesPlace) {
                System.out.print(r + ", ");
            }
            System.out.println();
        }
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
