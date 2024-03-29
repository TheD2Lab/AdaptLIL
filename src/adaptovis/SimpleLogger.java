package adaptovis;

import java.io.*;

public class SimpleLogger {
    private File outputfile;
    private BufferedInputStream inputStream;
    private BufferedWriter outputWriter;
    public SimpleLogger(File outputFile) {
        this.outputfile = outputFile;
        try {
            this.outputWriter = new BufferedWriter(new FileWriter(this.outputfile, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void logLine(String line) {

        try {

            this.outputWriter.write(line);
            this.outputWriter.newLine();
            this.outputWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
