package vn.ducquoc.visual.heatmap;

public class Rectangle extends PointBasedShape {

    // public fields to simplify getters/setters
    public double width;
    public double height;

    public Rectangle() {
    }

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return "left: " + x + " top: " + y + " width: " + width + " height: " + height;
    }

    //
    // helpers: getMaxX, getMaxY, getCenterPointX, getCenterPointY
    //
    public double maxX() {
        return this.x + this.width;
    }

    public double maxY() {
        return this.y + this.height;
    }

    public double centerX() {
        return (this.x + this.width) / 2;
    }

    public double centerY() {
        return (this.y + this.height) / 2;
    }

}
