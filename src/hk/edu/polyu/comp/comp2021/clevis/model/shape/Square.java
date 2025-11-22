package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;

/**
 * Square class represents a square shape with position and side length.
 */
public class Square extends Shape {
    private double x;
    private double y;
    private final double l;
    /**
     *  Expected number of values for square shape.
     */
    public final static int EXPECTED_VALUES = 3;

    /**
     * Constructor to initialize a Square with name, position, and side length.
     * @param n name of the square
     * @param x x coordinate of the square
     * @param y y coordinate of the square
     * @param l side length of the square
     */
    public Square(String n, double x, double y, double l) {
        super(n);
        this.x = x;
        this.y = y;
        this.l = l;
    }

    @Override
    public double getx() {
        return this.x;
    }

    @Override
    public double gety() {
        return this.y;
    }


    @Override
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public void list() {
        System.out.println("Square " + this.name +
                " x:" + String.format("%.2f",x )+
                " y:" + String.format("%.2f",y )+
                " side width:" + String.format("%.2f",l));
    }

    @Override
    public void initboundingbox() {
        super.boundingbox(x, y, l, l);
    }

    @Override
    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", getBoundingbox()[0]) +
                " y:" + String.format("%.2f", getBoundingbox()[1]) +
                " width:" + String.format("%.2f", getBoundingbox()[2]) +
                " height" + String.format("%.2f", getBoundingbox()[3]));
    }

    @Override
    public void computeWorldBounds(double[] bounds) {
        bounds[0] = Math.min(bounds[0], x);
        bounds[1] = Math.min(bounds[1], y);
        bounds[2] = Math.max(bounds[2], x + l);
        bounds[3] = Math.max(bounds[3], y + l);
    }

    @Override
    public void drawShape(Graphics2D g2d) {
        g2d.drawRect((int)Math.round(x), (int)Math.round(y), (int)Math.round(l), (int)Math.round(l));
    }
}

