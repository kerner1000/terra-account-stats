package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.AssertLimitOrder;
import com.github.kerner1000.terra.json.data.ExecuteMessage;

import java.util.Map;

import static com.github.kerner1000.terra.Coin.LUNA;
import static com.github.kerner1000.terra.Coin.UST;

class MarketSwapHandler {
    static ExtractedSwap handle(ExecuteMessage executeMessage, Map<Double, Double> buyMap, Map<Double, Double> sellMap) {
        AssertLimitOrder assertLimitOrder = executeMessage.getAssertLimitOrder();
        double receiveAmount = assertLimitOrder.getMinimumReceive().doubleValue();
        double nativeAmount = assertLimitOrder.getOfferCoin().getAmount().doubleValue();
        double simpleAmount = Constants.simpleAmount(nativeAmount);
        double price;
        SwapType swapType;
        if ("uluna".equals(assertLimitOrder.getAskDenom())) {
            price = nativeAmount / receiveAmount;
            buyMap.put(price, simpleAmount);
            swapType = new SwapType(UST, LUNA);
        } else if ("uusd".equals(assertLimitOrder.getAskDenom())) {
            price = receiveAmount / nativeAmount;
            sellMap.put(price, simpleAmount);
            swapType = new SwapType(LUNA, UST);
        } else
            throw new IllegalStateException();

        var result = new ExtractedSwap(swapType, price, simpleAmount);
        return result;
    }
}
