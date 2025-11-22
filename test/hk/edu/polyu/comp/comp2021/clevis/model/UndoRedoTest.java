package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class UndoRedoTest {
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
    public void testemptyundoundo() throws Exception {
        String input = String.join("\n", Arrays.asList(
                "undo",
                "redo",
                "quit"
        )) + "\n";
        String out = runClevisWithInput(input);

        assertTrue(out.contains("There is no operation to be undo"));
        assertTrue(out.contains("There is no operation to be redo"));
    }

}