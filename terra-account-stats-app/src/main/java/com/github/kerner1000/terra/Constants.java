package com.github.kerner1000.terra;

public interface Constants {

    short TERRA_NUM_DIGITS = 6;

    static double simpleAmount(double nativeAmount){
        double result = nativeAmount;
        for(int i = 0; i < TERRA_NUM_DIGITS; i++){
            result = result / 10;
        }
        return result;
    }
}
