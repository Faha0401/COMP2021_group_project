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
}
