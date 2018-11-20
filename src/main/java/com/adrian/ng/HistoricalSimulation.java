package com.adrian.ng;

import yahoofinance.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class HistoricalSimulation extends RiskMeasure {
    @Override
    public double getVar() {
        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));
        int size = getSize();

        ArrayList<Double> tomorrowPortfolio = new ArrayList<>(Collections.nCopies(size, 0.0));

        /** Predict Tomorrow's Portfolio Prices **/
        try {
            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                double currentPrice = stock.getQuote().getPreviousClose().doubleValue();

                // get percentage changes of stock
                ArrayList<Double> percentageChanges = PercentageChange.getArrayList(stock.getHistory());

                // predict all possible tomorrow portfolio values per stock
                ArrayList<Double> tomorrowStockDelta = percentageChanges
                        .stream()
                        .map(i -> (i + 1) * currentPrice * hashStockDeltas.get(sym))
                        .collect(Collectors.toCollection(ArrayList::new));

                for (int i = 0; i < size; i++)
                    tomorrowPortfolio.set(i, tomorrowPortfolio.get(i) + tomorrowStockDelta.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Compute VaR
        Collections.sort(tomorrowPortfolio);
        double index = (1 - Confidence) * size;
        double VaR = (currentPortfolio - tomorrowPortfolio.get((int) index)) * TimeHorizon;
        return VaR;
    }
}
