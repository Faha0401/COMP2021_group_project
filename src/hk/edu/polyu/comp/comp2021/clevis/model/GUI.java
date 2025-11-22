package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.model.shape.Rectangle;
import hk.edu.polyu.comp.comp2021.clevis.model.shape.Shape;
import hk.edu.polyu.comp.comp2021.clevis.model.shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.List;

public class GUI extends JFrame {
    private DrawingPanel drawingPanel;
    private JTextField commandField;
    private Clevis clevis;
    private double scale = 1.0;
    private final double MIN_SCALE = 0.01;
    private final double MAX_SCALE = 1000.0;
    private double translateX = 0.0;
    private double translateY = 0.0;

    public GUI() {
        setTitle("Clevis");
        setSize(800,600);
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
            if (!command.isEmpty()) {
                clevis.executeCommand(command);
                commandField.setText("");
                drawingPanel.repaint();
            }
        };

        commandField.addActionListener(executeCommand);
        executeButton.addActionListener(executeCommand);

        setVisible(true);
    }

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
            double worldWidth = Math.max(0.000001, maxX - minX);
            double worldHeight = Math.max(0.000001, maxY - minY);

            double fitScale = Math.min((getWidth() - 2.0 * 20) / worldWidth, (getHeight() - 2.0 * 20) / worldHeight);
            if (Double.isInfinite(fitScale) || Double.isNaN(fitScale) || fitScale <= 0) fitScale = 1.0;
            double worldCenterX = (minX + maxX) / 2.0;
            double worldCenterY = (minY + maxY) / 2.0;

            scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, fitScale));
            translateX = worldCenterX;
            translateY = worldCenterY;

            AffineTransform at = new AffineTransform();
            at.translate(getWidth() / 2.0, getHeight() / 2.0);
            at.scale(scale, scale);
            at.translate(-translateX, -translateY);

            AffineTransform o = g2d.getTransform();
            g2d.setTransform(at);

            g2d.setColor(Color.BLACK);
            for (Shape shape : shapes) {
                drawShape(g2d, shape);
            }

            g2d.setTransform(o);
        }

        private double[] computeWorldBounds(List<Shape> shapes) {
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            for (Shape s : shapes) {
                if (s instanceof Rectangle) {
                    Rectangle r = (Rectangle) s;
                    double x = r.getx();
                    double y = r.gety();
                    double w = r.getWidth();
                    double h = r.getHeight();
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x + w);
                    maxY = Math.max(maxY, y + h);
                } else if (s instanceof Circle) {
                    Circle c = (Circle) s;
                    double x = c.getx();
                    double y = c.gety();
                    double r = c.getRadius();
                    minX = Math.min(minX, x - r);
                    minY = Math.min(minY, y - r);
                    maxX = Math.max(maxX, x + r);
                    maxY = Math.max(maxY, y + r);
                } else if (s instanceof Square) {
                    Square q = (Square) s;
                    double x = q.getx();
                    double y = q.gety();
                    double l = q.getSide();
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x + l);
                    maxY = Math.max(maxY, y + l);
                } else if (s instanceof Line) {
                    Line l = (Line) s;
                    minX = Math.min(minX, Math.min(l.getX1(), l.getX2()));
                    minY = Math.min(minY, Math.min(l.getY1(), l.getY2()));
                    maxX = Math.max(maxX, Math.max(l.getX1(), l.getX2()));
                    maxY = Math.max(maxY, Math.max(l.getY1(), l.getY2()));
                } else if (s instanceof GroupedShape) {
                    GroupedShape g = (GroupedShape) s;
                    for (Shape child : g.getGroup()) {
                        double[] b = computeWorldBounds(java.util.Collections.singletonList(child));
                        minX = Math.min(minX, b[0]);
                        minY = Math.min(minY, b[1]);
                        maxX = Math.max(maxX, b[2]);
                        maxY = Math.max(maxY, b[3]);
                    }
                }
            }
            if (minX == Double.POSITIVE_INFINITY) {
                minX = minY = 0;
                maxX = maxY = 1;
            }
            return new double[]{minX, minY, maxX, maxY};
        }

        private void drawShape(Graphics2D g2d, Shape shape) {
            if (shape instanceof Rectangle) {
                Rectangle r = (Rectangle) shape;
                int x = (int) Math.round(r.getx());
                int y = (int) Math.round(r.gety());
                int w = (int) Math.round(r.getWidth());
                int h = (int) Math.round(r.getHeight());
                g2d.drawRect(x, y, w, h);
            } else if (shape instanceof Circle) {
                Circle c = (Circle) shape;
                int cx = (int) Math.round(c.getx());
                int cy = (int) Math.round(c.gety());
                int rr = (int) Math.round(c.getRadius());
                g2d.drawOval(cx - rr, cy - rr, 2 * rr, 2 * rr);
            } else if (shape instanceof Square) {
                Square s = (Square) shape;
                int x = (int) Math.round(s.getx());
                int y = (int) Math.round(s.gety());
                int side = (int) Math.round(s.getSide());
                g2d.drawRect(x, y, side, side);
            } else if (shape instanceof Line) {
                Line l = (Line) shape;
                int x1 = (int) Math.round(l.getX1());
                int y1 = (int) Math.round(l.getY1());
                int x2 = (int) Math.round(l.getX2());
                int y2 = (int) Math.round(l.getY2());
                g2d.drawLine(x1, y1, x2, y2);
            } else if (shape instanceof GroupedShape) {
                GroupedShape gshape = (GroupedShape) shape;
                for (Shape s : gshape.getGroup()) {
                    drawShape(g2d, s);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
