package com.adrian.ng;


import yahoofinance.Stock;

import java.math.BigDecimal;


import org.apache.commons.math3.distribution.NormalDistribution;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class Analytical extends RiskMeasure {

    private String volatilityMeasure;

    public Analytical(String volatilityMeasure){
        this.volatilityMeasure = volatilityMeasure;
    }


    private BigDecimal getSquareNumber(BigDecimal squareNumber) {

        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(squareNumber.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = squareNumber.divide(x0, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal(2), ROUND_HALF_UP);
        }
        return x1;

    }

    @Override
    public BigDecimal getVar() {

        NormalDistribution distribution = new NormalDistribution(0, 1);

        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));

        double riskPercentile = -distribution.inverseCumulativeProbability(1 - Confidence);

        BigDecimal[] currentPrices = new BigDecimal[strSymbols.length];
        BigDecimal[] stockDelta = new BigDecimal[strSymbols.length];
        int size = getSize();


        BigDecimal[][] matrixPcntChanges = new BigDecimal[strSymbols.length][size];
        try {
            for (int i = 0; i < strSymbols.length; i++) {
                String sym = strSymbols[i];
                Stock stock = stockHashMap.get(sym);
                currentPrices[i] = stock.getQuote().getPreviousClose();
                stockDelta[i] = new BigDecimal(hashStockDeltas.get(sym));
                // get percentage changes of stock
                BigDecimal[] percentageChanges = PercentageChange.getArray(stock.getHistory());
                matrixPcntChanges[i] = percentageChanges;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        VolatilityFactory volatilityFactory = new VolatilityFactory();
        VolatilityAbstract volatilty = volatilityFactory.getType(volatilityMeasure);
        BigDecimal[][] correlationMatrix = volatilty.getCorrelationMatrix(matrixPcntChanges);

        BigDecimal[] assetVolatilities = new BigDecimal[strSymbols.length];
        for (int j = 0; j < strSymbols.length; j++)
            assetVolatilities[j] = volatilty.getVolatility(matrixPcntChanges[j], matrixPcntChanges[j]);

        //Compute linear combination
        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0; i < strSymbols.length; i++)
            for (int j = 0; j < strSymbols.length; j++)
                sum = sum
                        .add(stockDelta[i]
                                .multiply(stockDelta[j])
                                .multiply(currentPrices[i])
                                .multiply(currentPrices[j])
                                .multiply(correlationMatrix[i][j])
                                .multiply(assetVolatilities[i])
                                .multiply(assetVolatilities[j])
                        );
        //Computer VaR
        BigDecimal VaR = new BigDecimal(Math.sqrt(TimeHorizon))
                .multiply(new BigDecimal(riskPercentile))
                .multiply(getSquareNumber(sum));

        return VaR;
    }


}
