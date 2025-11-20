package hk.edu.polyu.comp.comp2021.clevis.model;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;



/*read me!!!!!!!!!!!!!!
so currently the code is just a prototype and would like to have the following implementation
1. Use bucket list to search for shapes using O(1) time, need key for Z-index
2. Improve the input handling for exceptional input
3.
feel free to add more
*/

public class Clevis {
    List<Shape> Shapes = new ArrayList<>();
    static int zCount = 0;
    static int index;
    static int commandindex = 0;

    class Logger {
        private PrintWriter htmlWriter;
        private PrintWriter txtWriter;

        public Logger(String htmlPath, String txtPath) {
            try {
                htmlWriter = new PrintWriter(new FileWriter(htmlPath, true));
                txtWriter = new PrintWriter(new FileWriter(txtPath, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void logCommand(int index, String command) {
            htmlWriter.println("<tr><td>" + index + "</td><td>" + command + "</td></tr>");
            txtWriter.println(command);
            htmlWriter.flush();
            txtWriter.flush();
        }

        public void close() {
            htmlWriter.close();
            txtWriter.close();
        }
    }


    public void delete(String name) {
        Shapes.removeIf(s -> s.name.equals(name));
    }

    public boolean ExistName(String name) {

        for (Shape ungroupShape : Shapes) {
            if (ungroupShape.name.equals(name)) {
                System.out.println("The name " + name + " is used, please enter another name.");
                return true;
            }
        }
        return false;
    }
    private Shape searchShape(String name){ //search for ungroup shape
        for(Shape e: Shapes){
            if (e.name.equals(name)){
                return e;
            }
        }
        System.out.println("Shape " + name + " is not found.");
        return null;
    }

    public static boolean checkInputLength(String[] parts, int expectedLength) {
        if (parts.length != expectedLength) {
            System.out.println("[Error]: expected " + expectedLength + " values, but got " + parts.length + ".");
            return false;
        }
        return true;
    }

    public static double[] ReadValues(String[] parts, int numberOfValues) {
        double[] values = new double[numberOfValues];

        for (int i = 0; i < numberOfValues; i++) {
            try {
                values[i] = Double.parseDouble(parts[index+i]);
            } catch (NumberFormatException e) {
                System.out.println("[Error]: value \"" + parts[index+i] + "\" is not a valid double.");
                return null;
            }
        }
        return values;
    }
    private Boolean shapeAt(double x, double y, Shape e){
        return x > e.boundingbox[0] - 0.05 && y > e.boundingbox[1] - 0.05
                && x < e.boundingbox[3]+e.boundingbox[0] + 0.05 && y < e.boundingbox[4]+e.boundingbox[1] + 0.05;
    }

    public Clevis() {
        System.out.println("Welcome to our clevis: ");
        label:
        while (true) { //using a variable here since im using try and catch in input scanning
            String operation;
            Logger logger = new Logger("log.html", "log.txt");
            try { //looking for a better catch implementation
                System.out.println("Please enter your operation: ");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                logger.logCommand(commandindex, line);
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0) continue;
                index = 0;
                operation = parts[index++];

                switch (operation) {
                    case "quit": {
                        System.out.println("The application has been terminated.");
                        break label;
                    }//req15;
                    case "rectangle": {
                        if (!checkInputLength(parts, Rectangle.EXPECTED_VALUES + 2)) break;
                        String name = parts[index++];
                        if (ExistName(name)) break;

                        double[] values = ReadValues(parts, Rectangle.EXPECTED_VALUES);
                        if (values == null) break;
                        Shapes.add(0, new Rectangle(name, values[0], values[1], values[2], values[3]));
                        System.out.println("Rectangle " + name + " has been created.");
                        break;
                    } //req2
                    case "line": {
                        if (!checkInputLength(parts, Line.EXPECTED_VALUES + 2)) break;
                        String name = parts[index++];
                        if (ExistName(name)) break;

                        double[] values = ReadValues(parts, Line.EXPECTED_VALUES);
                        if (values == null) break;
                        Shapes.add(0,new Line(name, values[0], values[1], values[2], values[3]));
                        System.out.println("Line " + name + " has been created.");
                        break;
                    }//req3
                    case "circle": {
                        if (!checkInputLength(parts, Circle.EXPECTED_VALUES + 2)) break;
                        String name = parts[index++];
                        if (ExistName(name)) break;

                        double[] values = ReadValues(parts, Circle.EXPECTED_VALUES);
                        if (values == null) break;
                        Shapes.add(0, new Circle(name, values[0], values[1], values[2]));
                        System.out.println("Circle " + name + " has been created.");
                        break;
                    }//req4
                    case "square": {
                        if (!checkInputLength(parts, Square.EXPECTED_VALUES + 2)) break;
                        String name = parts[index++];
                        if (ExistName(name)) break;

                        double[] values = ReadValues(parts, Square.EXPECTED_VALUES);
                        if (values == null) break;
                        Shapes.add(0, new Square(name, values[0], values[1], values[2]));
                        System.out.println("Square " + name + " has been created.");
                        break;

                    }//req5
                    case "list": {
                        if (!checkInputLength(parts, 2)) break;
                        String name = parts[index++];
                        Shape s = searchShape(name);
                        if( s!= null){
                            s.list();
                        }
                        break;
                    }//req13
                    case "listAll": {
                        for (Shape s : Shapes) {
                            s.list();
                        }
                        break;
                    }//req14
                    case "delete": {
                        if (!checkInputLength(parts, 2)) break;
                        String delName = parts[index++];
                        delete(delName);
                        break;
                    }//req8
                    case "group": {
                        if (parts.length < 4) {
                            System.out.println("[Error]: expected at least 4 values, but got " + parts.length + ".");
                            break;
                        }
                        String groupName = parts[index++];
                        if (ExistName(groupName)) break;
                        GroupedShape newGroup = new GroupedShape(groupName);
                        String[] shapeName = Arrays.copyOfRange(parts, index, parts.length);
                        if (shapeName.length == 0) {
                            System.out.println("No shape is selected.");
                        }
                        boolean aborted = false;
                        for (String s : shapeName) {
                            Shape shape = searchShape(s);
                            if (shape == null) {
                                System.out.println("Group creation aborted.");
                                aborted = true;
                                break;
                            }
                            newGroup.add(shape);
                            Shapes.remove(shape);
                        }
                        if (aborted) break;
                        Shapes.add(0, newGroup);
                        System.out.println("Group " + groupName + " has been created.");
                        break;
                    }
                    case "ungroup":{
                        if (!checkInputLength(parts, 2)) break;
                        String groupName = parts[index++];
                        boolean found = false;
                        for (Shape s : Shapes){
                            if (s.name.equals(groupName) && s instanceof GroupedShape){
                                Shapes.addAll(((GroupedShape)s).group);
                                Shapes.remove(s);
                                Shapes.sort(Comparator.comparingInt((Shape x) -> x.zIndex).reversed());
                                System.out.println("Group " + groupName + " has been ungrouped.");
                                found = true;
                                break;
                            }
                        }
                        if(!found) System.out.println("Group " + groupName + " not found.");
                        break ;
                    }//req7

                    case "intersect": {
                        if (!checkInputLength(parts, 3)) break;
                        Shape n1 = searchShape(parts[index++]);
                        Shape n2 = searchShape(parts[index++]);
                        if(n1 == null || n2 == null) break;
                        if (!(n1.boundingbox[0] > n2.boundingbox[0]+n2.boundingbox[2] ||    //n1 left > n2 right
                                n1.boundingbox[0]+n1.boundingbox[2] < n2.boundingbox[0] ||  //n1 right < n2 left
                                n1.boundingbox[1] < n2.boundingbox[1]+n2.boundingbox[3] ||  //n1 top < n2 bottom
                                n1.boundingbox[1]+n1.boundingbox[3] > n2.boundingbox[1])){  //n1 bottom > n2 top
                            System.out.println(n1.name + " intersect with " + n2.name+".");
                            break;
                        }
                        System.out.println(n1.name + " does not intersect with " + n2.name+".");
                        break;
                    }

                    case "boundingBox":{
                        if (!checkInputLength(parts, 2)) break;
                        String ShapeName = parts[index++];
                        Shape s = searchShape(ShapeName);
                        if(s != null) s.boundingbox();
                        break;
                    }

                    case "move": {
                        if (!checkInputLength(parts, 4)) break;
                        String ShapeName = parts[index++];
                        Shape s = searchShape(ShapeName);
                        double[] coord = ReadValues(parts, 2);
                        if(s!=null && coord!=null) {
                            double x = coord[0];
                            double y = coord[1];
                            s.move(x,y);
                            System.out.println("Shape " + ShapeName + " is moved to ("+String.format("%.2f", s.getx())+","+String.format("%.2f", s.gety())+").");
                            break;
                        }

                    }

                    case "shapeAt": {
                        if (!checkInputLength(parts, 3)) break;
                        double[] coord = ReadValues(parts, 2);
                        if(coord==null) break;
                        double x = coord[0];
                        double y = coord[1];
                        for (Shape s:Shapes) {
                            if (shapeAt(x, y, s)) {
                                System.out.println(s.getClass() + s.name + " is the first shape that covers the point (" + String.format("%.2f", x)+","+String.format("%.2f", y)+ ").");
                                break;
                            }
                        }
                        System.out.println("No shape is covering the point (" + String.format("%.2f", x)+","+String.format("%.2f", y) + ").");
                        break;
                    }

                    default:
                        System.out.println("Unexpected Command.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally{
                logger.close();
            }
        }
    }

    abstract class Shape {
        String name;
        int zIndex;
        double[] boundingbox = new double[4];

        Shape(String name) {
            this.name = name;
            zIndex = zCount++;
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

    class Rectangle extends Shape {
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
            System.out.println("Rectangle " + this.name + " x:" + x + " y:" + y + " width:" + width + " height:" + height);
        }
        public void initboundingbox(){
            super.boundingbox(x,y,width,height);
        }
        public void boundingbox() {
            initboundingbox();
            System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));
        }
    }

    class Line extends Shape {
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
            System.out.println("Line " + this.name + " x1:" + x1 + " y1:" + y1 + " x1:" + x2 + " y2:" + y2);
        }
        public void initboundingbox(){
            super.boundingbox(x1,y1,x2-x1,y2-y1);
        }
        public void boundingbox() {
            initboundingbox();
            System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));        }


    }

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

    class Square extends Shape {
        double x, y, l;
        public final static int EXPECTED_VALUES = 3;

        public Square(String n, double x, double y, double l) {
            super(n);
            this.x = x;
            this.y = y;
            this.l = l;
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
            System.out.println("Square " + this.name + " x:" + x + " y:" + y + " side width:" + l);
        }
        public void initboundingbox(){
            super.boundingbox(x,y,l,l);
           }
        public void boundingbox() {
            initboundingbox();
            System.out.println("Bounding Box: x:" + String.format("%.2f", boundingbox[0]) + " y:" + String.format("%.2f", boundingbox[1]) + " width:"+ String.format("%.2f", boundingbox[2]) + " height" + String.format("%.2f", boundingbox[3]));        }
    }

    class GroupedShape extends Shape{
        List<Shape> group;
        public GroupedShape(String n) {
            super(n);
            this.group = new ArrayList<>();
        }
        private void add(Shape addshape){
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
}