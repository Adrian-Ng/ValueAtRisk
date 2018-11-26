package com.adrian.ng;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

public class BackTestKupiec extends BackTest {

    private double loglikelihood(double alpha, int i, double q){
        double part1 = (alpha + 1 - i) / (q * (alpha + 1));
        part1 = Math.pow(part1, alpha + 1 - i);
        double part2 = i / ((1 - q) * (alpha + 1));
        part2 = Math.pow(part2, i);
        double result = 2*Math.log(part1*part2);
        return result;
    }

    private static double epsilon;

    public int[] doCoverage(){
        int[] nonRejectionInterval = new int[2];
        int alpha = countMoments + 1;
        double q = Integer.parseInt(hashParam.get("Confidence"));
        ChiSquaredDistribution distribution = new ChiSquaredDistribution(1, 0);
        double quantile = distribution.inverseCumulativeProbability(1-epsilon);
        int i = 0;
        //CALCULATE LOWER INTERVAL
        while(true) {
            if(loglikelihood(alpha, i, q)<=quantile) {
                break;
            }
            i++;
        }
        //which increment is closest to quantile?
        double dist1 = Math.abs(loglikelihood(alpha, i ,q)-quantile);
        double dist2 = Math.abs(loglikelihood(alpha, i-1,q)-quantile);
        if(dist1 > dist2)
            nonRejectionInterval[0] = i;
        else nonRejectionInterval[0] = i-1;
        //CALCULATE UPPER INTERVAL
        while(true) {
            if(loglikelihood(alpha, i, q) >= quantile){
                break;
            }
            i++;
        }
        //which increment is closest to quantile?
        dist1 = Math.abs(loglikelihood(alpha, i ,q)-quantile);
        dist2 = Math.abs(loglikelihood(alpha, i-1,q)-quantile);
        if(dist1 > dist2)
            nonRejectionInterval[1] = i;
        else nonRejectionInterval[1] = i-1;
        return nonRejectionInterval;
    }

}
