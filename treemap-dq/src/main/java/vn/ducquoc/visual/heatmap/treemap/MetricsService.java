package vn.ducquoc.visual.heatmap.treemap;

import java.util.List;

import vn.ducquoc.visual.heatmap.Rectangle;

public interface MetricsService {

    /**
     * Calculates size and position in percent of an item in Treemap/Heatmap
     * 
     * @param <T>
     * 
     * @param <T>
     * @param data
     *            list of items will be displayed in Treemap/Heatmap
     * @param rectCanvas
     *            contains width, height of a container
     * @param sizeGetter
     *            provide the size value of <T>
     * @return list Object that can be displayed in the Treemap/Heatmap UI
     */
    <T> List<? extends ColoredNode<T>> layout(List<T> data, Rectangle rectCanvas, MetricsGetter<T> metricsGetter);

    /**
     * Applies color for items in Treemap/Heatmap
     * 
     * @param <T>
     * @param data
     * @param seedColor
     *            is a hex color for generating random colors or gradients
     * @param metricGetter
     *            provide color value
     */
    <T> void populateColor(List<? extends ColoredNode<T>> data, String colorSeed, final MetricsGetter<T> metricsGetter);

}
