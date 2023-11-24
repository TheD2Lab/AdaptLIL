package analysis;

public class GazeMetrics {


    /**
     * TODO, Readme
     * We will start with three additional metrics. If the learning increases, we will continue adding more.
     */
    public int fixationCount;
    public int saccadeCount;


    public GazeMetrics(int fixationCount, int saccadeCount) {
        this.fixationCount = fixationCount;
        this.saccadeCount = saccadeCount;
    }
}
