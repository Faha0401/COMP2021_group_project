package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;

/**
 * Line class represents a line shape defined by two endpoints.
 */
public class Line extends Shape {
    private double x1, y1, x2, y2;
    /**
     *  Expected number of values for line shape.
     */
    public final static int EXPECTED_VALUES = 4;

    /**
     * Constructor to initialize a Line with name and endpoints.
     * @param n name of the line
     * @param x1 x coordinate of the first endpoint
     * @param y1 y coordinate of the first endpoint
     * @param x2 x coordinate of the second endpoint
     * @param y2 y coordinate of the second endpoint
     */
    public Line(String n, double x1, double y1, double x2, double y2) {
        super(n);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public double getx(){
        return this.x1;
    }
    @Override
    public double gety(){
        return this.y1;
    }


    @Override
    public void move(double dx, double dy){
        this.x1 += dx;
        this.y1 += dy;
        this.x2 += dx;
        this.y2 += dy;
    }

    @Override
    public void list() {
        System.out.println("Line " + this.name + " x1:" + String.format("%.2f",x1) + " y1:" + String.format("%.2f",y1) + " x1:" + String.format("%.2f",x2) + " y2:" + String.format("%.2f",y2));
    }
    @Override
    public void initboundingbox(){
        super.boundingbox(x1,y1,Math.abs(x2-x1),Math.abs(y2-y1));
    }
    @Override
    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", getBoundingbox()[0]) + " y:" + String.format("%.2f", getBoundingbox()[1]) + " width:"+ String.format("%.2f", getBoundingbox()[2]) + " height" + String.format("%.2f", getBoundingbox()[3]));        }

    @Override
    public void computeWorldBounds(double[] bounds) {
        bounds[0] = Math.min(bounds[0], Math.min(x1, x2));
        bounds[1] = Math.min(bounds[1], Math.min(y1, y2));
        bounds[2] = Math.max(bounds[2], Math.max(x1, x2));
        bounds[3] = Math.max(bounds[3], Math.max(y1, y2));
    }

    @Override
    public void drawShape(Graphics2D g2d) {
        g2d.drawLine((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
    }
}


