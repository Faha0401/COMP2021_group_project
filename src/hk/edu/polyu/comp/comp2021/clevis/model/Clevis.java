package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.model.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


/**
 * Clevis class represents the main application for managing shapes and executing commands.
 */
public class Clevis {
    private final List<Shape> shapes = new ArrayList<>();
    private final Logger logger = new Logger("log.html", "log.txt");
    private int commandIndex = 0;

    private final Stack<Shape> Bin = new Stack<>();
    private final Stack<String[]> undo = new Stack<>();
    private final Stack<String[]> redo = new Stack<>();
    private final Stack<double[]> undocoord = new Stack<>();
    private final Stack<double[]> redocoord = new Stack<>();
    private boolean quitFlag = false;


    /**
     *  Constructor initializes the Clevis application and starts the command input loop.
     * @param GUImode indicates whether the application is in GUI mode
     */
    public Clevis(boolean GUImode) {
        System.out.println("Welcome to our clevis: ");
        if (GUImode) return;
        Scanner scanner = new Scanner(System.in);
        while (!quitFlag) {
            try {
                System.out.println("Please enter your operation: ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine();
                commandIndex = 0;
                logger.logCommand(commandIndex, line);
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0) continue;
                CommandParser commandParser = new CommandParser(this);
                commandParser.parseAndExecute(line);
                if (quitFlag) break;

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                logger.close();
            }
        }
    }

    /**
     * Executes a command given as a string.
     * @param line the command string to execute
     */
    public void executeCommand(String line) {
        try {
            commandIndex++;
            logger.logCommand(commandIndex, line);
            CommandParser commandParser = new CommandParser(this);
            commandParser.parseAndExecute(line);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            logger.close();
        }
    }


    /**
     * Gets the list of shapes.
     * @return the list of shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
     * Gets the Bin stack.
     * @return the Bin stack
     */
    public Stack<Shape> getBin() {
        return Bin;
    }

    /**
     * Gets the undo stack.
     * @return the undo stack
     */
    public Stack<String[]> getUndo() {
        return undo;
    }

    /**
     * Gets the redo stack.
     * @return the redo stack
     */
    public Stack<String[]> getRedo() {
        return redo;
    }

    /**
     * Gets the undocoord stack.
     * @return the undocoord stack
     */
    public Stack<double[]> getUndocoord() {
        return undocoord;
    }

    /**
     * Gets the redocoord stack.
     * @return the redocoord stack
     */
    public Stack<double[]> getRedocoord() {
        return redocoord;
    }

    /**
     * Sets the quit flag to indicate whether to exit the application.
     * @param quitFlag the quit flag value
     */
    public void setQuitFlag(boolean quitFlag) {
        this.quitFlag = quitFlag;
    }
}
