package analysis;

import java.io.File;
import java.io.FileReader;

public class Participant {
    private String id;

    private File anatomyGazeFile;
    private File confGazeFile;
    private File confFixationFile;
    private File anatomyFixationFile;
    private File baselineGazeFile;
    private File baselineFixationFile;
    private File anatomyAnswersFile;
    private File confAnswersFile;
    public Participant(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public File getAnatomyGazeFile() {
        return anatomyGazeFile;
    }

    public void setAnatomyGazeFile(File anatomyGazeFile) {
        this.anatomyGazeFile = anatomyGazeFile;
    }

    public File getConfGazeFile() {
        return confGazeFile;
    }

    public void setConfGazeFile(File confGazeFile) {
        this.confGazeFile = confGazeFile;
    }

    public File getConfFixationFile() {
        return confFixationFile;
    }

    public void setConfFixationFile(File confFixationFile) {
        this.confFixationFile = confFixationFile;
    }

    public File getAnatomyFixationFile() {
        return anatomyFixationFile;
    }

    public void setAnatomyFixationFile(File anatomyFixationFile) {
        this.anatomyFixationFile = anatomyFixationFile;
    }

    public File getBaselineGazeFile() {
        return baselineGazeFile;
    }

    public void setBaselineGazeFile(File baselineGazeFile) {
        this.baselineGazeFile = baselineGazeFile;
    }

    public File getBaselineFixationFile() {
        return baselineFixationFile;
    }

    public void setBaselineFixationFile(File baselineFixationFile) {
        this.baselineFixationFile = baselineFixationFile;
    }

    public File getAnatomyAnswersFile() {
        return anatomyAnswersFile;
    }

    public void setAnatomyAnswersFile(File anatomyAnswersFile) {
        this.anatomyAnswersFile = anatomyAnswersFile;
    }

    public File getConfAnswersFile() {
        return confAnswersFile;
    }

    public void setConfAnswersFile(File confAnswersFile) {
        this.confAnswersFile = confAnswersFile;
    }
}
