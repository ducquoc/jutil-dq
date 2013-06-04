package vn.ducquoc.visual.heatmap.treemap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.ducquoc.visual.heatmap.ColorUtil;
import vn.ducquoc.visual.heatmap.Pair;
import vn.ducquoc.visual.heatmap.Rectangle;

@SuppressWarnings({ "unchecked", "unused" })
public class MetricsServiceImpl implements MetricsService {

    private static final String NODATA_COLOR = "000000";

    public <T> void populateColor(List<? extends ColoredNode<T>> data, String colorSeed,
            final MetricsGetter<T> metricsGetter) {

        for (ColoredNode<T> item : data) {
            if (item.getObject() == null) {
                item.setColor(NODATA_COLOR);
                continue;
            }
            double value = metricsGetter.getDensityValue(item.getObject());

            Color color = ColorUtil.fromDouble(value);
            item.setColor(ColorUtil.toHexRgb(color));
        }
    }

    public <T> List<? extends ColoredNode<T>> layout(List<T> data, Rectangle rectCanvas,
            final MetricsGetter<T> metricsGetter) {

        // sorting by size - larger first
        Collections.sort(data, new Comparator<T>() {
            public int compare(T o1, T o2) {
                return metricsGetter.getSizeValue(o2).compareTo(metricsGetter.getSizeValue(o1));
            }
        });

        List<RectangleColoredNode<T>> result = (List<RectangleColoredNode<T>>) sliceAndDiceLayout(data, rectCanvas,
                metricsGetter);

        if (result.size() > 0) {
            adjustBorder(result, rectCanvas);
        }
        return result;
    }

    /**
     * Slice and dice layout algorithm for tree maps. <br/>
     * {@link http://www.academia.edu/372245/
     * Slice_and_Dice_A_Simple_Improved_Approximate_Tiling_Recipe}
     * 
     * @param data
     * @param canvas
     * @param metricsGetter
     * @return sorted list of nodes which has been updated the position
     */
    private <T> List<? extends ColoredNode<T>> sliceAndDiceLayout(List<T> data, Rectangle canvas,
            final MetricsGetter<T> metricsGetter) {
        if (data.size() <= 2) { // special case - it took quite minutes of me
            return sliceLayout(data, canvas, metricsGetter);
        }

        List<RectangleColoredNode<T>> result = new ArrayList<RectangleColoredNode<T>>();
        Double sizeSum = getSizeSum(data, metricsGetter);
        double a = metricsGetter.getSizeValue(data.get(0)) / sizeSum;
        double b = a;
        int mid = 0;

        if (canvas.width > canvas.height) { // landscape canvas
            for (T item : data) {
                mid++;
                double aspect = normAspect(canvas.width, canvas.height, a, b);
                double q = metricsGetter.getSizeValue(item) / sizeSum;
                if (normAspect(canvas.width, canvas.height, a, b + q) > aspect) {
                    break;
                }

                b += q;
            }
            result.addAll((List<? extends RectangleColoredNode<T>>) sliceLayout(data.subList(0, mid), new Rectangle(
                    canvas.x, canvas.y, canvas.width * b, canvas.height), metricsGetter));
            result.addAll((List<? extends RectangleColoredNode<T>>) sliceAndDiceLayout(data.subList(mid, data.size()),
                    new Rectangle(canvas.x + canvas.width * b, canvas.y, canvas.width * (1 - b), canvas.height),
                    metricsGetter));
        } else { // portrait canvas
            for (T item : data) {
                mid++;
                double aspect = normAspect(canvas.height, canvas.width, a, b);
                double q = metricsGetter.getSizeValue(item) / sizeSum;
                if (normAspect(canvas.height, canvas.width, a, b + q) > aspect) {
                    break;
                }
                b += q;
            }
            result.addAll((List<? extends RectangleColoredNode<T>>) sliceLayout(data.subList(0, mid), new Rectangle(
                    canvas.x, canvas.y, canvas.width, canvas.height * b), metricsGetter));
            result.addAll((List<? extends RectangleColoredNode<T>>) sliceAndDiceLayout(data.subList(mid, data.size()),
                    new Rectangle(canvas.x, canvas.y + canvas.height * b, canvas.width, canvas.height * (1 - b)),
                    metricsGetter));
        }
        return result;
    }

