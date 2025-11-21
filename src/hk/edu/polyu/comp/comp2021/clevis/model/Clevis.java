package hk.edu.polyu.comp.comp2021.clevis.model;
import java.util.*;

public class Clevis {
    List<Shape> Shapes = new ArrayList<>();
    Stack<Shape> Bin = new Stack<>();
    Stack<String[]> undo = new Stack<>();
    Stack<String[]> redo = new Stack<>();
    Stack<double[]> undocoord = new Stack<>();
    Stack<double[]> redocoord = new Stack<>();

    static int index;
    static int commandindex = 0;

    public void redo(){
        if(redo.empty()){
            System.out.println("There is no operation to be redo");
            return;
        }
        String[] curRndo = redo.pop();
            switch (curRndo[0]){
                case("create"): {
                    undo.push(new String[]{"delete", curRndo[1]});
                    break;
                }
                case("delete"): {
                    undo.push(new String[]{"create", curRndo[1]});
                    break;
                }
                case("ungroup"): {
                    undo.push(new String[]{"regroup", curRndo[1]});
                    break;
                }
                case("regroup"): {
                    undo.push(new String[]{"ungroup", curRndo[1]});
                    break;
                }
                case("move"): {
                    Shape shape = this.searchShape(curRndo[1]);
                    double[] dest = redocoord.pop();
                    undocoord.push(new double[]{shape.getx(), shape.gety()});
                    shape.move(dest[0], dest[1]);
                    undo.push(new String[]{"move", curRndo[1]});
                    break;
                }
            }
        undo(true);
    }

    public void undo(boolean isredo){
        if(undo.empty()){
            System.out.println("There is no operation to be undo");
            return;
        }
        String[] curUndo = undo.pop();
            switch (curUndo[0]){
                case("create"):{
                    Shape shape = this.searchShape(curUndo[1]);
                    Shapes.remove(shape);
                    Bin.push(shape);
                    System.out.println(shape.name + " has been removed");
                    break;}
                case("delete"):{
                    Shape shape = Bin.pop();
                    Shapes.add(shape);
                    System.out.println(shape.name + " has been added");
                    break;}
                case("ungroup"):{
                    Shape shape = this.searchShape(curUndo[1]);
                    ungroup(shape.name);
                    break;}
                case("regroup"):{
                    Shape shape = Bin.pop();
                    for(Shape s : ((GroupedShape)shape).group){
                        delete(s.name);
                    }
                    Shapes.add(shape);
                    System.out.println(shape.name + " has been regrouped");
                    break;}
                case("move"):{
                    Shape shape = this.searchShape(curUndo[1]);
                    double[] dest = undocoord.pop();
                    redocoord.push(new double[]{shape.getx(), shape.gety()});
                    shape.move(dest[0]*-1, dest[1]*-1);
                    System.out.println(shape.name + " has moved back to " + "(" + shape.getx() + "," + shape.gety() + ")");
                    break;
                }
                default:
                    System.out.println("An error has occur while undo");
                    break;
            }
        if(!isredo) redo.push(curUndo);
        ;
    }

    public boolean ungroup(String groupName) {
        for (Shape s : Shapes) {
            if (s.name.equals(groupName) && s instanceof GroupedShape) {
                Bin.push(s);
                Shapes.addAll(((GroupedShape) s).group);
                Shapes.remove(s);
                Shapes.sort(Comparator.comparingInt((Shape x) -> x.zIndex).reversed());
                System.out.println("Group " + groupName + " has been ungrouped.");
                return true;
            }
        }
        return false;
    }

    public void delete(String name) {
        if (this.searchShape(name) != null) {
            Bin.push(this.searchShape(name));
            Shapes.remove(this.searchShape(name));
        } else {
            System.out.println("Shape " + name + " is not found.");
        }
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
        e.initboundingbox();
        return (x > e.boundingbox[0] - 0.05 && x < e.boundingbox[0] + 0.05 && y < e.boundingbox[1] + 0.05 && y > e.boundingbox[1] + e.boundingbox[3] - 0.05) ||
                (x > e.boundingbox[0] + e.boundingbox[2] - 0.05 && x < e.boundingbox[0] + e.boundingbox[2] + 0.05 && y < e.boundingbox[1] + 0.05 && y > e.boundingbox[1] + e.boundingbox[3] - 0.05) ||
                (y > e.boundingbox[1] - 0.05 && y < e.boundingbox[1] + 0.05 && x > e.boundingbox[0] - 0.05 && x < e.boundingbox[0] + e.boundingbox[2] + 0.05) ||
                (y > e.boundingbox[1] + e.boundingbox[3] - 0.05 && y < e.boundingbox[1] + e.boundingbox[3] + 0.05 && x > e.boundingbox[0] - 0.05 && x < e.boundingbox[0] + e.boundingbox[2] + 0.05);
    }

    public Clevis() {
        System.out.println("Welcome to our clevis: ");
        // Use a single Scanner for System.in and guard calls with hasNextLine()
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) { //using a variable here since im using try and catch in input scanning
            String operation;
            Logger logger = new Logger("log.html", "log.txt");
            try { //looking for a better catch implementation
                System.out.println("Please enter your operation: ");
                if (!scanner.hasNextLine()) break; // avoid NoSuchElementException when stdin is exhausted (tests)
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
                        undo.push(new String[]{"create", name});
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
                        undo.push(new String[]{"create", name});
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
                        undo.push(new String[]{"create", name});
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
                        undo.push(new String[]{"create", name});
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
                        System.out.println("Shape " + delName + " has been deleted.");
                        undo.push(new String[]{"delete", delName});
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
                        undo.push(new String[]{"ungroup", newGroup.name});
                        System.out.println("Group " + groupName + " has been created.");
                        break;
                    }
                    case "ungroup":{
                        if (!checkInputLength(parts, 2)) break;
                        String groupName = parts[index++];
                        if(!ungroup(groupName)) System.out.println("Group " + groupName + " not found.");
                        undo.push(new String[]{"regroup", groupName});
                        break ;
                    }//req7

                    case "intersect": {
                        if (!checkInputLength(parts, 3)) break;
                        Shape n1 = searchShape(parts[index++]);
                        Shape n2 = searchShape(parts[index++]);
                        if(n1 == null || n2 == null) break;
                        n1.list();
                        n2.list();
                        n1.initboundingbox();
                        n2.initboundingbox();
                        if (!(n1.boundingbox[0] > n2.boundingbox[0]+n2.boundingbox[2] || //(in y down format) n1 left > n2 right
                                n1.boundingbox[0]+n1.boundingbox[2] < n2.boundingbox[0] ||  //n1 right < n2 left
                                n1.boundingbox[1] + n1.boundingbox[3] < n2.boundingbox[1] ||  //n1 bot < n2 top
                                n1.boundingbox[1] > n1.boundingbox[1] + n2.boundingbox[3])){  //n1 top > n2 bot
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
                            this.undocoord.push(coord);
                            double x = coord[0];
                            double y = coord[1];
                            s.move(x,y);
                            undo.push(new String[]{"move", ShapeName});
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
                        boolean found = false;
                        for (Shape s:Shapes) {
                            if (shapeAt(x, y, s)) {
                                System.out.println( s.name + " is the first shape that covers the point (" + String.format("%.2f", x)+","+String.format("%.2f", y)+ ").");
                                found = true;
                                break;
                            }
                        }
                        if (found) System.out.println("No shape is covering the point (" + String.format("%.2f", x)+","+String.format("%.2f", y) + ").");
                        break;
                    }

                    case "undo":{
                        undo(false);
                        break;
                    }
                    case "redo":{
                        redo();
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
}