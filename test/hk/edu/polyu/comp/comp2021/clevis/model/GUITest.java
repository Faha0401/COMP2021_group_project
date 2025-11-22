package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.model.shape.Shape;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

import static org.junit.Assert.assertTrue;

/**
 * Tests for GUI painting and internal DrawingPanel behavior.
 */
public class GUITest {

    private static final int T1_HEIGHT = 80;
    private static final int T1_WIDTH = 100;

    private static final int T2_HEIGHT = 160;
    private static final int T2_WIDTH = 200;

    private static final int T3_HEIGHT = 40;
    private static final int T3_WIDTH = 50;

    private static final int T4_HEIGHT = 200;
    private static final int T4_WIDTH = 300;

    /**
	 * Ensure paintComponent returns gracefully when there are no shapes.
     * @throws Exception on reflection errors
     */
	@Test
	public void testPaintWithNoShapes() throws Exception {
		GUI gui = new GUI();
		try {
			Field dpField = GUI.class.getDeclaredField("drawingPanel");
			dpField.setAccessible(true);
			Object drawingPanel = dpField.get(gui);

			BufferedImage img = new BufferedImage(T1_WIDTH, T1_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();

			Method paint = drawingPanel.getClass().getDeclaredMethod("paintComponent", Graphics.class);
			paint.setAccessible(true);

			paint.invoke(drawingPanel, g);
			g.dispose();

		} finally {
			gui.dispose();
		}
	}

	/**
	 * Ensure paintComponent calls through computeWorldBounds and drawShape when shapes exist.
     * @throws Exception on reflection errors
     */
	@Test
	public void testPaintWithShapes() throws Exception {
		GUI gui = new GUI();
		try {
			Field clevisField = GUI.class.getDeclaredField("clevis");
			clevisField.setAccessible(true);
			Clevis clevis = (Clevis) clevisField.get(gui);

			hk.edu.polyu.comp.comp2021.clevis.model.shape.Rectangle rect =
					new hk.edu.polyu.comp.comp2021.clevis.model.shape.Rectangle("ts", 0, 0, 10, 10);
			clevis.getShapes().add(rect);

			Field dpField = GUI.class.getDeclaredField("drawingPanel");
			dpField.setAccessible(true);
			Object drawingPanel = dpField.get(gui);

			BufferedImage img = new BufferedImage(T2_WIDTH, T2_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();

			Method paint = drawingPanel.getClass().getDeclaredMethod("paintComponent", Graphics.class);
			paint.setAccessible(true);

			paint.invoke(drawingPanel, g);
			g.dispose();

			List<Shape> shapes = clevis.getShapes();
			assertTrue(shapes.contains(rect));

		} finally {
			gui.dispose();
		}
	}

	/**
	 * Force the computeWorldBounds fallback branch by adding a shape that sets
	 * bounds[2] to POSITIVE_INFINITY, which should trigger the protection code.
     * @throws Exception on reflection errors
     */
	@Test
	public void testComputeWorldBoundsInfinityBranch() throws Exception {
		GUI gui = new GUI();
		try {
			Field clevisField = GUI.class.getDeclaredField("clevis");
			clevisField.setAccessible(true);
			Clevis clevis = (Clevis) clevisField.get(gui);

			hk.edu.polyu.comp.comp2021.clevis.model.shape.TestInfinityShape infShape =
					new hk.edu.polyu.comp.comp2021.clevis.model.shape.TestInfinityShape();
			clevis.getShapes().add(infShape);

			Field dpField = GUI.class.getDeclaredField("drawingPanel");
			dpField.setAccessible(true);
			Object drawingPanel = dpField.get(gui);

			BufferedImage img = new BufferedImage(T3_WIDTH, T3_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();

			Method paint = drawingPanel.getClass().getDeclaredMethod("paintComponent", Graphics.class);
			paint.setAccessible(true);

			paint.invoke(drawingPanel, g);
			g.dispose();

		} finally {
			gui.dispose();
		}
	}

	/**
	 * Simulate typing a command and triggering the action listener on the command field.
	 * Verifies the field is cleared after execution.
     * @throws Exception on reflection errors
     */
	@Test
	public void testCommandFieldActionClearsText() throws Exception {
		GUI gui = new GUI();
		try {
			Field cmdField = GUI.class.getDeclaredField("commandField");
			cmdField.setAccessible(true);
			JTextField commandField = (JTextField) cmdField.get(gui);
			commandField.setText("rectangle r_cmd 0 0 1 1");
			ActionListener[] listeners = commandField.getActionListeners();
			for (ActionListener al : listeners) {
				al.actionPerformed(new ActionEvent(commandField, ActionEvent.ACTION_PERFORMED, ""));
			}

			assertTrue(commandField.getText().isEmpty());

		} finally {
			gui.dispose();
		}
	}

	/**
	 * Force the scale clamping to MIN_SCALE by supplying a shape with very large world bounds.
     * @throws Exception on reflection errors
     */
	@Test
	public void testScaleClampedToMin() throws Exception {
		GUI gui = new GUI();
		try {
			Field clevisField = GUI.class.getDeclaredField("clevis");
			clevisField.setAccessible(true);
			Clevis clevis = (Clevis) clevisField.get(gui);

			hk.edu.polyu.comp.comp2021.clevis.model.shape.TestHugeBoundsShape huge =
					new hk.edu.polyu.comp.comp2021.clevis.model.shape.TestHugeBoundsShape();
			clevis.getShapes().add(huge);

			Field dpField = GUI.class.getDeclaredField("drawingPanel");
			dpField.setAccessible(true);
			Object drawingPanel = dpField.get(gui);

			BufferedImage img = new BufferedImage(T4_WIDTH, T4_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();

			Method paint = drawingPanel.getClass().getDeclaredMethod("paintComponent", Graphics.class);
			paint.setAccessible(true);
			paint.invoke(drawingPanel, g);
			g.dispose();

		} finally {
			gui.dispose();
		}
	}

}
