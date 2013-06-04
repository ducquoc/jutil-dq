package vn.ducquoc.visual.heatmap.swing;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import vn.ducquoc.visual.heatmap.Gradient;

class HeatMapFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    HeatMapPanel panel;

    public HeatMapFrame() throws Exception {
        super("Heat Map Frame");
        double[][] data = HeatMapPanel.generateRampTestData();
        boolean useGraphicsYAxis = true;

        // you can use a pre-defined gradient:
        panel = new HeatMapPanel(data, useGraphicsYAxis, Gradient.GRADIENT_BLUE_TO_RED);

        // or you can also make a custom gradient:
        Color[] gradientColors = new Color[] { Color.blue, Color.green, Color.yellow };
        Color[] customGradient = Gradient.createMultiGradient(gradientColors, 500);
        panel.updateGradient(customGradient);

        // set miscelaneous settings
        panel.setDrawLegend(true);

        panel.setTitle("Height (m)");
        panel.setDrawTitle(true);

        panel.setXAxisTitle("X-Distance (m)");
        panel.setDrawXAxisTitle(true);

        panel.setYAxisTitle("Y-Distance (m)");
        panel.setDrawYAxisTitle(true);

        panel.setCoordinateBounds(0, 6.28, 0, 6.28);
        panel.setDrawXTicks(true);
        panel.setDrawYTicks(true);

        panel.setColorForeground(Color.black);
        panel.setColorBackground(Color.white);

        this.getContentPane().add(panel);
    }

    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception {
        HeatMapFrame hmf = new HeatMapFrame();
        hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hmf.setSize(500, 500);
        hmf.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }
}
