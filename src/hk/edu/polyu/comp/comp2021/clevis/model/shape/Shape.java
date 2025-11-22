package hk.edu.polyu.comp.comp2021.clevis.model.shape;

public abstract class Shape {
    protected String name;
    protected double[] boundingbox = new double[4];
    private int zIndex;
    private static int zCount = 0;

    Shape(String name) {
        this.name = name;
        zIndex = zCount++;
    }

    public String getName() {
        return name;
    }

    public int getzIndex() {
        return zIndex;
    }

    public double[] getBoundingbox() {
        return boundingbox;
    }

    public abstract void list();
    public abstract void move(double dx, double dy);
    public abstract void boundingbox();
    public abstract void initboundingbox();
    public void boundingbox(double x, double y, double width, double height){
        this.boundingbox[0] = x;
        this.boundingbox[1] = y;
        this.boundingbox[2] = width;
        this.boundingbox[3] = height;
    }
    public abstract double getx();
    public abstract double gety();
}
