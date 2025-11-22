package hk.edu.polyu.comp.comp2021.clevis.model.shape;

/**
 * Helper test shape to force computeWorldBounds special branch.
 */
public class TestInfinityShape extends Shape {

    /**
     * Constructor to initialize a TestInfinityShape with a name.
     */
    public TestInfinityShape() {
        super("infTest");
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
        bounds[2] = Double.POSITIVE_INFINITY; // force branch
    }

    @Override
    public void drawShape(java.awt.Graphics2D g2d) { }
}
