package vn.ducquoc.visual.heatmap.treemap;

import vn.ducquoc.visual.heatmap.PointBasedShape;
import vn.ducquoc.visual.heatmap.Rectangle;

/**
 * @author ducquoc
 * 
 * @param <T>
 */
public class RectangleColoredNode<T> implements ColoredNode<T> {

    private String color;
    private Rectangle bound;
    private T object;

    public RectangleColoredNode(String color, Rectangle bound, T object) {
        this.color = color;
        this.bound = bound;
        this.object = object;
    }

    //
    // trivial getters/setters
    //
    /**
     * @override
     */
    public String getColor() {
        return this.color;
    }

    /**
     * @override
     */
    public Rectangle getBound() {
        return this.bound;
    }

    /**
     * @override
     */
    public T getObject() {
        return this.object;
    }

    /** @override */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @override
     */
    public void setBound(PointBasedShape bound) {
        this.bound = (Rectangle) bound;
    }

}
