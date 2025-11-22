package hk.edu.polyu.comp.comp2021.clevis.model.shape;

import java.awt.*;

/**
 * Helper test shape to force very large world bounds so fitScale becomes very small
 * and the GUI clamps scale to MIN_SCALE.
 */
public class TestHugeBoundsShape extends Shape {
    private static final double HUGE_VALUE = 1e9;

    /**
     * Constructor to initialize a TestHugeBoundsShape with a name.
     */
    public TestHugeBoundsShape() {
        super("hugeTest");
    }

    @Override
    public void list() { }

    @Override
    public void move(double dx, double dy) { }

    @Override
    public void boundingbox() { }

    @Override
    public void initboundingbox() { }

    @Override
    public double getx() { return 0; }

    @Override
    public double gety() { return 0; }

    @Override
    public void computeWorldBounds(double[] bounds) {
        bounds[0] = Math.min(bounds[0], 0.0);
        bounds[1] = Math.min(bounds[1], 0.0);
        bounds[2] = Math.max(bounds[2], HUGE_VALUE);
        bounds[3] = Math.max(bounds[3], HUGE_VALUE);
    }

    @Override
    public void drawShape(Graphics2D g2d) { }
}
