package hk.edu.polyu.comp.comp2021.clevis.model.shape;

public class Rectangle extends Shape {
    double x, y, width, height;
    public final static int EXPECTED_VALUES = 4;

    public Rectangle(String n, double x, double y, double width, double height) {
        super(n);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getx(){
        return this.x;
    }
    public double gety(){
        return this.y;
    }

    public void move(double dx, double dy){
        this.x += dx;
        this.y += dy;
    }
    public void list() {
        System.out.println("Rectangle " + this.name + " x:" + String.format("%.2f",x) + " y:" + String.format("%.2f",y) + " width:" + String.format("%.2f",width) + " height:" + String.format("%.2f",height));
    }
    public void initboundingbox(){
        super.boundingbox(x,y,width,height);
    }
    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }
}
