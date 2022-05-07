package com.github.kerner1000.terra;

import com.github.kerner1000.terra.commons.BuySellMap;
import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.commons.Coin;
import org.openapitools.model.BuySellSwaps;
import org.openapitools.model.SwapEntry;
import org.openapitools.model.Swaps;

import java.math.BigDecimal;
import java.util.Map;

public class Transformer {

    public static BuySellSwaps transform(Coin coin, BuySellMaps maps){
        BuySellSwaps buySellSwaps = new BuySellSwaps();
        buySellSwaps.setCoin(coin.toString());
        buySellSwaps.setBuy(new Swaps());
        buySellSwaps.setSell(new Swaps());
        for(Map.Entry<BuySellMap.Key, Number> element : maps.getBuyMap().getMap().entrySet()){
            SwapEntry swapEntry = new SwapEntry();
            swapEntry.setId(element.getKey().id());
            swapEntry.setPrice(BigDecimal.valueOf(element.getKey().price().doubleValue()));
            swapEntry.setAmount(BigDecimal.valueOf(element.getValue().doubleValue()));
            buySellSwaps.getBuy().addSwapsItem(swapEntry);
        }
        for(Map.Entry<BuySellMap.Key, Number> element : maps.getSellMap().getMap().entrySet()){
            SwapEntry swapEntry = new SwapEntry();
            swapEntry.setId(element.getKey().id());
            swapEntry.setPrice(BigDecimal.valueOf(element.getKey().price().doubleValue()));
            swapEntry.setAmount(BigDecimal.valueOf(element.getValue().doubleValue()));
            buySellSwaps.getSell().addSwapsItem(swapEntry);
        }
        return buySellSwaps;
    }
}
