package com.adrian.ng;

import org.apache.commons.math3.distribution.BinomialDistribution;

public class BackTestStandard extends  BackTest {

     private static int epsilon;

    public int[] doCoverage() {
        //https://www.value-at-risk.net/backtesting-coverage-tests/
        int alpha = countMoments + 1;
        //We reject the VaR measure if the number of violations is not in this interval
        BinomialDistribution distribution = new BinomialDistribution(alpha, 1 - Integer.parseInt(hashParam.get("Confidence")));
        //maximise a such that Pr(X < a) ≤ ε/2
        double pr = 0.0;
        int a = 0;
        while (pr <= epsilon / 2) {
            pr = distribution.cumulativeProbability(a);
            a++;
        }
        //minimize b such that Pr(b < X) ≤ ε/2
        pr = 1.0;
        int b = 0;
        while (pr >= epsilon / 2) {
            pr = 1 - distribution.cumulativeProbability(b);
            b++;
        }
        //maximise Pr(X ∉ [a + n, b]) ≤ ε
        double pr1 = 0.0;
        int n1 = 0;
        while (pr1 <= epsilon / 2) {
            pr1 = distribution.cumulativeProbability(a + n1) + (1 - distribution.cumulativeProbability(b));
            n1++;
        }
        //maximise Pr(X ∉ [a + n, b]) ≤ ε
        double pr2 = 0.0;
        int n2 = 0;
        while (pr2 <= epsilon / 2) {
            pr2 = distribution.cumulativeProbability(a) + (1 - distribution.cumulativeProbability(b - n2));
            n2++;
        }
        int[] nonRejectionInterval = new int[2];
        if (pr1 > pr2) {
            nonRejectionInterval[0] = a + n1;
            nonRejectionInterval[1] = b;
        } else {
            nonRejectionInterval[0] = a;
            nonRejectionInterval[1] = b - n2;
        }
        return nonRejectionInterval;
    }

}


