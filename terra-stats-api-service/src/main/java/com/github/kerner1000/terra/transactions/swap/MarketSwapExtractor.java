package com.github.kerner1000.terra.transactions.swap;

import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.commons.Constants;
import com.github.kerner1000.terra.commons.ExtractedSwap;
import com.github.kerner1000.terra.commons.SwapType;
import com.github.kerner1000.terra.json.SwapExtractor;
import com.github.kerner1000.terra.json.data.AssertLimitOrder;
import com.github.kerner1000.terra.json.data.ExecuteMessage;

import static com.github.kerner1000.terra.commons.Coin.LUNA;
import static com.github.kerner1000.terra.commons.Coin.UST;

public class MarketSwapExtractor implements SwapExtractor {

    public ExtractedSwap extract(String txHash, ExecuteMessage executeMessage) {
        BuySellMaps buySellMaps = new BuySellMaps();
        AssertLimitOrder assertLimitOrder = executeMessage.getAssertLimitOrder();
        double receiveAmount = assertLimitOrder.getMinimumReceive().doubleValue();
        double nativeAmount = assertLimitOrder.getOfferCoin().getAmount().doubleValue();
        double simpleAmount = Constants.simpleAmount(nativeAmount);
        double price;
        SwapType swapType;
        if ("uluna".equals(assertLimitOrder.getAskDenom())) {
            price = nativeAmount / receiveAmount;
            buySellMaps.addBuy(txHash, price, simpleAmount);
            swapType = new SwapType(UST, LUNA);
        } else if ("uusd".equals(assertLimitOrder.getAskDenom())) {
            price = receiveAmount / nativeAmount;
            buySellMaps.addSell(txHash, price, simpleAmount);
            swapType = new SwapType(LUNA, UST);
        } else
            throw new IllegalStateException();

        var result = new ExtractedSwap(swapType, buySellMaps);
        return result;
    }
}
