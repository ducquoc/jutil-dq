package vn.ducquoc.visual.heatmap.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RectanglesSample extends JPanel {

    private static final long serialVersionUID = 1L;

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        //
        boolean Contained;

        Rectangle2D Shape1 = new Rectangle2D.Double();
        Shape1.setRect(20., 20., 100., 50.);
        Rectangle2D Shape2 = new Rectangle2D.Double(140., 20., 100., 50.);
        Rectangle2D Shape3 = new Rectangle2D.Float();
        Shape3.setRect(260., 20., 100., 50.);
        Rectangle2D Shape4 = new Rectangle2D.Float(380.F, 20.F, 100.F, 50.F);

        g2d.draw(Shape1);
        Contained = Shape1.contains(30., 30.);
        g2d.drawString("contains = " + Contained, 20, 90);

        g2d.fill(Shape2);
        Contained = Shape2.contains(160., 10., 30., 40.);
        g2d.drawString("contains = " + Contained, 140, 90);

        g2d.draw(Shape3);
        Contained = Shape3.intersects(280., 10., 30., 50.);
        g2d.drawString("intersects = " + Contained, 260, 90);

        g2d.fill(Shape4);
        Contained = Shape4.intersectsLine(400., 10., 490., 70.);
        g2d.drawString("intersectsLine = " + Contained, 380, 90);

        g2d.setColor(Color.blue);
        g2d.drawLine(30 - 10, 30, 30 + 10, 30);
        g2d.drawLine(30, 30 - 10, 30, 30 + 10);
        g2d.drawRect(160, 10, 30, 40);
        g2d.drawRect(280, 10, 30, 50);
        g2d.drawLine(400, 10, 490, 70);

        //
        g2d.setColor(Color.BLUE);

        for (int i = 1; i <= 10; i++) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i * 0.1f));
            g2d.fillRect(50 * i, 120, 40, 40);
        }

        //
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(100, 200, 240, 160, 10, 10);
        g2d.draw(roundedRectangle);
    }

    public static void main(String[] args) {
        final String[] params = new String[args.length];
        System.arraycopy(args, 0, params, 0, args.length);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI(params);
                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }

    public static void createAndShowGUI(String[] params) {

        JFrame frame = new JFrame("Rectangles Sample");
        frame.add(new RectanglesSample());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
