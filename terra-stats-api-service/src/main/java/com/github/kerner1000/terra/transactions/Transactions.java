package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.commons.BuySellMap;
import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.commons.SwapPairs;
import com.github.kerner1000.terra.commons.SwapPrices;
import com.github.kerner1000.terra.json.data.Additional;
import com.github.kerner1000.terra.json.data.Swap;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
public class Transactions {

    public static final Predicate<Swap> BUY_WITH_UST = s -> s != null && "uusd".equals(s.getOfferAsset().getInfo().getNativeToken().getDenom());

    public static final Predicate<Swap> BUY_WITH_LUNA = s -> s != null && "uluna".equals(s.getOfferAsset().getInfo().getNativeToken().getDenom());

    static final Predicate<Additional> TERRASWAP_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.TerraSwap.LUNA_UST));

    static final Predicate<Additional> MARKET_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.Market.LUNA_UST));

    static final Predicate<Additional> ASTROPORT_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.Astroport.LUNA_UST));

    public SwapPrices getWeightedMean(BuySellMaps swapResult) {
        return new SwapPrices(weightedMean(swapResult.getBuyMap()), weightedMean(swapResult.getSellMap()));
    }

    static double weightedMean(BuySellMap map) {


        double num = 0;
        double denom = 0;
        for (Map.Entry<BuySellMap.Key, Number> entry : map.getMap().entrySet()) {
            num += entry.getKey().price().doubleValue() * entry.getValue().doubleValue();
            denom += entry.getValue().doubleValue();
        }

        return num / denom;

    }



}
