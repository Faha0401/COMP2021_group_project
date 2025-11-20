package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.List;

class GroupedShape extends Shape{
    List<Shape> group;
    public GroupedShape(String n) {
        super(n);
        this.group = new ArrayList<>();
    }
    public void add(Shape addshape){
        if(addshape instanceof GroupedShape){
            group.addAll(((GroupedShape) addshape).group);
        }else{
            group.add(addshape);
        }
    }
    public double getx(){
        initboundingbox();
        return this.boundingbox[0];
    }
    public double gety(){
        initboundingbox();
        return this.boundingbox[1];
    }


    public void list(){
        System.out.println("Group shape :" + name);


        for (Shape shape : group) {
            System.out.print("\t");
            shape.list();
        }
    }
    public void move(double dx, double dy){
        for(Shape e : group){
            e.move(dx, dy);
        }
    }
    public void boundingbox(){
        initboundingbox();
        for (Shape s : group){
            this.boundingbox[0] = Math.min(this.boundingbox[0], s.boundingbox[0]);
            this.boundingbox[1] = Math.min(this.boundingbox[1], s.boundingbox[1]);
            this.boundingbox[2] = Math.max(this.boundingbox[2], s.boundingbox[2]);
            this.boundingbox[3] = Math.max(this.boundingbox[3], s.boundingbox[3]);
        }
        System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[0]));
    }

    public void initboundingbox() {
        for (Shape s : group){s.initboundingbox();}
        this.boundingbox[0] = Integer.MAX_VALUE;
        this.boundingbox[1] = Integer.MAX_VALUE;
        this.boundingbox[2] = Integer.MIN_VALUE;
        this.boundingbox[3] = Integer.MIN_VALUE;
    }
}
