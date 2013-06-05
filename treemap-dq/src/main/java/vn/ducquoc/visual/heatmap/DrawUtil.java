package vn.ducquoc.visual.heatmap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DrawUtil {

    private DrawUtil() {
    }

    /**
     * Draws a line, using the current color, between the points (xStart,
     * yStart) and (xEnd, yEnd) in this graphics context's coordinate system.
     * 
     * @param g
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     */
    public static void drawLine(Graphics g, int xStart, int yStart, int xEnd, int yEnd) {
        g.drawLine(xStart, yStart, xEnd, yEnd);
    }

    public static void drawString(Graphics g, String text, int x, int y) {
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawString(text, x, y);
        }
    }

    public static void drawString(Graphics g, String text, int x, int y, Color color) {
        final Color oldColor = g.getColor();
        g.setColor(color);

        DrawUtil.drawString(g, text, x, y);

        g.setColor(oldColor);
    }

    public static void drawRect(Graphics g, Rectangle rec) {
        g.drawRect((int) Math.round(rec.x), (int) Math.round(rec.y), (int) Math.round(rec.width),
                (int) Math.round(rec.height));
    }

    public static void fillRect(Graphics g, Rectangle rec) {
        g.fillRect((int) Math.round(rec.x), (int) Math.round(rec.y), (int) Math.round(rec.width),
                (int) Math.round(rec.height));
    }

    public static void fillRect(Graphics g, Rectangle rec, Color color) {
        final Color oldColor = g.getColor();
        g.setColor(color);

        DrawUtil.fillRect(g, rec);

        g.setColor(oldColor);
    }

    public static void drawRoundRect(Graphics g, Rectangle rec) {
        int xStart = (int) Math.round(rec.x);
        int xEnd = (int) Math.round(rec.maxX());
        int yStart = (int) Math.round(rec.y);
        int yEnd = (int) Math.round(rec.maxY());

        DrawUtil.drawLine(g, xStart + 1, yStart, xEnd - 1, yStart);
        DrawUtil.drawLine(g, xStart + 1, yEnd, xEnd - 1, yEnd);
        DrawUtil.drawLine(g, xStart, yStart + 1, xStart, yEnd - 1);
        DrawUtil.drawLine(g, xEnd, yStart + 1, xEnd, yEnd - 1);
    }

    public static void drawRoundRect(Graphics g, Rectangle rec, Color color) {
        final Color oldColor = g.getColor();
        g.setColor(color);

        DrawUtil.drawRoundRect(g, rec);

        g.setColor(oldColor);
    }

    public static void drawPlainRect(Graphics g, Rectangle rec) {
        int xStart = (int) Math.round(rec.x);
        int xEnd = (int) Math.round(rec.maxX());
        int yStart = (int) Math.round(rec.y);
        int yEnd = (int) Math.round(rec.maxY());

        DrawUtil.drawLine(g, xStart, yStart, xEnd - 1, yStart);
        DrawUtil.drawLine(g, xStart, yEnd, xEnd, yEnd - 1);
        DrawUtil.drawLine(g, xStart + 1, yStart, xStart, yEnd);
        DrawUtil.drawLine(g, xEnd, yStart + 1, xEnd, yEnd);
    }

    public static void drawPlainRect(Graphics g, Rectangle rec, Color color) {
        final Color oldColor = g.getColor();
        g.setColor(color);

        DrawUtil.drawPlainRect(g, rec);

        g.setColor(oldColor);
    }

    public static void saveImageFile(String filename, BufferedImage img) {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        // png files
        if (suffix.toLowerCase().equals("png")) {
            try {
                ImageIO.write(img, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // workaround for jpeg (need to change from ARGB to RGB)
        else if (suffix.toLowerCase().equals("jpg")) {
            WritableRaster raster = img.getRaster();
            WritableRaster newRaster;
            newRaster = raster.createWritableChild(0, 0, img.getWidth(), img.getHeight(), 0, 0, new int[] { 0, 1, 2 });
            DirectColorModel cm = (DirectColorModel) img.getColorModel();
            DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(),
                    cm.getBlueMask());
            BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false, null);
            try {
                ImageIO.write(rgbBuffer, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else { // GIF and BMP are not as popular as PNG/JPG
            System.out.println("Invalid image file type: " + suffix);
        }
    }

}
