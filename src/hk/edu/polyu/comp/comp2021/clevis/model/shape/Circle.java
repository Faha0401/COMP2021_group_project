package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;

/**
 * Circle class represents a circle shape with position and radius.
 */
public class Circle extends Shape {
    private double x;
    private double y;
    private final double r;
    /**
     * Expected number of values for circle shape.
     */
    public final static int EXPECTED_VALUES = 3;

    /**
     * Constructor to initialize a Circle with name, position, and radius.
     * @param n name of the circle
     * @param x x coordinate of the circle
     * @param y y coordinate of the circle
     * @param r radius of the circle
     */
    public Circle(String n, double x, double y, double r) {
        super(n);
        this.x = x;
        this.y = y;
        this.r = r;
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
        System.out.println("Circle " + this.name +
                " x:" + String.format("%.2f",x) +
                " y:" + String.format("%.2f",y) +
                " radius:" + String.format("%.2f",r));
    }
    @Override
    public void initboundingbox(){
        super.boundingbox(x-r,y-r,2*r,2*r);
    }
    @Override
    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", getBoundingbox()[0]) +
                " y:" + String.format("%.2f", getBoundingbox()[1]) +
                " width:"+ String.format("%.2f", getBoundingbox()[2]) +
                " height:" + String.format("%.2f", getBoundingbox()[3]));
    }

    @Override
    public void computeWorldBounds(double[] bounds) {
        bounds[0] = Math.min(bounds[0], x - r);
        bounds[1] = Math.min(bounds[1], y - r);
        bounds[2] = Math.max(bounds[2], x + r);
        bounds[3] = Math.max(bounds[3], y + r);
    }

    @Override
    public void drawShape(Graphics2D g2d) {
        g2d.drawOval((int) Math.round(x - r), (int) Math.round(y - r), (int) Math.round(2 * r), (int) Math.round(2 * r));
    }
}