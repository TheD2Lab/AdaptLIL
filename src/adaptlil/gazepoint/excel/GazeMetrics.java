package adaptlil.gazepoint.excel;

import adaptlil.annotations.IgnoreWekaAttribute;
import adaptlil.data_classes.Fixation;
import adaptlil.data_classes.Saccade;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class GazeMetrics {


    /**
     * TODO, Readme
     * We will start with three additional metrics. If the learning increases, we will continue adding more.
     */
    public Integer fixationCount = 0;
    public Integer saccadeCount = 0;


    @IgnoreWekaAttribute
    private List<Fixation> fixations;

    @IgnoreWekaAttribute
    private List<Saccade> saccades;

    public Double sumOfSaccadeLen = 0.0;
    public Double meanSaccadeLen=0.0;
    public Double medianSaccadeLen=0.0;
    @IgnoreWekaAttribute
    public Double stdOfSaccadeLen=0.0;
    @IgnoreWekaAttribute
    public Double minSaccadeLen = 5000.0;
    @IgnoreWekaAttribute
    public Double maxSaccadeLen = 0.0;

    public Double sumOfFixationDuration = 0.0;
    public Double meanFixationDuration=0.0;
    public Double medianFixationDuration=0.0;
    @IgnoreWekaAttribute
    public Double stdOfFixationDuration=0.0;
    @IgnoreWekaAttribute
    public Double minFixationDuration = 5000.0;
    @IgnoreWekaAttribute
    public Double maxFixationDuration = 0.0;

    public Double sumOfSaccadeDurations = 0.0;
    public Double meanSaccadeDuration=0.0;
    public Double medianSaccadeDuration=0.0;
    @IgnoreWekaAttribute
    public Double stdOfSaccadeDurations=0.0;
    public Double minSaccadeDuration = 5000.0;
    public Double maxSaccadeDuration = 0.0;

    public Double scanpathDuration=0.0;
    public Double fixationToSaccadeRatio=0.0;

    public Double sumOfAbsoluteDegrees = 0.0;
    public Double meanAbsoluteDegree = 0.0;
    public Double medianAbsoluteDegree = 0.0;
    @IgnoreWekaAttribute
    public Double stdOfAbsoluteDegrees = 0.0;
    @IgnoreWekaAttribute
    public Double minAbsoluteDegree = 5000.0;
    @IgnoreWekaAttribute
    public Double maxAbsoluteDegree = 0.0;

    public Double sumOfRelativeDegrees = 0.0;
    public Double meanRelativeDegree = 0.0;
    public Double medianRelativeDegree = 0.0;
    @IgnoreWekaAttribute
    public Double stdOfRelativeDegrees = 0.0;
    @IgnoreWekaAttribute
    public Double minRelativeDegree = 5000.0;
    @IgnoreWekaAttribute
    public Double maxRelativeDegree = 0.0;



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

    /**
     * Calculate the statistical data surrounding the fixations.
     */
    public void calculateFixationMetaData() {
        this.fixationCount = fixations.size();
        this.sumOfFixationDuration = 0.0;
        double[] durations = new double[this.fixationCount];

        for (int i = 0; i < this.fixationCount; i++) {
            Fixation fixation = this.fixations.get(i);
            durations[i] = fixation.getDuration();
            this.sumOfFixationDuration += fixation.getDuration();

            if (fixation.getDuration() < this.minFixationDuration)
                this.minFixationDuration = fixation.getDuration();

            if (fixation.getDuration() > this.maxFixationDuration)
                this.maxFixationDuration = fixation.getDuration();
        }
        if (this.fixationCount > 0) {
            DescriptiveStatistics durationDescriptiveStatistics = new DescriptiveStatistics(durations);
            this.medianFixationDuration = durationDescriptiveStatistics.getPercentile(50);
            this.stdOfFixationDuration = durationDescriptiveStatistics.getStandardDeviation();
            this.meanFixationDuration = durationDescriptiveStatistics.getMean();
        }

    }

    public void calculateSaccadeMetaData() {
        this.saccadeCount = saccades.size();

        this.calculateSaccadeDurationMetaData();
        this.calculateSaccadeLenMetaData();
    }

    public void calculateSaccadeDurationMetaData() {
        this.sumOfSaccadeDurations = 0.0;
        double[] durations = new double[this.saccades.size()];

        for (int i = 0; i < this.saccades.size(); i++) {
            Saccade saccade = this.saccades.get(i);
            durations[i] = saccade.getDuration();

            this.sumOfSaccadeDurations += saccade.getDuration();

            if (saccade.getDuration() < this.minSaccadeDuration)
                this.minSaccadeDuration = saccade.getDuration();

            if (saccade.getDuration() > this.maxSaccadeDuration)
                this.maxSaccadeDuration = saccade.getDuration();
        }
        if (this.saccadeCount > 0) {
            DescriptiveStatistics durationDescriptiveStatistics = new DescriptiveStatistics(durations);
            this.medianSaccadeDuration = durationDescriptiveStatistics.getPercentile(50);
            this.stdOfSaccadeDurations = durationDescriptiveStatistics.getStandardDeviation();
            this.meanSaccadeDuration = durationDescriptiveStatistics.getMean();
        }

    }

    public void calculateSaccadeLenMetaData() {
        this.sumOfSaccadeLen = 0.0;
        double[] lengths = new double[this.saccades.size()];

        for (int i = 0; i < this.saccades.size(); i++) {
            Saccade saccade = saccades.get(i);
            lengths[i] = saccade.getLength();
            this.sumOfSaccadeLen += saccade.getLength();

            if (saccade.getLength() < this.minSaccadeLen)
                this.minSaccadeLen = saccade.getLength();

            if (saccade.getLength() > this.maxSaccadeLen)
                this.maxSaccadeLen = saccade.getLength();
        }
        if (this.saccadeCount > 0) {
            DescriptiveStatistics saccadeLengthDescriptiveStatistics = new DescriptiveStatistics(lengths);

            this.medianSaccadeLen = saccadeLengthDescriptiveStatistics.getPercentile(50);
            this.stdOfSaccadeLen = saccadeLengthDescriptiveStatistics.getStandardDeviation();
            this.meanSaccadeLen = saccadeLengthDescriptiveStatistics.getMean();
        }

    }
    
    public void calculateScanpathMetaData() {
        this.scanpathDuration = this.sumOfFixationDuration + this.sumOfSaccadeDurations;
        if (this.sumOfFixationDuration > 0 && this.sumOfSaccadeDurations > 0)
            this.fixationToSaccadeRatio = this.sumOfFixationDuration / this.sumOfSaccadeDurations;
    }
    
    public void calculateRelativeDegreeMetaData() {
        double[] relativeDegrees = new double[this.saccades.size() - 1];

        for (int i = 1; i < this.saccades.size(); ++i) {
            Double relativeDegree = this.saccades.get(i-1).calculateRelativeAngle(this.saccades.get(i).getPointB());
            this.sumOfRelativeDegrees += relativeDegree;
            relativeDegrees[i-1] = relativeDegree;
            
            if (relativeDegree < this.minRelativeDegree)
                this.minRelativeDegree = relativeDegree;
            
            if (relativeDegree > this.maxRelativeDegree)
                this.maxRelativeDegree = relativeDegree;
        }

        if (relativeDegrees.length > 0) {
            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(relativeDegrees);
            this.stdOfRelativeDegrees = descriptiveStatistics.getStandardDeviation();
            this.medianRelativeDegree = descriptiveStatistics.getPercentile(50);
            this.meanRelativeDegree = descriptiveStatistics.getMean();
        }
    }
    
    public void calculateAbsoluteDegreeMetaData() {
        double[] absoluteDegrees = new double[this.saccades.size()];

        for (int i = 0; i < this.saccades.size(); ++i) {
            Double absoluteDegree = this.saccades.get(i).calculateAbsoluteAngle();
            this.sumOfAbsoluteDegrees += absoluteDegree;
            absoluteDegrees[i] = absoluteDegree;

            if (absoluteDegree < this.minAbsoluteDegree)
                this.minAbsoluteDegree = absoluteDegree;

            if (absoluteDegree > this.maxAbsoluteDegree)
                this.maxAbsoluteDegree = absoluteDegree;
        }

        if (absoluteDegrees.length > 0) {
            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(absoluteDegrees);
            this.stdOfAbsoluteDegrees = descriptiveStatistics.getStandardDeviation();
            this.medianAbsoluteDegree = descriptiveStatistics.getPercentile(50);
            this.meanAbsoluteDegree = descriptiveStatistics.getMean();
        }
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
