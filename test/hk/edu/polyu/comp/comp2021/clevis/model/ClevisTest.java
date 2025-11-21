package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class ClevisTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @After
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String runClevisWithInput(String input) throws Exception {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        new Clevis();

        ps.flush();
        return baos.toString();
    }


    @Test
    public void testunexpected() throws Exception{
        String input = String.join("\n", Arrays.asList(
                "hi",
                "quit"

        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Unexpected Command."));

    }


    @Test
    public void testintersect() throws Exception{
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 10 5",
                "circle c1 5 5 2",
                "intersect",
                "intersect r1 c1",
                "rectangle r2 100 100 10 5",
                "intersect r1 r2",
                "quit"

        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("[Error]: expected 3 values, but got 1."));
        assertTrue(out.contains("r1 intersect with c1."));
        assertTrue(out.contains("r1 does not intersect with r2."));//need to pass this

    }


    @Test
    public void testCreateAndListAll() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 10 5",
                "circle c1 5 5 2",
                "listAll",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Rectangle r1 has been created."));
        assertTrue(out.contains("Circle c1 has been created."));
        assertTrue(out.contains("Circle c1 x:5.0 y:5.0 radius:2.0"));
        assertTrue(out.contains("Rectangle r1 x:0.0 y:0.0 width:10.0 height:5.0"));
    }

    @Test
    public void testMoveAndShapeAt() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "square s1 1 1 2",
                "move s1 3 4",
                "shapeAt 4 5",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Square s1 has been created."));
        assertTrue(out.contains("Shape s1 is moved to (4.00,5.00)."));
        assertTrue(out.contains("s1 is the first shape that covers the point (4.00,5.00)."));
    }

    @Test
    public void testGroupUngroupAndDelete() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "rectangle rA 0 0 2 2",
                "rectangle rB 3 3 2 2",
                "group G1 rA rB",
                "list G1",
                "ungroup G1",
                "delete rA",
                "listAll",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Rectangle rA has been created."));
        assertTrue(out.contains("Rectangle rB has been created."));
        assertTrue(out.contains("Group G1 has been created."));
        assertTrue(out.contains("Group shape :G1"));
        assertTrue(out.contains("\tRectangle rA x:0.0 y:0.0 width:2.0 height:2.0"));
        assertTrue(out.contains("\tRectangle rB x:3.0 y:3.0 width:2.0 height:2.0"));
        assertTrue(out.contains("Group G1 has been ungrouped."));
        assertTrue(out.contains("Shape rA has been deleted."));
        assertTrue(out.contains("Rectangle rB x:3.0 y:3.0 width:2.0 height:2.0"));
    }

    @Test
    public void testCircleCreationAndListing() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "circle c1 5 5 2",
                "list c1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);

        // Verifies that the creation message is printed
        assertTrue(out.contains("Circle c1 has been created."));
        // Verifies that the properties of the circle are listed correctly
        assertTrue(out.contains("Circle c1 x:5.0 y:5.0 radius:2.0"));
    }

    @Test
    public void testMoveCircle() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "circle c1 5 5 2",
                "move c1 3 3",
                "list c1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);

        // Verifies that the circle has been created
        assertTrue(out.contains("Circle c1 has been created."));
        // Verifies that the circle has been moved to the new coordinates
        assertTrue(out.contains("Shape c1 is moved to (8.00,8.00)."));
        // Verifies that the properties of the circle reflect the new position
        assertTrue(out.contains("Circle c1 x:8.0 y:8.0 radius:2.0"));
    }

    @Test
    public void testLineCreationAndListing() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "line l1 5 5 2 2",  // Command to create a circle
                "list l1",          // Command to list the properties of the circle
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);

        // Verifies that the creation message is printed
        assertTrue(out.contains("Line l1 has been created."));
        // Verifies that the properties of the circle are listed correctly
        assertTrue(out.contains("Line l1 x1:5.0 y1:5.0 x1:2.0 y2:2.0"));
    }
    @Test
    public void testMoveLine() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "line l1 5 5 2 2",
                "move l1 3 4",
                "list l1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);

        // Verifies that the circle has been created
        assertTrue(out.contains("Line l1 has been created."));
        // Verifies that the circle has been moved to the new coordinates
        assertTrue(out.contains("Shape l1 is moved to (8.00,9.00)."));
        // Verifies that the properties of the circle reflect the new position
        assertTrue(out.contains("Line l1 x1:8.0 y1:9.0 x1:5.0 y2:6.0"));
    }
    @Test
    public void testSquareCreationAndListing() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "square s1 2 2 5",
                "list s1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Square s1 has been created."));

        assertTrue(out.contains("Square s1 x:2.0 y:2.0 side width:5.0"));
    }

    @Test
    public void testRectangleCreationAndListing() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 5 5 4 4",
                "list r1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Rectangle r1 has been created."));

        assertTrue(out.contains("Rectangle r1 x:5.0 y:5.0 width:4.0 height:4.0"));
    }

    @Test
    public void testMoveRectangle() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 5 5 4 4",
                "move r1 3 4",
                "list r1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Rectangle r1 has been created."));

        assertTrue(out.contains("Shape r1 is moved to (8.00,9.00)."));
        assertTrue(out.contains("Rectangle r1 x:8.0 y:9.0 width:4.0 height:4.0"));
    }





    @Test
    public void testInitBoundingBox() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "line l1 5 5 2 2",
                "boundingBox l1",
                "rectangle r1 0 0 4 3",
                "boundingBox r1",
                "circle c1 5 5 2",
                "boundingBox c1",
                "square s1 2 2 5",
                "boundingBox s1",

                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Line l1 has been created."));
        assertTrue(out.contains("Bounding Box: x:5.00 y:5.00 width:-3.00 height-3.00"));
        assertTrue(out.contains("Rectangle r1 has been created."));
        assertTrue(out.contains("Bounding Box: x:0.00 y:0.00 width:4.00 height3.00"));
        assertTrue(out.contains("Circle c1 has been created."));
        assertTrue(out.contains("Bounding Box: x:3.00 y:3.00 width:4.00 height4.00"));
        assertTrue(out.contains("Square s1 has been created."));
        assertTrue(out.contains("Bounding Box: x:2.00 y:2.00 width:5.00 height5.00"));


    }

    @Test
    public void testgroupedshape() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 5 5 4 4",
                "circle c1 5 5 2",
                "group g1 r1 c1",
                "list g1",
                "move g1 4 5",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Group g1 has been created."));

        assertTrue(out.contains("Group shape :g1\n" +
                "\tRectangle r1 x:5.0 y:5.0 width:4.0 height:4.0\n" +
                "\tCircle c1 x:5.0 y:5.0 radius:2.0"));
        assertTrue(out.contains("Shape g1 is moved to (7.00,8.00)."));
    }


}
