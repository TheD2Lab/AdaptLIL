package adaptlil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {
    private File outputfile;
    private BufferedWriter outputWriter;

    /**
     * SimpleLogger is used to write logs to a file.
     * @param outputFile
     */
    public SimpleLogger(File outputFile) {
        this.outputfile = outputFile;
        try {
            this.outputWriter = new BufferedWriter(new FileWriter(this.outputfile, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Log the string into this SimpleLogger's output file.
     * @param line
     */
    public void logLine(String line) {
        try {
            this.outputWriter.write(line);
            this.outputWriter.newLine();
            this.outputWriter.flush();
        } catch (IOException ignored) {

        }
    }

    /**
     * Print line to System.out and log the line in this SimpleLogger's file w/ option to disable prependTimestamp.
     * @param line
     */
    public void printAndLog(String line, boolean disablePrependedTimestamp) {
        this.printAndLog(line, System.out, disablePrependedTimestamp);
    }

    /**
     * Prepends timestamp and print line to System.out and log the line in this SimpleLogger's file.
     * @param line
     */
    public void printAndLog(String line) {
        this.printAndLog(line, System.out, true);
    }
    /**
     * Prints w/ timestamp prepended
     * @param line String to output to the stream and to log in SimpleLogger's log file
     * @param outStream Specify Printstream to output line to
     */
    private void printAndLog(String line, PrintStream outStream, boolean disablePrependTimestamp) {
        if (disablePrependTimestamp)
            this.logLine("["+System.currentTimeMillis() + '|' + this.getDateTimeStamp() + "]:" + line);
        else
            this.logLine(line);
        outStream.println(line);
    }

    /**
     * Return the current date and time formatted to yyyy-mm-dd hh:mm:ss
     * @return
     */
    public String getDateTimeStamp() {
        return (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis()))));
    }

}
