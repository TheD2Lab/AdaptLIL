package analysis;

public class GazeMetrics {


    /**
     * TODO, Readme
     * We will start with three additional metrics. If the learning increases, we will continue adding more.
     */
    public Integer fixationCount;
    public Integer saccadeCount;

    public Double meanFixationDuration;


    public GazeMetrics(int fixationCount, int saccadeCount, Double meanFixationDuration) {
        this.fixationCount = fixationCount;
        this.saccadeCount = saccadeCount;
        this.meanFixationDuration = meanFixationDuration;
    }

    public Integer getFixationCount() {
        return fixationCount;
    }

    public Integer getSaccadeCount() {
        return saccadeCount;
    }

    public Double getMeanFixationDuration() {
        return meanFixationDuration;
    }

    public void setFixationCount(Integer fixationCount) {
        this.fixationCount = fixationCount;
    }

    public void setSaccadeCount(Integer saccadeCount) {
        this.saccadeCount = saccadeCount;
    }

    public void setMeanFixationDuration(Double meanFixationDuration) {
        this.meanFixationDuration = meanFixationDuration;
    }
}
