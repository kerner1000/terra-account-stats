package com.github.kerner1000.terra;

import org.openapitools.model.BuySellSwaps;
import org.openapitools.model.SwapEntry;
import org.openapitools.model.Swaps;

import java.math.BigDecimal;
import java.util.Map;

public class Transformer {

    public static BuySellSwaps transform(BuySellMaps maps){
        BuySellSwaps buySellSwaps = new BuySellSwaps();
        buySellSwaps.setBuy(new Swaps());
        buySellSwaps.setSell(new Swaps());
        for(Map.Entry<BuySellMaps.Key, Number> element : maps.getBuyMap().entrySet()){
            SwapEntry swapEntry = new SwapEntry();
            swapEntry.setId(element.getKey().id());
            swapEntry.setPrice(BigDecimal.valueOf(element.getKey().price().doubleValue()));
            swapEntry.setAmount(BigDecimal.valueOf(element.getValue().doubleValue()));
            buySellSwaps.getBuy().addSwapsItem(swapEntry);
        }
        for(Map.Entry<BuySellMaps.Key, Number> element : maps.getSellMap().entrySet()){
            SwapEntry swapEntry = new SwapEntry();
            swapEntry.setId(element.getKey().id());
            swapEntry.setPrice(BigDecimal.valueOf(element.getKey().price().doubleValue()));
            swapEntry.setAmount(BigDecimal.valueOf(element.getValue().doubleValue()));
            buySellSwaps.getSell().addSwapsItem(swapEntry);
        }
        return buySellSwaps;
    }
}
