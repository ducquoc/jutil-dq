package vn.ducquoc.visual.heatmap.treemap;

import vn.ducquoc.visual.heatmap.PointBasedShape;

public interface ColoredNode<T> {
    /**
     * 
     * @return color in hex format without character #
     */
    String getColor();

    /**
     * 
     * @return PointBasedShape instance in percent (Rectangle for now)
     */
    PointBasedShape getBound();

    /**
     * 
     * @param color
     *            in hex format (ex: #ffffff or ffffff)
     */
    void setColor(String color);

    /**
     * 
     * @param rect
     */
    void setBound(PointBasedShape rect);

    /**
     * 
     * @return the data object
     */
    T getObject();

}
