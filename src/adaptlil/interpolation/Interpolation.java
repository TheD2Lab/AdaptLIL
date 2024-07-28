package adaptlil.interpolation;

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


    public double[] interpolate(double a, double b, int delta) {
        //Determine the max size of the columns.
        double ratio = 1;


        double[] interpols = new double[delta];

        for (int i = 0; i < delta; ++i) {
            ratio = (i + 1) / (double) (delta + 1);

            double offset = b - a;
            interpols[i] = (a + (offset * ratio));
        }



        return interpols;
    }



    public double[][] interpolate(double[] a, double[] b, int delta) {
        //Determine the max size of the columns.
        int cols = a.length > b.length ? a.length : b.length;
        double ratio = 1;


        double[][] interpolShorts = new double[delta][cols];

        for (int i = 0; i < delta; ++i) {
            ratio = (i + 1) / (double) (delta + 1);

            //This equation calculates the average amount we need to add to each short in the array.
            for (int j = 0; j < a.length; ++j) {

                //add a[j] to the interpolated amount to reach the new average.
                double offset = b[j] - a[j];
                interpolShorts[i][j] = (a[j] + (offset * ratio));

            }
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
        int cols = Math.max(a.length, b.length);
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
                interpolShorts[i][j] = a[j] + interpolAmount;
        }

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

}
