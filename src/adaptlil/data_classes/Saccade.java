package adaptlil.data_classes;

import java.awt.geom.Point2D;

public class Saccade {

    private Point2D.Double pointA;
    private Point2D.Double pointB;

    private double duration;
    private double length;


    /**
     * Default constructor for a saccade
     * where you have a point a, and a point b with a duration
     *
     * @param pointA
     * @param pointB
     * @param duration
     */
    public Saccade(Point2D.Double pointA, Point2D.Double pointB, double duration) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.duration = duration;
    }

    public Saccade(Point2D.Double pointA, Point2D.Double pointB, double duration, double length) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.duration = duration;
        this.length = length;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setLength(double length) {
        this.length = length;
    }


    public double getDuration() {
        return duration;
    }

    public double getLength() {
        return length;
    }

    public Point2D.Double getPointA() {
        return pointA;
    }

    public Point2D.Double getPointB() {
        return pointB;
    }

    /**
     * Given a saccade c, the relative angle from this saccade to the saccade c is calculated
     * @param c
     * @return
     */
    public double calculateRelativeAngle(Saccade c) {
        return this.calculateRelativeAngle(new Point2D.Double(c.pointA.getX(), c.pointA.getY()));
    }

    /**
     * Given a point c, it calculates the relative angle between the first point, second point of the saccade, to the new point c.
     * Point c is likley a point of fixation
     * @param c Point to calculate relative angle
     * @return
     */
    public double calculateRelativeAngle(Point2D.Double c) {

        double firstDegree = 0;
        double secondDegree = 0;
        double relativeDegree = 0;

        //degree between A and B
        double firstDifferenceInY = this.pointB.getY()-this.pointA.getY();
        double firstDifferenceInX = this.pointB.getX()-this.pointA.getX();

        //degree between B and C
        double secondDifferenceInY = c.getY()-this.pointB.getY();
        double secondDifferenceInX = c.getX()-this.pointB.getX();

        if((firstDifferenceInX==0.0 && secondDifferenceInX==0.0) || (firstDifferenceInY==0.0 && secondDifferenceInY==0.0)){
            //when A, B and C are all in a straight line, either horizontally or vertically
            relativeDegree = 180.0;

        }else if(firstDifferenceInX==0.0 && secondDifferenceInY<0.0){
            //when A&B are in a straight vertial line, C is to the lower (left or right) of B
            double secondSlope = Math.abs(secondDifferenceInX)/Math.abs(secondDifferenceInY);
            //returns the arctangent of a number as a value between -PI/2 and PI/2 radians
            double secondArctangent = Math.atan(secondSlope);
            secondDegree = Math.abs(Math.toDegrees(secondArctangent));

            //finally, the relative degree between A, B and C
            relativeDegree =  180.0 - secondDegree;


        }else if(firstDifferenceInX==0.0 && secondDifferenceInY>0.0){
            //when A&B are in a straight vertical line, C is to the upper (left or right) of B
            double secondSlope = Math.abs(secondDifferenceInX)/Math.abs(secondDifferenceInY);
            double secondArctangent = Math.atan(secondSlope);
            relativeDegree = Math.toDegrees(secondArctangent);


        }else if(secondDifferenceInX==0.0 && firstDifferenceInY<0.0){
            //when B&C are in a stright vertical line, A is to the upper (left or right) of B
            double firstSlope = Math.abs(firstDifferenceInX)/Math.abs(firstDifferenceInY);
            //returns the arctangent of a number as a value between -PI/2 and PI/2 radians
            double firstArctangent = Math.atan(firstSlope);
            firstDegree = Math.abs(Math.toDegrees(firstArctangent));
            //finally, the relative degree between A, B and C
            relativeDegree = 180.0 - firstDegree;


        }else if(secondDifferenceInX==0.0 && firstDifferenceInY>0.0){
            //when B&C are in a straight vertical line, A is to the lower (left or right) of B
            double firstSlope = Math.abs(firstDifferenceInX)/Math.abs(firstDifferenceInY);
            double firstArctangent = Math.atan(firstSlope);
            relativeDegree = Math.toDegrees(firstArctangent);


        }else if(firstDifferenceInY==0.0 && secondDifferenceInX<0.0){
            //when A&B are in a straight horizontal line, C is to the lower left of B (note if C is to the lower right of B, it is included in the last if-else statement below)
            double secondSlope = Math.abs(secondDifferenceInY)/Math.abs(secondDifferenceInX);
            double secondArctangent = Math.atan(secondSlope);
            relativeDegree = Math.toDegrees(secondArctangent);


        }else if(secondDifferenceInY==0.0 && firstDifferenceInX<0.0){
            //when B&C are in a straight horizontal line, A is to the upper right of B (note if A is to the upper left of B, it is included in the last if-else statement below)
            double firstSlop = Math.abs(firstDifferenceInY)/Math.abs(firstDifferenceInX);
            double firstArctangent = Math.atan(firstSlop);
            relativeDegree = Math.toDegrees(firstArctangent);


        }else{
            //all other regular cases where A, B and C are spread from one another; and
            //when A&B are in a straight horizontal line, C is to the lower right of B; and
            //when B&C are in a straight horizontal line, A is to the upper left of B.
            double firstSlope = Math.abs(firstDifferenceInY)/Math.abs(firstDifferenceInX);
            //returns the arctangent of a number as a value between -PI/2 and PI/2 radians
            double firstArctangent = Math.atan(firstSlope);
            firstDegree = Math.abs(Math.toDegrees(firstArctangent));

            double secondSlope = Math.abs(secondDifferenceInY)/Math.abs(secondDifferenceInX);
            //returns the arctangent of a number as a value between -PI/2 and PI/2 radians
            double secondArctangent = Math.atan(secondSlope);
            secondDegree = Math.abs(Math.toDegrees(secondArctangent));

            //finally, the relative degree between A, B and C
            relativeDegree = 180.0 - firstDegree - secondDegree;

        }

        return relativeDegree;
    }


    /**
     * Calculates the absolute angle (assuming x-axis=0)
     * @return
     */
    public double calculateAbsoluteAngle() {
        double absoluteDegree = 0;

        double differenceInY = this.pointB.getY() - this.pointB.getX();
        double differenceInX = this.pointB.getX() - this.pointA.getY();

        if(differenceInX==0.0){
            //when A&B are in a straight vertical line
            absoluteDegree = 90.00;
        }else if(differenceInY==0.0){
            //when A&B are in a straight horizontal line
            absoluteDegree = 0.0;
        }else {
            //all other cases where A&B draw a sloppy line
            double absoluteSlope = Math.abs(differenceInY) / Math.abs(differenceInX);
            //returns the arctangent of a number as a value between -PI/2 and PI/2 radians
            double absoluteArctangent = Math.atan(absoluteSlope);

            absoluteDegree = Math.abs(Math.toDegrees(absoluteArctangent));
        }

        return absoluteDegree;
    }
}
