package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.model.shape.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 *  GUI class provides a graphical user interface for the Clevis application.
 */
public class GUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final double WORLD_MIN = 0.000001;
    private static final int PADDING = 100;
    private static final double CENTER_DIVISOR = 2.0;
    private final DrawingPanel drawingPanel;
    private final JTextField commandField;
    private final Clevis clevis;
    private static final double MIN_SCALE = 0.01;
    private static final double MAX_SCALE = 1000.0;

    /**
     * Constructor initializes the GUI components and layout.
     */
    public GUI() {
        setTitle("Clevis");
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        JPanel commandPanel = new JPanel(new BorderLayout());
        commandField = new JTextField();
        JButton executeButton = new JButton("Execute");

        clevis = new Clevis(true);

        commandPanel.add(new JLabel("Command: "), BorderLayout.WEST);
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(executeButton, BorderLayout.EAST);
        add(commandPanel, BorderLayout.SOUTH);

        ActionListener executeCommand = e -> {
            String command = commandField.getText().trim();
            clevis.executeCommand(command);
            commandField.setText("");
            if (!command.isEmpty()) {
                drawingPanel.repaint();
            }
        };

        commandField.addActionListener(executeCommand);
        executeButton.addActionListener(executeCommand);

        setVisible(true);
    }

    /**
     *  DrawingPanel is a custom JPanel for rendering shapes like a canvas.
     */
    class DrawingPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            List<Shape> shapes = clevis.getShapes();
            if (shapes.isEmpty()) return;
            double[] world = computeWorldBounds(shapes);
            double minX = world[0], minY = world[1], maxX = world[2], maxY = world[3];

            double worldWidth = Math.max(WORLD_MIN, maxX - minX);
            double worldHeight = Math.max(WORLD_MIN, maxY - minY);

            double fitScale = Math.min((getWidth() - PADDING * 2) / worldWidth, (getHeight() - PADDING * 2) / worldHeight);
            if (Double.isInfinite(fitScale) || Double.isNaN(fitScale) || fitScale <= 0)
                fitScale = 1.0;
            double worldCenterX = (minX + maxX) / CENTER_DIVISOR;
            double worldCenterY = (minY + maxY) / CENTER_DIVISOR;

            double scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, fitScale));

            AffineTransform at = new AffineTransform();
            at.translate(getWidth() / CENTER_DIVISOR, getHeight() / CENTER_DIVISOR);
            at.scale(scale, scale);
            at.translate(-worldCenterX, -worldCenterY);

            AffineTransform o = g2d.getTransform();
            g2d.setTransform(at);

            g2d.setColor(Color.BLACK);
            for (Shape shape : shapes) {
                drawShape(g2d, shape);
            }

            g2d.setTransform(o);
        }

        private double[] computeWorldBounds(List<Shape> shapes) {
            double[] bounds = new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
            for (Shape s : shapes) {
                s.computeWorldBounds(bounds);
            }
            if (bounds[0] == Double.POSITIVE_INFINITY || bounds[0] > bounds[2]){
                bounds[0] = 1;
                bounds[1] = 1;
                bounds[2] = 0;
                bounds[3] = 0;
            }
            return bounds;
        }

        private void drawShape(Graphics2D g2d, Shape shape) {
            shape.drawShape(g2d);
        }
    }

    /**
     * Main method to launch the GUI application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
