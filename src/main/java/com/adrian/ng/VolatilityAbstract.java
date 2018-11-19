package com.adrian.ng;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.math.BigDecimal.ROUND_HALF_UP;

public abstract class VolatilityAbstract {

    public BigDecimal[][] getCorrelationMatrix(BigDecimal[][] matrix) {

        int numCol = matrix.length;
        BigDecimal[][] correlationMatrix = new BigDecimal[numCol][numCol];

        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numCol; j++) {
                BigDecimal covXY = getVariance(matrix[i], matrix[j]);
                BigDecimal sigmaX = getVolatility(matrix[i], matrix[i]);
                BigDecimal sigmaY = getVolatility(matrix[j], matrix[j]);
                correlationMatrix[i][j] = covXY.divide(sigmaX.multiply(sigmaY), BigDecimal.ROUND_HALF_UP);
            }
            System.out.println("\t\t" + Arrays.toString(correlationMatrix[i]));
        }
        return correlationMatrix;
    }


    abstract public BigDecimal getVariance(BigDecimal[] xVector, BigDecimal[] yVector);

    public BigDecimal getVolatility(BigDecimal[] xVector, BigDecimal[] yVector) {
        //https://stackoverflow.com/a/19743026/10526321
        BigDecimal variance = getVariance(xVector, yVector);

        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(variance.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = variance.divide(x0, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal(2), ROUND_HALF_UP);
        }
        return x1;
    }

}
