package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;

/**
 *  Rectangle class represents a rectangle shape with position and dimensions.
 */
public class Rectangle extends Shape {
    private double x;
    private double y;
    private final double width;
    private final double height;
    /**
     *  Expected number of values for rectangle shape.
     */
    public final static int EXPECTED_VALUES = 4;

    /**
     * Constructor to initialize a Rectangle with name, position, width, and height.
     * @param n name of the rectangle
     * @param x x coordinate of the rectangle
     * @param y y coordinate of the rectangle
     * @param width width of the rectangle
     * @param height height of the rectangle
     */
    public Rectangle(String n, double x, double y, double width, double height) {
        super(n);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public double getx(){
        return this.x;
    }
    @Override
    public double gety(){
        return this.y;
    }

    @Override
    public void move(double dx, double dy){
        this.x += dx;
        this.y += dy;
    }
    @Override
    public void list() {
        System.out.println("Rectangle " + this.name +
                " x:" + String.format("%.2f",x) +
                " y:" + String.format("%.2f",y) +
                " width:" + String.format("%.2f",width) +
                " height:" + String.format("%.2f",height));
    }
    @Override
    public void initboundingbox(){
        super.boundingbox(x,y,width,height);
    }
    @Override
    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", getBoundingbox()[0]) +
                " y:" + String.format("%.2f", getBoundingbox()[1]) +
                " width:"+ String.format("%.2f", getBoundingbox()[2]) +
                " height" + String.format("%.2f", getBoundingbox()[3]));
    }

    @Override
    public void computeWorldBounds(double[] bounds) {
        bounds[0] = Math.min(bounds[0], this.x);
        bounds[1] = Math.min(bounds[1], this.y);
        bounds[2] = Math.max(bounds[2], this.x + this.width);
        bounds[3] = Math.max(bounds[3], this.y + this.height);
    }

    @Override
    public void drawShape(Graphics2D g2d) {
        g2d.drawRect((int)Math.round(x), (int)Math.round(y), (int)Math.round(width), (int)Math.round(height));
    }
}