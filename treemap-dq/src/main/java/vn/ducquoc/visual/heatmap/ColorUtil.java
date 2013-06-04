package vn.ducquoc.visual.heatmap;

import java.awt.Color;

public class ColorUtil {

    private ColorUtil() {
    }

    public static Color fromDensityCode(String densityCode) {
        Color result = fromColorCode(densityCode);
        if (result == null) {
            int[] rgbValues = makeRGB(densityCode);
            result = new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
        }

        return result;
    }

    public static Color fromColorCode(String colorCode) {
        if ("black".equalsIgnoreCase(colorCode))
            return Color.black;
        if ("white".equalsIgnoreCase(colorCode))
            return Color.white;
        if ("red".equalsIgnoreCase(colorCode))
            return Color.red;
        if ("green".equalsIgnoreCase(colorCode))
            return Color.green;
        if ("blue".equalsIgnoreCase(colorCode))
            return Color.blue;
        if ("yellow".equalsIgnoreCase(colorCode))
            return Color.yellow;
        if ("orange".equalsIgnoreCase(colorCode))
            return Color.orange;
        if ("cyan".equalsIgnoreCase(colorCode))
            return Color.cyan;
        if ("magenta".equalsIgnoreCase(colorCode))
            return Color.magenta;
        if ("pink".equalsIgnoreCase(colorCode))
            return Color.pink;
        if ("gray".equalsIgnoreCase(colorCode))
            return Color.gray;
        if ("lightGray".equalsIgnoreCase(colorCode))
            return Color.lightGray;
        if ("darkGray".equalsIgnoreCase(colorCode))
            return Color.darkGray;

        return null;
    }

    public static Double toDouble(Color color) {
        return new Double(new Integer(color.getRGB()));
    }

    public static Color fromDouble(Double value) {
        int rgb = value.intValue();
        int b = rgb & 0x000000ff;
        int g = (rgb & 0x0000ff00) >> 8;
        int r = (rgb & 0x00ff0000) >> 16;
        int a = (rgb & 0xff000000) >> 24;
        if (a < 0) {
            a = a + 256; // signed value
        }
        return new Color(r, g, b, a);
    }

    public static String toHexRgb(Color color) {
        int[] rgb = { color.getRed(), color.getGreen(), color.getBlue() };
        return makeHexColor(rgb);
    }

    public static String makeHexColor(int[] rgb) {
        StringBuilder sb = new StringBuilder();
        sb.append(rgb[0] > 15 ? Integer.toHexString(rgb[0]) : "0" + Integer.toHexString(rgb[0]));
        sb.append(rgb[1] > 15 ? Integer.toHexString(rgb[1]) : "0" + Integer.toHexString(rgb[1]));
        sb.append(rgb[2] > 15 ? Integer.toHexString(rgb[2]) : "0" + Integer.toHexString(rgb[2]));
        return sb.toString();
    }

    public static int[] makeRGB(String hexColor) {
        int[] rgb = new int[3];
        String colorString = hexColor.startsWith("#") ? hexColor.substring(1) : hexColor;
        rgb[0] = Integer.parseInt(colorString.substring(0, 2), 16);
        rgb[1] = Integer.parseInt(colorString.substring(2, 4), 16);
        rgb[2] = Integer.parseInt(colorString.substring(4, 6), 16);
        return rgb;
    }

    private static String[] generate(String min, String max, int steps) {
        String[] colors = new String[steps];
        int[] minRGB = makeRGB(min);
        int[] maxRGB = makeRGB(max);
        for (int i = 0; i < steps; i++) {
            int[] rgb = new int[3];
            rgb[0] = minRGB[0] + (maxRGB[0] - minRGB[0]) * i / steps;
            rgb[1] = minRGB[1] + (maxRGB[1] - minRGB[1]) * i / steps;
            rgb[2] = minRGB[2] + (maxRGB[2] - minRGB[2]) * i / steps;
            colors[i] = makeHexColor(rgb);
        }
        return colors;
    }

    public static String[] generateGradientColor(String min, String middle, String max, int steps) {
        // middle color always overlaps
        // if steps is odd (e.g 9), we will generate 1 extra color.
        // if steps is even, we will generate 2 extra color, then remove 1.
        String[] a = generate(min, middle, steps / 2 + 1);
        String[] b = generate(middle, max, steps / 2 + 1);
        String[] colors = new String[steps];
        System.arraycopy(a, 0, colors, 0, a.length);
        int oneOrTwo = (steps + 1) % 2 + 1; // odd:-1, even: -2
        System.arraycopy(b, oneOrTwo, colors, a.length, b.length - oneOrTwo);
        return colors;
    }

}
