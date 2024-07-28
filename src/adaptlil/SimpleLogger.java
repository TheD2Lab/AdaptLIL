package adaptlil;

import java.io.*;

public class SimpleLogger {
    private File outputfile;
    private BufferedInputStream inputStream;
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
     * Print to System.out and log the line argument in this SimpleLogger's file.
     * @param line
     */
    public void printAndLog(String line) {
        this.printAndLog(line, System.out);
    }

    /**
     *
     * @param line String to output to the stream and to log in SimpleLogger's log file
     * @param outStream Specify Printstream to output line to
     */
    public void printAndLog(String line, PrintStream outStream) {
        this.logLine(line);
        outStream.println(line);
    }

}
