package com.adrian.ng;

import yahoofinance.Stock;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class HistoricalSimulation extends RiskMeasure {


    @Override
    public BigDecimal getVar() {
        double Confidence = Double.parseDouble(hashParam.get("Confidence"));
        double TimeHorizon = Math.sqrt(Integer.parseInt(hashParam.get("TimeHorizonDays")));

        int size = getSize();

        BigDecimal currentPortfolio = BigDecimal.ZERO;
        ArrayList<BigDecimal> tomorrowPortfolio = new ArrayList<>(Collections.nCopies(size, BigDecimal.ZERO));

        /** Predict Tomorrow's Portfolio Prices **/
        try {
            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                BigDecimal currentPrice = stock.getQuote().getPreviousClose();

                //System.out.printf("Current %s price %f\n", sym, currentPrice);

                // add to current portfolio
                currentPortfolio = currentPortfolio.add(currentPrice.multiply(new BigDecimal(hashStockDeltas.get(sym))));

                // get percentage changes of stock
                ArrayList<BigDecimal> percentageChanges = PercentageChange.getArrayList(stock.getHistory());

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

        //System.out.printf("CurrentPortfolio:%f\n", currentPortfolio);

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
