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
        ArtifactBean b2 = new ArtifactBean("2", 80d, "yellow");
        ArtifactBean b3 = new ArtifactBean("3", 70d, "red");
        ArtifactBean b4 = new ArtifactBean("4", 60d, "cyan");
        ArtifactBean b5 = new ArtifactBean("5", 100d, "orange");
        ArtifactBean b6 = new ArtifactBean("6", 50d, "pink");
        ArtifactBean b7 = new ArtifactBean("7", 140d, "11AAFF");
        ArtifactBean b8 = new ArtifactBean("8", 75d, "AA11CC");
        List<ArtifactBean> data = Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b8);

        Rectangle canvas = new Rectangle(10, 10, 320, 240);
        MetricsGetter<ArtifactBean> metrics = new NodeMetricsGetter<ArtifactBean>();
        MetricsService service = new MetricsServiceImpl();
        List<RectangleColoredNode<ArtifactBean>> mapData = (List<RectangleColoredNode<ArtifactBean>>) service.layout(
                data, canvas, metrics);
        service.populateColor(mapData, "777777", metrics);
        zoom(mapData, 3.);

        return mapData;
    }

    private static void zoom(List<RectangleColoredNode<ArtifactBean>> mapData, double zoomFactor) {
        for (RectangleColoredNode<ArtifactBean> node : mapData) {
            Rectangle bound = node.getBound();
            bound.x = zoomFactor * bound.x;
            bound.y = zoomFactor * bound.y;
            bound.width = zoomFactor * bound.width;
            bound.height = zoomFactor * bound.height;
            // node.setBound(bound);
        }
    }

    private static void drawTreeMap(Graphics g, List<RectangleColoredNode<ArtifactBean>> mapData) {
        Graphics2D g2d = (Graphics2D) g;
        for (RectangleColoredNode<ArtifactBean> item : mapData) {
            if (item.getBound() != null) {
                Rectangle rect = item.getBound();
                Color color = ColorUtil.fromDensityCode(item.getColor());
                // DrawUtil.drawPlainRect(g2d, rect, color);
                DrawUtil.fillRect(g2d, rect, color);
                String text = item.getObject().getName() + " ("
                        + new NodeMetricsGetter<ArtifactBean>().getSizeValue(item.getObject()) + ")";
                int strWidth = g.getFontMetrics().stringWidth(text);
                int strHeight = g.getFontMetrics().getHeight();
                int xPadding = (int) ((rect.width - strWidth) > 0 ? (rect.width - strWidth)/2 : 1);
                int yPadding = (int) ((rect.height - strHeight) > 0 ? (rect.height + strHeight)/ 2 : strHeight - 1);
                DrawUtil.drawString(g2d, text, (int) rect.x + xPadding, (int) rect.y + yPadding, Color.darkGray.darker());
            }
        }
    }

    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            System.out.println("[WARN] Minor exception: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                    saveToFile();
                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Sample treemap");
        frame.add(new TreeMapSample());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocation(0, 0);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void saveToFile() {
        BufferedImage img = new BufferedImage(400, 350, BufferedImage.TYPE_INT_ARGB);

        Graphics offscreen = img.createGraphics();
        offscreen.setColor(Color.white);
        offscreen.fillRect(0, 0, 400, 350);
        drawTreeMap(offscreen, createMockData());

        String filename = "/TreeMapLayout.png"; // "/TreeMapLayout.jpg"
        DrawUtil.saveImageFile(filename, img);
    }
}
