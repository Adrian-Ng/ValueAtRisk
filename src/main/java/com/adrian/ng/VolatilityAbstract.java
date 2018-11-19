package com.adrian.ng;

import java.math.BigDecimal;
import java.util.Arrays;

public abstract class VolatilityAbstract {

    public BigDecimal[][] getCorrelationMatrix(BigDecimal[][] matrix){

        int numCol = matrix.length;
        BigDecimal[][] correlationMatrix = new BigDecimal[numCol][numCol];

        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numCol; j++) {
                BigDecimal covXY = getVariance(matrix[i], matrix[j]);
                BigDecimal sigmaX = getVolatility(matrix[i],matrix[i]);
                BigDecimal sigmaY = getVolatility(matrix[j], matrix[j]);
                correlationMatrix[i][j] = covXY.divide(sigmaX.multiply(sigmaY),BigDecimal.ROUND_HALF_UP);
            }
            System.out.println("\t\t" + Arrays.toString(matrix[i]));
        }
        return correlationMatrix;
    }



    abstract public BigDecimal getVariance(BigDecimal[] xVector, BigDecimal[] yVector);

    abstract public BigDecimal getVolatility(BigDecimal[] xVector, BigDecimal[] yVector);

}
