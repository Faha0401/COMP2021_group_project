package hk.edu.polyu.comp.comp2021.clevis.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private PrintWriter htmlWriter;
    private PrintWriter txtWriter;

    public Logger(String htmlPath, String txtPath) {
        try {
            htmlWriter = new PrintWriter(new FileWriter(htmlPath, true));
            txtWriter = new PrintWriter(new FileWriter(txtPath, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logCommand(int index, String command) {
        htmlWriter.println("<tr><td>" + index + "</td><td>" + command + "</td></tr>");
        txtWriter.println(command);
        htmlWriter.flush();
        txtWriter.flush();
    }

    public void close() {
        htmlWriter.close();
        txtWriter.close();
    }
}
