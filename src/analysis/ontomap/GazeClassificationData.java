package analysis.ontomap;

import analysis.GazeMetrics;
import adaptovis.GazeWindow;

public class GazeClassificationData {
    public GazeWindow gazeWindow;
    public GazeMetrics gazeMetrics;

    public GazeClassificationData(GazeWindow gazeWindow, GazeMetrics gazeMetrics) {
        this.gazeWindow = gazeWindow;
        this.gazeMetrics = gazeMetrics;
    }
}
