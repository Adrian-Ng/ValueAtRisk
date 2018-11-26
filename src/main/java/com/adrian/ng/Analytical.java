package com.adrian.ng;

import yahoofinance.Stock;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Analytical extends RiskMeasure {

    private String volatilityMeasure;

    public Analytical(String volatilityMeasure) {
        this.volatilityMeasure = volatilityMeasure;
    }

    @Override
    public double getVar() {

        NormalDistribution distribution = new NormalDistribution(0, 1);
        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));
        double riskPercentile = -distribution.inverseCumulativeProbability(1 - Confidence);

        double[] currentPrices = new double[countAsset];
        double[] stockDelta = new double[countAsset];

        double[][] matrixPcntChanges = new double[countAsset][size];
        try {
            for (int i = 0; i < countAsset; i++) {
                String sym = strSymbols[i];
                Stock stock = stockHashMap.get(sym);
                currentPrices[i] = stock.getQuote().getPreviousClose().doubleValue();
                stockDelta[i] = new Double(hashStockDeltas.get(sym));
                // get percentage changes of stock
                double[] percentageChanges = PercentageChange.getArray(stock.getHistory());
                matrixPcntChanges[i] = percentageChanges;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[][] covarianceMatrix = new VolatilityFactory()
                .getType(volatilityMeasure)
                .getCovarianceMatrix(matrixPcntChanges);

        //Compute linear combination
        double sum = 0.0;
        for (int i = 0; i < countAsset; i++)
            for (int j = 0; j < countAsset; j++)
                sum += stockDelta[i]
                        * stockDelta[j]
                        * currentPrices[i]
                        * currentPrices[j]
                        * covarianceMatrix[i][j];
        //Computer VaR
        double VaR = Math.sqrt(TimeHorizon)
                * riskPercentile
                * Math.sqrt(sum);
        return VaR;
    }


}
