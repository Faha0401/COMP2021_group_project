package hk.edu.polyu.comp.comp2021.clevis.model.shape;

public class Square extends Shape {
    double x, y, l;
    public final static int EXPECTED_VALUES = 3;

    public Square(String n, double x, double y, double l) {
        super(n);
        this.x = x;
        this.y = y;
        this.l = l;
    }

    public double getx() {
        return this.x;
    }

    public double gety() {
        return this.y;
    }


    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void list() {
        System.out.println("Square " + this.name + " x:" + String.format("%.2f",x )+ " y:" + String.format("%.2f",y )+ " side width:" + String.format("%.2f",l));
    }

    public void initboundingbox() {
        super.boundingbox(x, y, l, l);
    }

    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:" + String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));
    }

    public double getSide() {
        return this.l;
    }
}

