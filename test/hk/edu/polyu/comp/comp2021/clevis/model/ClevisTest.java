package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * ClevisTest class contains unit tests for the Clevis application.
 */
public class ClevisTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    /**
     * restore method to restore original System input and output streams after each test.
     */
    @After
    public void restore() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String runClevisWithInput(String input) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        new Clevis(false);

        ps.flush();
        return baos.toString();
    }


    /**
     * testunexpected method to test handling of unexpected commands in the Clevis application.
     */
    @Test
    public void testunexpected(){
        String input = String.join("\n", Arrays.asList(
                "hi",
                "quit"

        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Unexpected Command."));

    }


    /**
     * testintersect method to test the intersect command in the Clevis application.
     */
    @Test
    public void testintersect(){
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
        assertTrue(out.contains("r1 does not intersect with r2."));

    }


    /**
     * testCreateAndListAll method to test creating shapes and listing all shapes in the Clevis application.
     */
    @Test
    public void testCreateAndListAll(){
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 10 5",
                "circle c1 5 5 2",
                "listAll",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Rectangle r1 has been created."));
        assertTrue(out.contains("Circle c1 has been created."));
        assertTrue(out.contains("Circle c1 x:5.00 y:5.00 radius:2.00"));
        assertTrue(out.contains("Rectangle r1 x:0.00 y:0.00 width:10.00 height:5.00"));
    }

    /**
     * testMoveAndShapeAt method to test moving a shape and checking which shape is at a specific point in the Clevis application.
     */
    @Test
    public void testMoveAndShapeAt(){
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

    /**
     * testGroupUngroupAndDelete method to test grouping, ungrouping, and deleting shapes in the Clevis application.
     */
    @Test
    public void testGroupUngroupAndDelete() {
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
        assertTrue(out.contains("Group G1 :"));
        assertTrue(out.contains("\tRectangle rA x:0.00 y:0.00 width:2.00 height:2.00"));
        assertTrue(out.contains("\tRectangle rB x:3.00 y:3.00 width:2.00 height:2.00"));
        assertTrue(out.contains("Group G1 has been ungrouped."));
        assertTrue(out.contains("Shape rA has been deleted."));
        assertTrue(out.contains("Rectangle rB x:3.00 y:3.00 width:2.00 height:2.00"));
    }

    /**
     * testCircleCreationAndListing method to test creating a circle and listing its details in the Clevis application.
     */
    @Test
    public void testCircleCreationAndListing() {
        String input = String.join("\n", Arrays.asList(
                "circle c1 5 5 2",
                "list c1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);

        assertTrue(out.contains("Circle c1 has been created."));
        assertTrue(out.contains("Circle c1 x:5.00 y:5.00 radius:2.00"));
    }

    /**
     * testMoveCircle method to test moving a circle and listing its updated details in the Clevis application.
     */
    @Test
    public void testMoveCircle() {
        String input = String.join("\n", Arrays.asList(
                "circle c1 5 5 2",
                "move c1 3 3",
                "list c1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);

        assertTrue(out.contains("Circle c1 has been created."));
        assertTrue(out.contains("Shape c1 is moved to (8.00,8.00)."));
        assertTrue(out.contains("Circle c1 x:8.00 y:8.00 radius:2.00"));
    }

    /**
     * testLineCreationAndListing method to test creating a line and listing its details in the Clevis application.
     */
    @Test
    public void testLineCreationAndListing() {
        String input = String.join("\n", Arrays.asList(
                "line l1 5 5 2 2",
                "list l1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Line l1 has been created."));
        assertTrue(out.contains("Line l1 x1:5.00 y1:5.00 x1:2.00 y2:2.00"));
    }

    /**
     * testMoveLine method to test moving a line and listing its updated details in the Clevis application.
     */
    @Test
    public void testMoveLine() {
        String input = String.join("\n", Arrays.asList(
                "line l1 5 5 2 2",
                "move l1 3 4",
                "list l1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Line l1 has been created."));
        assertTrue(out.contains("Shape l1 is moved to (8.00,9.00)."));
        assertTrue(out.contains("Line l1 x1:8.00 y1:9.00 x1:5.00 y2:6.00"));
    }

    /**
     * testSquareCreationAndListing method to test creating a square and listing its details in the Clevis application.
     */
    @Test
    public void testSquareCreationAndListing() {
        String input = String.join("\n", Arrays.asList(
                "square s1 2 2 5",
                "list s1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Square s1 has been created."));

        assertTrue(out.contains("Square s1 x:2.00 y:2.00 side width:5.00"));
    }

    /**
     * testMoveSquare method to test moving a square and listing its updated details in the Clevis application.
     */
    @Test
    public void testRectangleCreationAndListing() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 5 5 4 4",
                "list r1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Rectangle r1 has been created."));

        assertTrue(out.contains("Rectangle r1 x:5.00 y:5.00 width:4.00 height:4.00"));
    }

    /**
     * testMoveRectangle method to test moving a rectangle and listing its updated details in the Clevis application.
     */
    @Test
    public void testMoveRectangle() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 5 5 4 4",
                "move r1 3 4",
                "list r1",
                "quit"
        )) + "\n";

        String out = runClevisWithInput(input);


        assertTrue(out.contains("Rectangle r1 has been created."));

        assertTrue(out.contains("Shape r1 is moved to (8.00,9.00)."));
        assertTrue(out.contains("Rectangle r1 x:8.00 y:9.00 width:4.00 height:4.00"));
    }





    /**
     * testInitBoundingBox method to test initializing bounding boxes for various shapes in the Clevis application.
     */
    @Test
    public void testInitBoundingBox() {
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
        assertTrue(out.contains("Bounding Box: x:5.00 y:5.00 width:3.00 height3.00"));
        assertTrue(out.contains("Rectangle r1 has been created."));
        assertTrue(out.contains("Bounding Box: x:0.00 y:0.00 width:4.00 height3.00"));
        assertTrue(out.contains("Circle c1 has been created."));
        assertTrue(out.contains("Bounding Box: x:3.00 y:3.00 width:4.00 height4.00"));
        assertTrue(out.contains("Square s1 has been created."));
        assertTrue(out.contains("Bounding Box: x:2.00 y:2.00 width:5.00 height5.00"));


    }

    /**
     * testgroupedshape method to test grouping shapes and moving the group in the Clevis application.
     */
    @Test
    public void testgroupedshape() {
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

        assertTrue(out.contains("Group g1 :"));
        assertTrue(out.contains("\tRectangle r1 x:5.00 y:5.00 width:4.00 height:4.00"));
        assertTrue(out.contains("\tCircle c1 x:5.00 y:5.00 radius:2.00"));

        assertTrue(out.contains("Shape g1 is moved to (7.00,8.00)."));
    }

    /**
     * testundoRedoShapeCreation method to test undoing and redoing shape creation in the Clevis application.
     */
    @Test
    public void testUndoshapecreation() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "undo",
                "listAll",
                "list c1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("c1 has been removed"));
        assertTrue(out.contains("Rectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("Shape c1 is not found"));
    }

    /**
     * testredoShapeCreation method to test redoing shape creation in the Clevis application.
     */
    @Test
    public void testRedoshapecreation() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "undo",
                "redo",
                "listAll",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("c1 has been removed"));
        assertTrue(out.contains("Rectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("Circle c1 x:3.00 y:3.00 radius:3.00"));
    }

    /**
     * testundoShapeDeletion method to test undoing shape deletion in the Clevis application.
     */
    @Test
    public void testundoshapedeletion() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "delete c1",
                "undo",
                "listAll",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("c1 has been deleted"));
        assertTrue(out.contains("Rectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("Circle c1 x:3.00 y:3.00 radius:3.00"));
    }

    /**
     * testredoShapeDeletion method to test redoing shape deletion in the Clevis application.
     */
    @Test
    public void testredoshapedeletion() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "delete c1",
                "undo",
                "redo",
                "list c1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("c1 has been deleted"));
        assertTrue(out.contains("Shape c1 is not found."));
    }

    /**
     * testundoShapeGrouping method to test undoing shape grouping in the Clevis application.
     */
    @Test
    public void testundoshapegroping() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "group g1 r1 c1",
                "list g1",
                "undo",
                "list g1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Group g1 :"));
        assertTrue(out.contains("\tRectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("\tCircle c1 x:3.00 y:3.00 radius:3.00"));
        assertTrue(out.contains("Group g1 has been ungrouped."));
        assertTrue(out.contains("Shape g1 is not found."));
    }

    /**
     * testredoShapeGrouping method to test redoing shape grouping in the Clevis application.
     */
    @Test
    public void testredoshapegroping() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "group g1 r1 c1",
                "undo",
                "redo",
                "list g1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Group g1 has been ungrouped."));
        assertTrue(out.contains("Group g1 has been regrouped."));
        assertTrue(out.contains("Group g1 :"));
        assertTrue(out.contains("\tRectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("\tCircle c1 x:3.00 y:3.00 radius:3.00"));
    }

    /**
     * testundoShapeUngrouping method to test undoing shape ungrouping in the Clevis application.
     */
    @Test
    public void testundoshapeungroping() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "group g1 r1 c1",
                "ungroup g1",
                "undo",
                "list g1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Group g1 has been ungrouped."));
        assertTrue(out.contains("Group g1 has been regrouped."));
        assertTrue(out.contains("Group g1 :"));
        assertTrue(out.contains("\tRectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("\tCircle c1 x:3.00 y:3.00 radius:3.00"));
    }

    /**
     * testredoShapeUngrouping method to test redoing shape ungrouping in the Clevis application.
     */
    @Test
    public void testredoshapeungroping() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 3 3 3",
                "group g1 r1 c1",
                "ungroup g1",
                "undo",
                "redo",
                "list g1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Group g1 has been ungrouped."));
        assertTrue(out.contains("Group g1 has been regrouped."));
        assertTrue(out.contains("Shape g1 is not found."));
    }

    /**
     * testundoMoveShape method to test undoing a shape move in the Clevis application.
     */
    @Test
    public void testundomoveshape() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "move r1 1 1",
                "list r1",
                "undo",
                "list r1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Shape r1 is moved to (1.00,1.00)"));
        assertTrue(out.contains("Rectangle r1 x:1.00 y:1.00 width:5.00 height:5.00"));
        assertTrue(out.contains("r1 has moved back to (0.0,0.0)"));
        assertTrue(out.contains("Rectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
    }

    /**
     * testredoMoveShape method to test redoing a shape move in the Clevis application.
     */
    @Test
    public void testredomoveshape() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "move r1 1 1",
                "undo",
                "list r1",
                "redo",
                "list r1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Shape r1 is moved to (1.00,1.00)"));
        assertTrue(out.contains("r1 has moved back to (0.0,0.0)"));
        assertTrue(out.contains("Rectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("Shape r1 is moved to (1.00,1.00)"));
        assertTrue(out.contains("Rectangle r1 x:1.00 y:1.00 width:5.00 height:5.00"));
    }

    /**
     * testundoMoveGroupShape method to test undoing a group shape move in the Clevis application.
     */
    @Test
    public void testundomovegroupshape() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 1 1 2",
                "group g1 r1 c1",
                "boundingBox g1",
                "move g1 1 1",
                "boundingBox g1",
                "undo",
                "boundingBox g1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Bounding Box: x:-1.00 y:-1.00 width:5.00 height:5.00"));
        assertTrue(out.contains("Shape g1 is moved to (0.00,0.00)."));
        assertTrue(out.contains("Bounding Box: x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("g1 has moved back to (-1.0,-1.0)"));
        assertTrue(out.contains("Bounding Box: x:-1.00 y:-1.00 width:5.00 height:5.00"));
    }

    /**
     * testredoMoveGroupShape method to test redoing a group shape move in the Clevis application.
     */
    @Test
    public void testredomovegroupshape() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 1 1 2",
                "group g1 r1 c1",
                "move g1 1 1",
                "undo",
                "redo",
                "boundingBox g1",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Shape g1 is moved to (0.00,0.00)."));
        assertTrue(out.contains("g1 has moved back to (-1.0,-1.0)"));
        assertTrue(out.contains("Shape g1 is moved to (0.00,0.00)."));
        assertTrue(out.contains("Bounding Box: x:0.00 y:0.00 width:5.00 height:5.00"));
    }

    /**
     * testGroupingExistedGroup method to test grouping an existing group in the Clevis application.
     */
    @Test
    public void testGroupingExistedGroup() {
        String input = String.join("\n", Arrays.asList(
                "rectangle r1 0 0 5 5",
                "circle c1 1 1 2",
                "group g1 r1 c1",
                "rectangle r2 10 10 2 2",
                "group g2 g1 r2",
                "list g2",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("Group g1 has been created."));
        assertTrue(out.contains("Group g2 has been created."));
        assertTrue(out.contains("Group g2 :"));
        assertTrue(out.contains("\tRectangle r1 x:0.00 y:0.00 width:5.00 height:5.00"));
        assertTrue(out.contains("\tCircle c1 x:1.00 y:1.00 radius:2.00"));
        assertTrue(out.contains("\tRectangle r2 x:10.00 y:10.00 width:2.00 height:2.00"));
    }


}


