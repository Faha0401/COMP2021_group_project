package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.model.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


public class Clevis {

    List<Shape> shapes = new ArrayList<>();

    static int commandIndex = 0;
    Logger logger;

    Stack<Shape> Bin = new Stack<>();
    static Stack<String[]> undo = new Stack<>();
    static Stack<String[]> redo = new Stack<>();
    static Stack<double[]> undocoord = new Stack<>();
    static Stack<double[]> redocoord = new Stack<>();
    static boolean quitFlag = false;


    public Clevis() {
        System.out.println("Welcome to our clevis: ");
        Scanner scanner = new Scanner(System.in);
        while (!quitFlag) {
            Logger logger = new Logger("log.html", "log.txt");
            try {
                System.out.println("Please enter your operation: ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine();
                commandIndex = 0;
                logger.logCommand(commandIndex, line);
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0) continue;
                int index = 0;
                CommandParser.parseAndExecute(line, index, shapes, quitFlag, Bin, undo, redo, undocoord, redocoord);
                if (quitFlag) break;

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                logger.close();
            }
        }
    }

    public Clevis(boolean GUImode) {
        System.out.println("Welcome to our clevis: ");
        Logger logger = new Logger("log.html", "log.txt");
    }

    public void executeCommand(String line) {
        try {
            commandIndex++;
            logger.logCommand(commandIndex, line);
            int index = 0;
            CommandParser.parseAndExecute(line, index, shapes, quitFlag, Bin, undo, redo, undocoord, redocoord);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            logger.close();
        }
    }


    public List<Shape> getShapes() {
        return shapes;
    }


}
