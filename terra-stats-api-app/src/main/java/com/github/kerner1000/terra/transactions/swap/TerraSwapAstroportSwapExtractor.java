package com.github.kerner1000.terra.transactions.swap;

import com.github.kerner1000.terra.*;
import com.github.kerner1000.terra.json.data.ExecuteMessage;
import com.github.kerner1000.terra.json.data.Swap;
import com.github.kerner1000.terra.transactions.Transactions;

public class TerraSwapAstroportSwapExtractor implements SwapExtractor {

    public ExtractedSwap extract(ExecuteMessage executeMessage) {
        Swap swap = executeMessage.getSwap();
        if(swap != null) {
            WeightedMeanSwapMaps weightedMeanSwapMaps = new WeightedMeanSwapMaps();
            double price = swap.getBeliefPrice().doubleValue();
            double simpleAmount = Constants.simpleAmount(swap.getOfferAsset().getAmount().doubleValue());
            SwapType swapType;
            if (Transactions.BUY_WITH_UST.test(swap)) {
                weightedMeanSwapMaps.getBuyMap().put((int) Math.round(price), (int) Math.round(simpleAmount));
                swapType = new SwapType(Coin.UST, Coin.LUNA);
            } else if (Transactions.BUY_WITH_LUNA.test(swap)) {
                price = 1 / price;
                swapType = new SwapType(Coin.LUNA, Coin.UST);
                weightedMeanSwapMaps.getSellMap().put((int) Math.round(price), (int) Math.round(simpleAmount));
            } else
                throw new IllegalStateException();
            return new ExtractedSwap(swapType, weightedMeanSwapMaps);
        }
        return null;
    }
}
