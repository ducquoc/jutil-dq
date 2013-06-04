package vn.ducquoc.visual.heatmap.treemap;

import java.awt.Color;

import vn.ducquoc.visual.heatmap.ColorUtil;

public class NodeMetricsGetter<T> implements MetricsGetter<T> {

    public Double getSizeValue(T object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        if (object instanceof ArtifactBean) {
            return ((ArtifactBean) object).getCost();
        }

        return Double.parseDouble(object.toString());
    }

    public Double getDensityValue(T object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        if (object instanceof ArtifactBean) {
            String colorCode = ((ArtifactBean) object).getHealthColor();
            Color color = ColorUtil.fromDensityCode(colorCode);

            // String colorHexCode = ColorUtil.makeHexColor(new int[] {
            // c.getRed(), c.getGreen(), c.getBlue() });
            return new Double(ColorUtil.toDouble(color));
        }

        return Double.parseDouble(object.toString());
    }

}
