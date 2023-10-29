package interpolation;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class Interpolation {
    public Interpolation() {}

    /**
     * Source: http://paulbourke.net/miscellaneous/interpolation/
     * Code by
     * @param a
     * @param b
     * @param mu
     * @return
     */
    private double cosineInterpolate(double a, double b, double mu)
    {
        double mu2 = (1 - Math.cos(mu * Math.PI))/2;
        return (a * (1- mu2) + b *mu2);
    }
    @SuppressWarnings("Duplicates")
    public double[] interpolate(double a, double b, int delta) {
        //Determine the max size of the columns.
        double ratio = 1;


        double[] interpols = new double[delta];

        for (int i = 0; i < delta; ++i) {
            ratio = (i + 1) / (double) (delta + 1);

            //This equation calculates the average amount we need to add to each short in the array.
            // double interpolAmount = ((bAverage - aAverage) * ratio);
            //double interpolAmount = cosineInterpolate(aAverage, bAverage, ratio);

            //add a[j] to the interpolated amount to reach the new average.
            //short offset = a[j];
            //  interpolShorts[i][j] = (short) ((offset + interpolAmount));
            double offset = b - a;
            // interpolAmount = ((int)a[j] + (int) b[j]) * ratio + 0.5;
            interpols[i] = (a + (offset * ratio));


            //interpolShorts[i][j] = (short) (a[j] + ((b[j] - a[j])/2));
            //test interpolation equation

        }



        return interpols;
    }



    @SuppressWarnings("Duplicates")
    public double[][] interpolate(double[] a, double[] b, int delta) {
        //Determine the max size of the columns.
        int cols = a.length > b.length ? a.length : b.length;
        double ratio = 1;


        double[][] interpolShorts = new double[delta][cols];

        for (int i = 0; i < delta; ++i) {
            ratio = (i + 1) / (double) (delta + 1);

            //This equation calculates the average amount we need to add to each short in the array.
           // double interpolAmount = ((bAverage - aAverage) * ratio);
            //double interpolAmount = cosineInterpolate(aAverage, bAverage, ratio);
            for (int j = 0; j < a.length; ++j) {

                //add a[j] to the interpolated amount to reach the new average.
                //short offset = a[j];
              //  interpolShorts[i][j] = (short) ((offset + interpolAmount));
                double offset = b[j] - a[j];
                // interpolAmount = ((int)a[j] + (int) b[j]) * ratio + 0.5;
                interpolShorts[i][j] = (a[j] + (offset * ratio));

            }
                //interpolShorts[i][j] = (short) (a[j] + ((b[j] - a[j])/2));
            //test interpolation equation

        }



        return interpolShorts;
    }

    /**
     * Linear interpolates an array of shorts
     * @param a     The starting array
     * @param b     The Ending array
     * @param delta The number of short arrays to interpolate
     * @return Returns a 2D array of shorts with the delta interpolated short arrays.
     */
    public double[][] linearInterpolation(double[] a, double[] b, int delta) {
        //Determine the max size of the columns.
        int cols = a.length > b.length ? a.length : b.length;
        float ratio = 1;
        double aAverage = calcAverage(a);
        double bAverage = calcAverage(b);

        boolean isASmaller = aAverage < bAverage ? true: false;
        double[][] interpolShorts = new double[delta][cols];

        for (int i = 0; i < delta; ++i) {
            ratio = (i + 1) / (float) (delta + 1);

            //This equation calculates the average amount we need to add to each short in the array.
            double interpolAmount = ((bAverage - aAverage) * ratio);

            for (int j = 0; j < a.length; ++j)
                //add a[j] to the interpolated amount to reach the new average.
                interpolShorts[i][j] = a[j] + interpolAmount;
                //interpolShorts[i][j] = (short) (a[j] + ((b[j] - a[j])/2));
            //test interpolation equation

        }

//        if (isASmaller)
//            System.out.println("aAverage: " + aAverage);
//        else
//            System.out.println("bAverage: " + bAverage);
//        for (int i = 0; i < delta; ++i)
//        {
//            ratio = (i + 1) / (float) (delta + 1);
//
//            if (isASmaller)
//                System.out.printf("%d: aAvg: %f ratio: %f\n",i, (aAverage + bAverage) * ratio, ratio);
//            else
//                System.out.printf("%d: bAvg: %f ratio: %f\n",i, (aAverage + bAverage) * ratio, ratio);
//
//
//        }
//
//        if (isASmaller)
//            System.out.println("bAverage: " + bAverage);
//        else
//            System.out.println("aAverage: " + aAverage);

        return interpolShorts;
    }

    private double calcAverage(double[] a)
    {
        int sum = 0;
        int len = a.length;
        for (int i = 0; i < len; ++i)
            sum += (int) a[i];

        return (sum / (double) len);

    }

    public void testInterpolation(double[] a, double[] b, int delta)
    {
        double[][] interpolatedShorts = linearInterpolation(a,b,delta);
        int sum = 0;
        double[] averages = new double[interpolatedShorts.length];
        double aAvg = calcAverage(a);
        double bAvg = calcAverage(b);

        for (int i = 0; i < interpolatedShorts.length; ++i)
        {
         averages[i] = calcAverage(interpolatedShorts[i]);
        }
        System.out.println("A Array Avg: " + aAvg);
        for (int i  = 0; i < averages.length; ++i)
            System.out.printf("averags[%d]: %f\n", i, averages[i]);

        System.out.println("B Array Avg: " + bAvg);
    }

}
