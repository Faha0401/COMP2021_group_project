package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;

/**
 * Shape abstract class represents a generic shape with common properties and methods.
 */
public abstract class Shape {
    /**
     *  Name of the shape.
     */
    protected String name;
    private final double[] boundingbox = new double[4];
    private final int zIndex;
    private static int zCount = 0;

    /**
     * Constructor to initialize a Shape with a name.
     * @param name name of the shape
     */
    Shape(String name) {
        this.name = name;
        addZCount();
        zIndex = zCount;
    }

    /**
     *
     */
    public static void addZCount(){
        zCount++;
    }

    /**
     * Gets the name of the shape.
     * @return name of the shape
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the z-index of the shape.
     * @return z-index of the shape
     */
    public int getzIndex() {
        return zIndex;
    }

    /**
     * Gets the bounding box of the shape.
     * @return bounding box of the shape
     */
    public double[] getBoundingbox() {
        return boundingbox;
    }

    /**
     * Lists the details of the shape.
     */
    public abstract void list();

    /**
     * Moves the shape by the specified offsets.
     * @param dx the offset in the x direction
     * @param dy the offset in the y direction
     */
    public abstract void move(double dx, double dy);

    /**
     * Calculates and prints the bounding box of the shape.
     */
    public abstract void boundingbox();

    /**
     * Initializes the bounding box of the shape.
     */
    public abstract void initboundingbox();

    /**
     * Sets the bounding box of the shape.
     * @param x the x coordinate of the bounding box
     * @param y the y coordinate of the bounding box
     * @param width the width of the bounding box
     * @param height the height of the bounding box
     */
    public void boundingbox(double x, double y, double width, double height){
        this.getBoundingbox()[0] = x;
        this.getBoundingbox()[1] = y;
        this.getBoundingbox()[2] = width;
        this.getBoundingbox()[3] = height;
    }

    /**
     * Gets the x coordinate of the shape.
     * @return the x coordinate of the shape
     */
    public abstract double getx();

    /**
     * Gets the y coordinate of the shape.
     * @return the y coordinate of the shape
     */
    public abstract double gety();

    /**
     * Computes the world bounds of the shape for GUI system.
     * @param bounds array to store the computed bounds
     */
    public abstract void computeWorldBounds(double[] bounds);

    /**
     * Draws the corresponding shape on GUI system.
     * @param g2d Graphics2D object for drawing
     */
    public abstract void drawShape(Graphics2D g2d);
}


