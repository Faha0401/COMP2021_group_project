package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.model.shape.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;


/**
 * CommandParser class is for parsing and executing commands related to shape management.
 * It supports operations such as creating shapes, listing shapes, deleting shapes,
 * grouping/ungrouping shapes, checking intersections, moving shapes, and undo/redo functionality
 * of commands.
 */
public class CommandParser {
    private final Clevis clevis;
    private final List<Shape> shapes;
    private final Stack<Shape> Bin;
    private final Stack<String[]> undo;
    private final Stack<String[]> redo;
    private final Stack<double[]> undocoord;
    private final Stack<double[]> redocoord;

    /**
     * Constructor for CommandParser, initializing with a Clevis instance and retrieve data from a Celvis instance.
     * @param clevis the Clevis instance containing shape data and command history.
     */
    public CommandParser(Clevis clevis) {
        this.clevis = clevis;
        shapes = clevis.getShapes();
        Bin = clevis.getBin();
        undo = clevis.getUndo();
        redo = clevis.getRedo();
        undocoord = clevis.getUndocoord();
        redocoord = clevis.getRedocoord();
    }

    /**
     * Parses and executes a given command string.
     * @param command the command string to be parsed and executed.
     */
    public void parseAndExecute(String command) {
        int index = 0;
        String[] parts = command.trim().split("\\s+");
        if (parts.length == 0) return;
        String operation = parts[index++];
        switch (operation) {
            case "quit": {
                clevis.setQuitFlag(true);
                System.out.println("The application has been terminated.");
                break;
            }

            case "rectangle": {
                if (!checkInputLength(parts, Rectangle.EXPECTED_VALUES + 2)) break;
                String name = parts[index++];
                if (ExistName(name, shapes)) break;

                double[] values = ReadValues(parts, Rectangle.EXPECTED_VALUES, index);
                if (values == null) break;
                shapes.add(0, new Rectangle(name, values[0], values[1], values[2], values[3]));
                undo.push(new String[]{"create", name});
                System.out.println("Rectangle " + name + " has been created.");
                break;
            }

            case "line": {
                if (!checkInputLength(parts, Line.EXPECTED_VALUES + 2)) break;
                String name = parts[index++];
                if (ExistName(name, shapes)) break;

                double[] values = ReadValues(parts, Line.EXPECTED_VALUES, index);
                if (values == null) break;
                shapes.add(0, new Line(name, values[0], values[1], values[2], values[3]));
                undo.push(new String[]{"create", name});
                System.out.println("Line " + name + " has been created.");
                break;
            }

            case "circle": {
                if (!checkInputLength(parts, Circle.EXPECTED_VALUES + 2)) break;
                String name = parts[index++];
                if (ExistName(name, shapes)) break;

                double[] values = ReadValues(parts, Circle.EXPECTED_VALUES, index);
                if (values == null) break;
                shapes.add(0, new Circle(name, values[0], values[1], values[2]));
                undo.push(new String[]{"create", name});
                System.out.println("Circle " + name + " has been created.");
                break;
            }

            case "square": {
                if (!checkInputLength(parts, Square.EXPECTED_VALUES + 2)) break;
                String name = parts[index++];
                if (ExistName(name, shapes)) break;

                double[] values = ReadValues(parts, Square.EXPECTED_VALUES, index);
                if (values == null) break;
                shapes.add(0, new Square(name, values[0], values[1], values[2]));
                undo.push(new String[]{"create", name});
                System.out.println("Square " + name + " has been created.");
                break;
            }
            case "list": {
                if (!checkInputLength(parts, 2)) break;
                String name = parts[index];
                Shape s = searchShape(name, shapes);
                if (s != null) {
                    s.list();
                }
                break;
            }

            case "listAll": {
                for (Shape s : shapes) {
                    s.list();
                }
                break;
            }

            case "delete": {
                if (!checkInputLength(parts, 2)) break;
                String delName = parts[index];
                undo.push(new String[]{"delete", delName});
                Shape delshape = searchShape(delName, shapes);
                if(delshape!=null) Bin.push(delshape);
                delete(delName, shapes);
                break;
            }

            case "group": {
                if (parts.length < 4) {
                    System.out.println("[Error]: expected at least 4 values, but got " + parts.length + ".");
                    break;
                }
                String groupName = parts[index++];
                if (ExistName(groupName, shapes)) break;
                GroupedShape newGroup = new GroupedShape(groupName);
                String[] shapeName = Arrays.copyOfRange(parts, index, parts.length);
                if (shapeName.length == 0) {
                    System.out.println("No shape is selected.");
                }
                boolean aborted = false;
                for (String s : shapeName) {
                    Shape shape = searchShape(s, shapes);
                    if (shape == null) {
                        System.out.println("Group creation aborted.");
                        aborted = true;
                        break;
                    }
                    newGroup.add(shape);
                    shapes.remove(shape);
                }
                if (aborted) break;
                shapes.add(0, newGroup);
                undo.push(new String[]{"ungroup", newGroup.getName()});
                System.out.println("Group " + groupName + " has been created.");
                break;
            }

            case "ungroup": {
                if (!checkInputLength(parts, 2)) break;
                String groupName = parts[index];
                if(!ungroup(groupName, shapes, Bin)) System.out.println("Group " + groupName + " not found.");
                undo.push(new String[]{"regroup", groupName});
                break ;
            }

            case "intersect": {
                if (!checkInputLength(parts, 3)) break;
                Shape n1 = searchShape(parts[index++], shapes);
                Shape n2 = searchShape(parts[index], shapes);
                if (n1 == null || n2 == null) break;
                n1.initboundingbox();
                n2.initboundingbox();
                double[] boundingbox1 = n1.getBoundingbox();
                double[] boundingbox2 = n2.getBoundingbox();
                if (!(boundingbox1[0] > boundingbox1[0]+boundingbox2[2] || //(in y down format) n1 left > n2 right
                        boundingbox1[0]+boundingbox1[2] < boundingbox2[0] ||  //n1 right < n2 left
                        boundingbox1[1] + boundingbox1[3] < boundingbox2[1] ||  //n1 bot < n2 top
                        boundingbox1[1] > boundingbox1[1] + boundingbox2[3])){  //n1 top > n2 bot
                    System.out.println(n1.getName() + " intersect with " + n2.getName() + ".");
                    break;
                }
                System.out.println(n1.getName() + " does not intersect with " + n2.getName() + ".");
                break;
            }

            case "boundingBox": {
                if (!checkInputLength(parts, 2)) break;
                String ShapeName = parts[index];
                Shape s = searchShape(ShapeName, shapes);
                if (s != null) s.boundingbox();
                break;
            }

            case "move": {
                if (!checkInputLength(parts, 4)) break;
                String ShapeName = parts[index++];
                Shape s = searchShape(ShapeName, shapes);
                double[] coord = ReadValues(parts, 2, index);
                undocoord.push(coord);
                if (s != null && coord != null) {
                    double x = coord[0];
                    double y = coord[1];
                    s.move(x, y);
                    undo.push(new String[]{"move", ShapeName});
                    System.out.println("Shape " + ShapeName + " is moved to (" + String.format("%.2f", s.getx()) + "," + String.format("%.2f", s.gety()) + ").");
                    break;
                }

            }

            case "shapeAt": {
                boolean flag = true;
                if (!checkInputLength(parts, 3)) break;
                double[] coord = ReadValues(parts, 2, index);
                if (coord == null) break;
                double x = coord[0];
                double y = coord[1];
                for (Shape s : shapes) {
                    if (shapeAt(x, y, s)) {
                        System.out.println(s.getName() + " is the first shape that covers the point (" + String.format("%.2f", x) + "," + String.format("%.2f", y) + ").");
                        flag = false;
                        break;
                    }
                }
                if (flag) System.out.println("No shape is covering the point (" + String.format("%.2f", x) + "," + String.format("%.2f", y) + ").");
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
                break;
        }

    }

    /**
     *  Redoes the last undone operation.
     */
    private void redo(){
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
                Shape shape = searchShape(curRndo[1], shapes);
                double[] dest = redocoord.pop();
                undocoord.push(new double[]{shape.getx(), shape.gety()});
                shape.move(dest[0], dest[1]);
                undo.push(new String[]{"move", curRndo[1]});
                break;
            }
        }
        undo(true);
    }

    /**
     * Undoes the last operation performed.
     * @param isredo indicates if the undo operation is part of a redo action.
     */
    private void undo(boolean isredo){
        if(undo.empty()){
            System.out.println("There is no operation to be undo");
            return;
        }
        String[] curUndo = undo.pop();
        switch (curUndo[0]){
            case("create"):{
                Shape shape = searchShape(curUndo[1], shapes);
                shapes.remove(shape);
                Bin.push(shape);
                System.out.println(shape.getName() + " has been removed.");
                break;}
            case("delete"):{
                Shape shape = Bin.pop();
                shapes.add(shape);
                System.out.println(shape.getName() + " has been added.");
                break;}
            case("ungroup"):{
                Shape shape = searchShape(curUndo[1], shapes);
                ungroup(shape.getName(), shapes, Bin);
                break;}
            case("regroup"):{
                Shape shape = Bin.pop();
                for(Shape s : ((GroupedShape) shape).getGroup()){
                    delete(s.getName(), shapes);
                }
                shapes.add(shape);
                System.out.println("Group " + shape.getName() + " has been regrouped.");
                break;}
            case("move"):{
                Shape shape = searchShape(curUndo[1], shapes);
                double[] dest = undocoord.pop();
                redocoord.push(new double[]{shape.getx(), shape.gety()});
                shape.move(dest[0]*-1, dest[1]*-1);
                System.out.println(shape.getName() + " has moved back to " + "(" + shape.getx() + "," + shape.gety() + ")");
                break;
            }
            default:
                System.out.println("An error has occur while undo.");
                break;
        }
        if(!isredo) redo.push(curUndo);
    }

    /**
     * Deletes a shape by its name from the provided list of shapes.
     * @param name the name of the shape to be deleted.
     * @param shapes the list of shapes from which the shape will be deleted.
     */
    private void delete(String name, List<Shape> shapes) {
        if (shapes.removeIf(s -> s.getName().equals(name))) {
            System.out.println("Shape " + name + " has been deleted.");
        } else {
            System.out.println("Shape " + name + " is not found.");
        }
    }
    /**
     * Checks if a shape with the given name already exists in the provided list of shapes.
     * @param name the name to check for existence.
     * @param shapes the list of shapes to check within.
     * @return true if a shape with the given name exists, false otherwise.
     */
    private boolean ExistName(String name, List<Shape> shapes) {
        for (Shape ungroupShape : shapes) {
            if (ungroupShape.getName().equals(name)) {
                System.out.println("The name " + name + " is used, please enter another name.");
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for a shape by its name in the provided list of shapes.
     * @param name the name of the shape to search for.
     * @param shapes the list of shapes to search within.
     * @return the shape if found, null otherwise.
     */
    public Shape searchShape(String name, List<Shape> shapes){ //search for ungroup shape
        for(Shape e: shapes){
            if (e.getName().equals(name)){
                return e;
            }
        }
        System.out.println("Shape " + name + " is not found.");
        return null;
    }

    /**
     * Checks if the input length matches the expected length, prints an error message if it is not.
     * @param parts the array of input parts.
     * @param expectedLength the expected length of the input.
     * @return true if the input length matches the expected length, false otherwise.
     */
    public boolean checkInputLength(String[] parts, int expectedLength) {
        if (parts.length != expectedLength) {
            System.out.println("[Error]: expected " + expectedLength + " values, but got " + parts.length + ".");
            return false;
        }
        return true;
    }

    /**
     * Reads a specified number of double values from the input parts starting at a given index.
     * @param parts the array of input parts.
     * @param numberOfValues the number of double values to read.
     * @param index the starting index in the parts array.
     * @return an array of double values if successful, null if any value is invalid.
     */
    private double[] ReadValues(String[] parts, int numberOfValues, int index) {
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

    /**
     * Checks if a point (x, y) is on the border of a given shape.
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @param e the shape to check against.
     * @return true if the point is on the border of the shape, false otherwise.
     */
    private boolean shapeAt(double x, double y, Shape e){
        final double TOLERANCE = 0.05;
        e.initboundingbox();
        double [] boundingbox = e.getBoundingbox();
        return (x > boundingbox[0] - TOLERANCE && x < boundingbox[0] + TOLERANCE && y < boundingbox[1] + TOLERANCE && y > boundingbox[1] + boundingbox[3] - TOLERANCE) ||
                (x > boundingbox[0] + boundingbox[2] - TOLERANCE && x < boundingbox[0] + boundingbox[2] + TOLERANCE && y < boundingbox[1] + TOLERANCE && y > boundingbox[1] + boundingbox[3] - TOLERANCE) ||
                (y > boundingbox[1] - TOLERANCE && y < boundingbox[1] + TOLERANCE && x > boundingbox[0] - TOLERANCE && x < boundingbox[0] + boundingbox[2] + TOLERANCE) ||
                (y > boundingbox[1] + boundingbox[3] - TOLERANCE && y < boundingbox[1] + boundingbox[3] + TOLERANCE && x > boundingbox[0] - TOLERANCE && x < boundingbox[0] + boundingbox[2] + TOLERANCE);
    }

    /**
     * Ungroups a grouped shape by its name, adding its constituent shapes back to the main shape list.
     * @param groupName the name of the group to ungroup.
     * @param shapes the list of shapes to modify.
     * @param Bin the stack to store the ungrouped shape.
     * @return true if the group was found and ungrouped, false otherwise.
     */
    private boolean ungroup(String groupName, List<Shape> shapes, Stack<Shape> Bin) {
        for (Shape s : shapes) {
            if (s.getName().equals(groupName) && s instanceof GroupedShape) {
                Bin.push(s);
                shapes.addAll(((GroupedShape) s).getGroup());
                shapes.remove(s);
                shapes.sort(Comparator.comparingInt(Shape::getzIndex).reversed());
                System.out.println("Group " + groupName + " has been ungrouped.");
                return true;
            }
        }
        return false;
    }
}
