package analysis;

import data_classes.Fixation;
import data_classes.Saccade;
import wekaext.annotations.IgnoreWekaAttribute;

import java.util.ArrayList;
import java.util.List;

public class GazeMetrics {


    /**
     * TODO, Readme
     * We will start with three additional metrics. If the learning increases, we will continue adding more.
     */
    public Integer fixationCount;
    public Integer saccadeCount;


    @IgnoreWekaAttribute
    private List<Fixation> fixations;

    @IgnoreWekaAttribute
    private List<Saccade> saccades;

    public Double sumOfSaccadeLen;
    public Double meanSaccadeLen;
    public Double medianSaccadeLen;
    public Double stdOfSaccadeLen;
    public Double minSaccadeLen = Double.MAX_VALUE;
    public Double maxSaccadeLen = Double.MIN_VALUE;

    public Double sumOfFixationDuration;
    public Double meanFixationDuration;
    public Double medianFixationDuration;
    public Double stdOfFixationDuration;
    public Double minFixationDuration = Double.MAX_VALUE;
    public Double maxFixationDuration = Double.MIN_VALUE;

    public Double sumOfSaccadeDurations;
    public Double meanSaccadeDuration;
    public Double medianSaccadeDuration;
    public Double stdOfSaccadeDurations;
    public Double minSaccadeDuration = Double.MAX_VALUE;
    public Double maxSaccadeDuration = Double.MIN_VALUE;

    public Double scanpathDuration;
    public Double fixationToSaccadeRatio;

    public Double sumOfAbsoluteDegrees;
    public Double meanAbsoluteDegree;
    public Double medianAbsoluteDegree;
    public Double stdOfAbsoluteDegrees;
    public Double minAbsoluteDegree = Double.MAX_VALUE;
    public Double maxAbsoluteDegree = Double.MIN_VALUE;

    public Double sumOfRelativeDegrees;
    public Double meanRelativeDegree;
    public Double medianRelativeDegree;
    public Double stdOfRelativeDegrees;
    public Double minRelativeDegree = Double.MAX_VALUE;
    public Double maxRelativeDegree = Double.MIN_VALUE;



    public GazeMetrics(List<Fixation> fixations, List<Saccade> saccades) {

        this.fixations = fixations;
        this.saccades = saccades;

        this.calculateDescriptiveMetrics();

    }

    public void calculateDescriptiveMetrics() {
        this.calculateFixationMetaData();
        this.calculateSaccadeMetaData();
        this.calculateScanpathMetaData();
        this.calculateRelativeDegreeMetaData();
        this.calculateAbsoluteDegreeMetaData();
    }

    public void calculateFixationMetaData() {
        this.fixationCount = fixations.size();
        this.sumOfFixationDuration = 0.0;
        List<Double> durations = new ArrayList<>();

        for (Fixation fixation : this.fixations) {
            durations.add(fixation.getDuration());
            this.sumOfFixationDuration += fixation.getDuration();

            if (fixation.getDuration() < this.minFixationDuration)
                this.minFixationDuration = fixation.getDuration();

            if (fixation.getDuration() > this.maxFixationDuration)
                this.maxFixationDuration = fixation.getDuration();
        }

        this.medianFixationDuration = descriptiveStats.getMedianOfDoubles(durations);
        this.stdOfFixationDuration = descriptiveStats.getStDevOfDoubles(durations);
        this.meanFixationDuration = this.sumOfFixationDuration / this.fixationCount;

    }

    public void calculateSaccadeMetaData() {
        this.saccadeCount = saccades.size();

        this.calculateSaccadeDurationMetaData();
        this.calculateSaccadeLenMetaData();
    }

    public void calculateSaccadeDurationMetaData() {
        this.sumOfSaccadeDurations = 0.0;
        List<Double> durations = new ArrayList<>();

        for (Saccade saccade : this.saccades) {
            durations.add(saccade.getDuration());
            this.sumOfSaccadeDurations += saccade.getDuration();

            if (saccade.getDuration() < this.minSaccadeDuration)
                this.minSaccadeDuration = saccade.getDuration();

            if (saccade.getDuration() > this.maxSaccadeDuration)
                this.maxSaccadeDuration = saccade.getDuration();
        }

        this.medianSaccadeDuration = descriptiveStats.getMedianOfDoubles(durations);
        this.stdOfSaccadeDurations = descriptiveStats.getStDevOfDoubles(durations);
        this.saccadeCount = saccades.size();
        this.meanSaccadeDuration = this.sumOfSaccadeDurations / this.saccadeCount;

    }

    public void calculateSaccadeLenMetaData() {
        this.sumOfSaccadeLen = 0.0;
        List<Double> lengths = new ArrayList<>();

        for (Saccade saccade : this.saccades) {
            lengths.add(saccade.getLength());
            this.sumOfSaccadeLen += saccade.getLength();

            if (saccade.getLength() < this.minSaccadeLen)
                this.minSaccadeLen = saccade.getLength();

            if (saccade.getLength() > this.maxSaccadeLen)
                this.maxSaccadeLen = saccade.getLength();
        }

        this.medianSaccadeLen = descriptiveStats.getMedianOfDoubles(lengths);
        this.stdOfSaccadeLen = descriptiveStats.getStDevOfDoubles(lengths);
        this.meanSaccadeLen = this.sumOfSaccadeLen / this.saccadeCount;

    }
    
    public void calculateScanpathMetaData() {
        this.scanpathDuration = this.sumOfFixationDuration + this.sumOfSaccadeDurations;
        this.fixationToSaccadeRatio = this.sumOfFixationDuration / this.sumOfSaccadeDurations;
    }
    
    public void calculateRelativeDegreeMetaData() {
        List<Double> relativeDegrees = new ArrayList<>();
        for (int i = 1; i < this.saccades.size(); ++i) {
            Double relativeDegree = this.saccades.get(i-1).calculateRelativeAngle(this.saccades.get(i).getPointB());
            this.sumOfRelativeDegrees += relativeDegree;
            relativeDegrees.add(relativeDegree);
            
            if (relativeDegree < this.minRelativeDegree)
                this.minRelativeDegree = relativeDegree;
            
            if (relativeDegree > this.maxRelativeDegree)
                this.maxRelativeDegree = relativeDegree;
        }
        
        this.stdOfRelativeDegrees = descriptiveStats.getStDevOfDoubles(relativeDegrees);
        this.medianRelativeDegree = descriptiveStats.getMedianOfDoubles(relativeDegrees);
        this.meanRelativeDegree = this.sumOfRelativeDegrees / relativeDegrees.size();
    }
    
    public void calculateAbsoluteDegreeMetaData() {
        List<Double> absoluteDegrees = new ArrayList<>();
        for (int i = 0; i < this.saccades.size(); ++i) {
            Double absoluteDegree = this.saccades.get(i).calculateAbsoluteAngle();
            this.sumOfAbsoluteDegrees += absoluteDegree;
            absoluteDegrees.add(absoluteDegree);

            if (absoluteDegree < this.minAbsoluteDegree)
                this.minAbsoluteDegree = absoluteDegree;

            if (absoluteDegree > this.maxAbsoluteDegree)
                this.maxAbsoluteDegree = absoluteDegree;
        }

        this.stdOfAbsoluteDegrees = descriptiveStats.getStDevOfDoubles(absoluteDegrees);
        this.medianAbsoluteDegree = descriptiveStats.getMedianOfDoubles(absoluteDegrees);
        this.meanAbsoluteDegree = this.sumOfAbsoluteDegrees / absoluteDegrees.size();
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
