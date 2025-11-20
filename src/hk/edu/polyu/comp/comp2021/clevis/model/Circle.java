package hk.edu.polyu.comp.comp2021.clevis.model;

class Circle extends Shape {
    double x, y, r;
    public final static int EXPECTED_VALUES = 3;

    public Circle(String n, double x, double y, double r) {
        super(n);
        this.x = x;
        this.y = y;
        this.r = r;
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
        System.out.println("Circle " + this.name + " x:" + x + " y:" + y + " radius:" + r);
    }
    public void initboundingbox(){
        super.boundingbox(x-r,y-r,2*r,2*r);
    }
    public void boundingbox() {
        initboundingbox();
        System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));        }
}