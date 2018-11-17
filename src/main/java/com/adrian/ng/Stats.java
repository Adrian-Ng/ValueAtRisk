package com.adrian.ng;

import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stats {

    public ArrayList<BigDecimal> percentageChange(List<HistoricalQuote> historicalQuotes) {

        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();

        ArrayList<BigDecimal> percentageChange = new ArrayList<>();
        BigDecimal a = iterator.next().getClose();

        do{
            BigDecimal b = iterator.next().getClose();
            //its monal
            BigDecimal PriceDiff = a
                                    .subtract(b)
                                    .divide(b);
            percentageChange.add(PriceDiff);
            a = b;
        }
        while (iterator.hasNext());


        for (HistoricalQuote historicalQuote : historicalQuotes){
            historicalQuote.getClose();

        }
        return percentageChange;
    }


}
