package com.github.kerner1000.terra.transactions.swap;

import com.github.kerner1000.terra.*;
import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.json.data.ExecuteMessage;
import com.github.kerner1000.terra.json.data.Swap;
import com.github.kerner1000.terra.transactions.Transactions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TerraSwapAstroportLoopSwapExtractor implements SwapExtractor {

    public ExtractedSwap extract(String txHash, ExecuteMessage executeMessage) {
        Swap swap = executeMessage.getSwap();
        if(swap != null) {
            BuySellMaps buySellMaps = new BuySellMaps();
            double price = swap.getBeliefPrice().doubleValue();
            double simpleAmount = Constants.simpleAmount(swap.getOfferAsset().getAmount().doubleValue());
            SwapType swapType;
            if (Transactions.BUY_WITH_UST.test(swap)) {
                buySellMaps.addBuy(txHash, price, simpleAmount);
                swapType = new SwapType(Coin.UST, Coin.LUNA);
            } else if (Transactions.BUY_WITH_LUNA.test(swap)) {
                price = 1 / price;
                swapType = new SwapType(Coin.LUNA, Coin.UST);
                buySellMaps.addSell(txHash, price, simpleAmount);
            } else
                throw new IllegalStateException();
            return new ExtractedSwap(swapType, buySellMaps);
        } else {
            log.debug("No swap found for Tx {}", txHash);
        }
        return null;
    }
}
