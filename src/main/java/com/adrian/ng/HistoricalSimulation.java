package com.adrian.ng;

import yahoofinance.Stock;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class HistoricalSimulation extends VaR implements RiskMeasure {

    @Override
    public BigDecimal getVar() {
        try {

            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                System.out.printf("\t%s:\n", stock.getName());
                BigDecimal currentPrice = stock.getQuote().getPreviousClose();

                BigDecimal currentPortfolio = currentPrice.multiply(new BigDecimal(hashStockDeltas.get(sym)));

                ArrayList<BigDecimal> percentageChanges = PercentageChange.percentageChange(stock.getHistory());
                ArrayList<BigDecimal> tomorrowPortfolio = percentageChanges
                        .stream()
                        .map(i -> i
                                .multiply(currentPrice)
                                .multiply(new BigDecimal(hashStockDeltas.get(sym))))
                        .collect(Collectors.toCollection(ArrayList::new));
/*
                for (BigDecimal bigDecimal : tomorrowPortfolio)
                    System.out.println(bigDecimal);*/

                double Confidence = Double.parseDouble(hashParam.get("Confidence"));
                double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));
                int index = (int) (1 - Confidence)* tomorrowPortfolio.size();
                BigDecimal VaR = currentPortfolio
                        .subtract(tomorrowPortfolio.get(index))
                        .multiply(new BigDecimal(TimeHorizon));
                return VaR;
            }
        } catch (NullPointerException | IOException | ArithmeticException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


}
