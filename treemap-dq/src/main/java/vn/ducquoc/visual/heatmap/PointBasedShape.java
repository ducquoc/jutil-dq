package vn.ducquoc.visual.heatmap;

public class PointBasedShape {

    // public fields to simplify getters/setters
    public double x;
    public double y;

    public PointBasedShape() {
    }

    public PointBasedShape(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "left: " + x + " top: " + y;
    }

}
