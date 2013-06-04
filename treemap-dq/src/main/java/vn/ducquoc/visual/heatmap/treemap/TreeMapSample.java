package vn.ducquoc.visual.heatmap.treemap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import vn.ducquoc.visual.heatmap.ColorUtil;
import vn.ducquoc.visual.heatmap.DrawUtil;
import vn.ducquoc.visual.heatmap.Rectangle;

@SuppressWarnings("unchecked")
public class TreeMapSample extends JPanel {

    private static final long serialVersionUID = 1L;

    public void paint(Graphics g) {
        super.paint(g);

        drawTreeMap(g, createMockData());
    }

    private static List<RectangleColoredNode<ArtifactBean>> createMockData() {
        ArtifactBean b1 = new ArtifactBean("1", 40d, "green");
        ArtifactBean b2 = new ArtifactBean("2", 20d, "yellow");
        ArtifactBean b3 = new ArtifactBean("3", 70d, "red");
        ArtifactBean b4 = new ArtifactBean("4", 30d, "blue");
        ArtifactBean b5 = new ArtifactBean("5", 100d, "orange");
        ArtifactBean b6 = new ArtifactBean("6", 10d, "pink");
        ArtifactBean b7 = new ArtifactBean("7", 140d, "11AAFF");
        ArtifactBean b8 = new ArtifactBean("8", 75d, "AA11CC");
        List<ArtifactBean> data = Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b8);

        Rectangle canvas = new Rectangle(0, 0, 320, 240);
        MetricsGetter<ArtifactBean> metrics = new NodeMetricsGetter<ArtifactBean>();
        MetricsService service = new MetricsServiceImpl();
        List<RectangleColoredNode<ArtifactBean>> sliceDice = (List<RectangleColoredNode<ArtifactBean>>) service.layout(
                data, canvas, metrics);
        service.populateColor(sliceDice, "777777", metrics);

        return sliceDice;
    }

    private static void drawTreeMap(Graphics g, List<RectangleColoredNode<ArtifactBean>> mapData) {
        Graphics2D g2d = (Graphics2D) g;
        for (RectangleColoredNode<ArtifactBean> item : mapData) {
            if (item.getBound() != null) {
                // DrawUtil.drawPlainRect(g2d, item.getBound(),
                // ColorUtil.fromDensityCode(item.getColor()));
                DrawUtil.fillRect(g2d, item.getBound(), ColorUtil.fromDensityCode(item.getColor()));
            }
        }
    }

    public static void main(String[] args) {
        final String[] params = new String[args.length];
        System.arraycopy(args, 0, params, 0, args.length);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI(params);
                    saveToFile();
                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }

    public static void createAndShowGUI(String[] params) {
        JFrame frame = new JFrame("Sample treemap");
        frame.add(new TreeMapSample());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocation(0, 0);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void saveToFile() {
        BufferedImage img = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);

        Graphics offscreen = img.createGraphics();
        offscreen.setColor(Color.white);
        offscreen.fillRect(0, 0, 400, 300);
        drawTreeMap(offscreen, createMockData());

        String filename = "/DucTest.png"; // "/DucTest.jpg"
        DrawUtil.saveImageFile(filename, img);
    }
}
