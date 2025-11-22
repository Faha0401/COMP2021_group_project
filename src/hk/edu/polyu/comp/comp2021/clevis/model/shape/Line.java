package hk.edu.polyu.comp.comp2021.clevis.model.shape;

public class Line extends Shape {
        double x1, y1, x2, y2;
        public final static int EXPECTED_VALUES = 4;

        public Line(String n, double x1, double y1, double x2, double y2) {
            super(n);
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }

        public double getx(){
            return this.x1;
        }
        public double gety(){
            return this.y1;
        }


        public void move(double dx, double dy){
            this.x1 += dx;
            this.y1 += dy;
            this.x2 += dx;
            this.y2 += dy;
        }

        public void list() {
            System.out.println("Line " + this.name + " x1:" + String.format("%.2f",x1) + " y1:" + String.format("%.2f",y1) + " x1:" + String.format("%.2f",x2) + " y2:" + String.format("%.2f",y2));
        }
        public void initboundingbox(){
            super.boundingbox(x1,y1,Math.abs(x2-x1),Math.abs(y2-y1));
        }
        public void boundingbox() {
            initboundingbox();
            System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));        }

        public double getX1(){ return x1; }
        public double getY1(){ return y1; }
        public double getX2(){ return x2; }
        public double getY2(){ return y2; }


    }


