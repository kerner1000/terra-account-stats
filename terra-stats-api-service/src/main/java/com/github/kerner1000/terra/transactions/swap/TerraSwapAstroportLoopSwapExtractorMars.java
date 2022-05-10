package com.github.kerner1000.terra.transactions.swap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.commons.*;
import com.github.kerner1000.terra.json.SwapExtractor;
import com.github.kerner1000.terra.json.data.ExecuteMessage;
import com.github.kerner1000.terra.json.data.Send;
import com.github.kerner1000.terra.json.data.Swap;
import com.github.kerner1000.terra.transactions.Transactions;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class TerraSwapAstroportLoopSwapExtractorMars implements SwapExtractor {

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ExtractedSwap extract(String txHash, ExecuteMessage executeMessage) {
        Swap swap = executeMessage.getSwap();
        Send send = executeMessage.getSend();
        if (swap != null) {
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
        } else if (send != null) {
            if (SwapPairs.Astroport.MARS_UST.equals(send.getContract())) {
                BuySellMaps buySellMaps = new BuySellMaps();
                Swap swap2 = send.getMsg();
                double price = swap2.getBeliefPrice().doubleValue();
                double simpleAmount = Constants.simpleAmount(send.getAmount().doubleValue());
                SwapType swapType;
                price = 1 / price;
                swapType = new SwapType(Coin.MARS, Coin.UST);
                buySellMaps.addSell(txHash, price, simpleAmount);
                return new ExtractedSwap(swapType, buySellMaps);
            }
        }
        return null;
    }
}
