package com.adrian.ng;

public class VolatilityFactory {

    public VolatilityAbstract getType(String type){

        if(type == null)
            return null;

        if(type.equals("EW"))
            return new VolatilityEW();

        if(type.equals("EWMA"))
            return new VolatilityEWMA();

        if(type.equals("GARCH"))
            return new VolatilityGARCH();

        return null;

    }

}
