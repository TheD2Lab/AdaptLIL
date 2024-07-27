package adaptlil.gazepoint.excel;

import java.io.File;

public class Participant {
    private String id;
    private String visType;

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

    public String getVisType() {
        return visType;
    }

    public void setVisType(String visType) {
        this.visType = visType;
    }

    public String toString() {
        return "id: " + getId() + "\n anatomFile: " +this.anatomyGazeFile.getAbsolutePath()
                +"\n anatomyFile fix: " + anatomyFixationFile.getAbsolutePath()
                + "\n anatFileanswer: " + anatomyAnswersFile.getAbsolutePath()
                + "\n conf gaze: " + confGazeFile.getAbsolutePath()
                + "\n conf fix: " + confFixationFile.getAbsolutePath()
                + "\n conf answer: " + confAnswersFile.getAbsolutePath()
                + "\nvisType: " + this.getVisType();
     }
}
