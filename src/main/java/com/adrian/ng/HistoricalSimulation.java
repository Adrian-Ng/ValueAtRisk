package com.adrian.ng;

import yahoofinance.Stock;

import java.io.IOException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HistoricalSimulation extends VaR implements RiskMeasure {


    @Override
    public BigDecimal getVar() {
        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));

        HashMap<String, ArrayList<BigDecimal>> stringArrayListHashMap = new HashMap<>();

        BigDecimal currentPortfolio = BigDecimal.ZERO;
        try {
            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                ArrayList<BigDecimal> percentageChanges = PercentageChange.percentageChange(stock.getHistory());
                stringArrayListHashMap.put(sym, percentageChanges);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<BigDecimal> bigDecimalArrayList = stringArrayListHashMap.get(strSymbols[0]);
        int size = bigDecimalArrayList.size();
        ArrayList<BigDecimal> tomorrowPortfolio = new ArrayList<>(Collections.nCopies(size, BigDecimal.ZERO));

        try {
            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                BigDecimal currentPrice = stock.getQuote().getPreviousClose();
                // add to current portfolio
                currentPortfolio = currentPortfolio.add(currentPrice.multiply(new BigDecimal(hashStockDeltas.get(sym))));

                // get percentage changes of stock
                ArrayList<BigDecimal> percentageChanges = PercentageChange.percentageChange(stock.getHistory());

                // predict all possible tomorrow portfolio values per stock
                ArrayList<BigDecimal> tomorrowStockDelta = percentageChanges
                        .stream()
                        .map(i -> i
                                .add(BigDecimal.ONE)
                                .multiply(currentPrice)
                                .multiply(new BigDecimal(hashStockDeltas.get(sym))))

                        .collect(Collectors.toCollection(ArrayList::new));

                for (int i = 0; i < size; i++)
                    tomorrowPortfolio.set(i, tomorrowPortfolio.get(i).add(tomorrowStockDelta.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.printf("CurrentPortfolio:%f\n", currentPortfolio);

        Collections.sort(tomorrowPortfolio);
       /* for(BigDecimal bd : tomorrowPortfolio)
            System.out.println(bd);*/

        double index = (1 - Confidence) * size;
        BigDecimal VaR = currentPortfolio
                .subtract(tomorrowPortfolio.get((int) index))
                .multiply(new BigDecimal(TimeHorizon));
        return VaR;

    }
}
