package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.ExecuteMessage;
import com.github.kerner1000.terra.json.data.Swap;

import java.util.Map;

import static com.github.kerner1000.terra.Coin.LUNA;
import static com.github.kerner1000.terra.Coin.UST;

class TerraSwapAstroportSwapHandler {

    static ExtractedSwap handle(ExecuteMessage executeMessage, Map<Double, Double> buyMap, Map<Double, Double> sellMap) {
        Swap swap = executeMessage.getSwap();
        double price = swap.getBeliefPrice().doubleValue();
        double simpleAmount = Constants.simpleAmount(swap.getOfferAsset().getAmount().doubleValue());
        SwapType swapType;
        if (Transactions.BUY_WITH_UST.test(swap)) {
            buyMap.put(price, simpleAmount);
            swapType = new SwapType(UST, LUNA);
        } else if (Transactions.BUY_WITH_LUNA.test(swap)) {
            price = 1 / price;
            swapType = new SwapType(LUNA, UST);
            sellMap.put(price, simpleAmount);
        } else
            throw new IllegalStateException();

        var result = new ExtractedSwap(swapType, price, simpleAmount);
        return result;
    }
}
