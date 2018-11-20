package com.adrian.ng;

import yahoofinance.Stock;

import java.io.IOException;

public abstract class RiskMeasure extends VaR {

    public int getSize() {
        Stock stck = stockHashMap.get(strSymbols[0]);
        int size = 0;
        try {
            size = stck.getHistory().size() - 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    abstract double getVar();

}