    private <T> List<? extends ColoredNode<T>> sliceLayout(List<T> data, Rectangle rect, MetricsGetter<T> metricsGetter) {
        List<RectangleColoredNode<T>> result = new ArrayList<RectangleColoredNode<T>>();
        double total = getSizeSum(data, metricsGetter);
        double a = 0;

        for (T item : data) {
            Rectangle bound = new Rectangle();
            double b = metricsGetter.getSizeValue(item) / total;
            if (rect.width < rect.height) {
                bound.x = rect.x;
                bound.width = rect.width;
                bound.y = rect.y + rect.height * a;
                bound.height = rect.height * b;
            } else {
                bound.x = rect.x + rect.width * a;
                bound.width = rect.width * b;
                bound.y = rect.y;
                bound.height = rect.height;
            }
            result.add(new RectangleColoredNode<T>("", bound, item));
            a += b;

        }
        return result;
    }

    private <T> Double getSizeSum(List<T> data, MetricsGetter<T> metricsGetter) {
        Double sizeSum = 0d;
        for (T item : data) {
            sizeSum += metricsGetter.getSizeValue(item);
        }
        return sizeSum;
    }

    private double aspect(double big, double small, double a, double b) {
        return (big * b) / (small * a / b);
    }

    private double normAspect(double big, double small, double a, double b) {
        double x = aspect(big, small, a, b);
        if (x < 1)
            return 1 / x;
        return x;
    }

    private <T> void adjustBorder(List<RectangleColoredNode<T>> data, Rectangle rect) {
        Pair<Double, Double> minWidthHeight = getMinWidthHeight(data);
        double totalWidth = rect.width;
        double totalHeight = rect.height;

        for (RectangleColoredNode<T> item : data) {
            Rectangle bound = item.getBound();
            double remainWidth = totalWidth - (bound.x + bound.width);
            double remainHeight = totalHeight - (bound.y + bound.height);

            item.getBound().x = Double.valueOf(bound.x * 100 / totalWidth);
            item.getBound().y = Double.valueOf(bound.y * 100 / totalHeight);

            if ((int) (remainWidth * 100) >= (int) (minWidthHeight.getFirst() * 100)) {
                item.getBound().width = Double.valueOf(bound.width * 100 / totalWidth);
            } else {
                item.getBound().width = Double.valueOf(100.0 - bound.x);
            }

            if ((int) (remainHeight * 100) >= (int) (minWidthHeight.getSecond() * 100)) {
                item.getBound().height = Double.valueOf(bound.height * 100 / totalHeight);
            } else {
                item.getBound().height = Double.valueOf(100.0 - bound.y);
            }
        }
    }

    private <T> Pair<Double, Double> getMinWidthHeight(List<RectangleColoredNode<T>> data) {
        Double minWidth = data.get(0).getBound().width;
        Double minHeight = data.get(0).getBound().height;
        for (RectangleColoredNode<T> item : data) {
            Double width = item.getBound().width;
            if (width < minWidth) {
                minWidth = width;
            }
            Double height = item.getBound().height;
            if (height < minHeight) {
                minHeight = height;
            }
        }
        return new Pair<Double, Double>(minWidth, minHeight);
    }

    //
    // place holders
    //
    /**
     * Squarified layout algorithm for tree maps. <br/>
     * {@link http://hcil2.cs.umd.edu/trs/2001-06/2001-06.html}
     */
    private <T> List<? extends ColoredNode<T>> squarifiedLayout(List<T> data, Rectangle rectCanvas,
            final MetricsGetter<T> metricsGetter) {

        // TODO : squarified layout looks desirable
        return sliceAndDiceLayout(data, rectCanvas, metricsGetter);
    }

    /**
     * Strip layout algorithm for tree maps. <br/>
     * {@link http://hcil2.cs.umd.edu/trs/2001-18/2001-18.html}
     */
    private <T> List<? extends ColoredNode<T>> stripLayout(List<T> data, Rectangle rectCanvas,
            final MetricsGetter<T> metricsGetter) {

        // TODO : strip layout is fun
        return sliceAndDiceLayout(data, rectCanvas, metricsGetter);
    }

    private int findIndexOf(double value, String[] colors) {
        for (int i = 0; i < colors.length; i++) {
            Color color = ColorUtil.fromDensityCode(colors[i]);
            if (ColorUtil.toDouble(color) == value) {
                return i;
            }
        }
        return -1;
    }

}
