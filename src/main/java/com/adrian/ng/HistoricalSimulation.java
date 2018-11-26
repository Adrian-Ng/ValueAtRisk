package com.adrian.ng;

import yahoofinance.Stock;

import java.util.Arrays;

public class HistoricalSimulation extends RiskMeasure {
    @Override
    public double getVar() {
        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));

        //ArrayList<Double> tomorrowPortfolio = new ArrayList<>(Collections.nCopies(size, 0.0));
        double[] tomorrowPortfolio = new double[size];

        /** Predict Tomorrow's Portfolio Prices **/
        try {
            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                int stockDeltas = hashStockDeltas.get(sym);
                double currentPrice = stock.getQuote().getPreviousClose().doubleValue();
                double[] percentageChanges = PercentageChange.getArray(stock.getHistory());
                for(int i = 0; i < percentageChanges.length; i++)
                    tomorrowPortfolio[i] += (percentageChanges[i] + 1) * currentPrice * stockDeltas;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Compute VaR
        Arrays.sort(tomorrowPortfolio);
        double index = (1 - Confidence) * size;
        double VaR = (currentPortfolio - tomorrowPortfolio[(int) index]) * TimeHorizon;
        return VaR;
    }
}
