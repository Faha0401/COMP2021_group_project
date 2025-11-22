package hk.edu.polyu.comp.comp2021.clevis.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  Logger class handles logging of commands to both HTML and text files.
 */
public class Logger {
    private PrintWriter htmlWriter;
    private PrintWriter txtWriter;

    /**
     * Constructor initializes the Logger with specified file paths.
     * @param htmlPath the path for the HTML log file
     * @param txtPath the path for the text log file
     */
    public Logger(String htmlPath, String txtPath) {
        try {
            htmlWriter = new PrintWriter(new FileWriter(htmlPath, true));
            txtWriter = new PrintWriter(new FileWriter(txtPath, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a command with its index to both HTML and text files.
     * @param index the index of the command
     * @param command the command string to log
     */
    public void logCommand(int index, String command) {
        htmlWriter.println("<tr><td>" + index + "</td><td>" + command + "</td></tr>");
        txtWriter.println(command);
        htmlWriter.flush();
        txtWriter.flush();
    }

    /**
     *  Closes the log files.
     */
    public void close() {
        htmlWriter.close();
        txtWriter.close();
    }
}
