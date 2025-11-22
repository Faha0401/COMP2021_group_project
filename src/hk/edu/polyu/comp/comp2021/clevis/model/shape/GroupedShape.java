package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GroupedShape class represents a group of shapes as a single shape.
 */
public class GroupedShape extends Shape {
    private final List<Shape> group;

    /**
     * Constructor to initialize a GroupedShape with a name.
     * @param n name of the grouped shape
     */
    public GroupedShape(String n) {
        super(n);
        this.group = new ArrayList<>();
    }

    /**
     * Adds a shape to the group. If the added shape is a GroupedShape, its shapes are added individually.
     * @param addshape shape to be added to the group
     */
    public void add(Shape addshape){
        if(addshape instanceof GroupedShape){
            getGroup().addAll(((GroupedShape) addshape).getGroup());
        }else{
            getGroup().add(addshape);
        }
    }
    @Override
    public double getx(){
        initboundingbox();
        return this.getBoundingbox()[0];
    }
    @Override
    public double gety(){
        initboundingbox();
        return this.getBoundingbox()[1];
    }

    /**
     * Gets the list of shapes in the group.
     * @return list of shapes in the group
     */
    public List<Shape> getGroup() {
        return group;
    }

    @Override
    public void list(){
        System.out.println("Group "+name+" :");

        for (Shape shape : getGroup()) {
            System.out.print("\t");
            shape.list();
        }
    }
    @Override
    public void move(double dx, double dy){
        for(Shape e : getGroup()){
            e.move(dx, dy);
        }
    }
    @Override
    public void boundingbox(){
        initboundingbox();

        System.out.println("Bounding Box: x:" + String.format("%.2f", getBoundingbox()[0]) + " y:" + String.format("%.2f", getBoundingbox()[1]) + " width:"+ String.format("%.2f", getBoundingbox()[2]) + " height:" + String.format("%.2f", getBoundingbox()[3]));
    }

    @Override
    public void initboundingbox() {
        for (Shape s : getGroup()){s.initboundingbox();}
        this.getBoundingbox()[0] = Integer.MAX_VALUE;
        this.getBoundingbox()[1] = Integer.MAX_VALUE;
        this.getBoundingbox()[2] = Integer.MIN_VALUE;
        this.getBoundingbox()[3] = Integer.MIN_VALUE;
        for (Shape s : getGroup()){
            this.getBoundingbox()[0] = Math.min(this.getBoundingbox()[0], s.getBoundingbox()[0]);
            this.getBoundingbox()[1] = Math.min(this.getBoundingbox()[1], s.getBoundingbox()[1]);
            this.getBoundingbox()[2] = Math.max(this.getBoundingbox()[2], s.getBoundingbox()[2]);
            this.getBoundingbox()[3] = Math.max(this.getBoundingbox()[3], s.getBoundingbox()[3]);
        }
    }

    @Override
    public void computeWorldBounds(double[] bounds) {
        for (Shape s : getGroup()) {
            s.computeWorldBounds(bounds);
        }
    }

    @Override
    public void drawShape(Graphics2D g2d) {
        for (Shape s : getGroup()) {
            s.drawShape(g2d);
        }
    }
}
