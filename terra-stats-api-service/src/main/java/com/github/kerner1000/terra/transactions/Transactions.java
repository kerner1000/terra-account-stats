package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.BuySellMaps;
import com.github.kerner1000.terra.SwapPairs;
import com.github.kerner1000.terra.SwapPrices;
import com.github.kerner1000.terra.json.data.Additional;
import com.github.kerner1000.terra.json.data.Swap;
import com.github.kerner1000.terra.json.data.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
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

    private final List<AbstractTransactionVisitor> visitors = Arrays.asList(new TerraswapTransactionVisitor(), new AstroportTransactionVisitor(), new MarketTransactionVisitor());

    public BuySellMaps getWeightedMeanSwapMaps(Collection<? extends Transaction> transactionsList) throws InterruptedException {

        BuySellMaps result = new BuySellMaps();

        for (Transaction transaction : transactionsList) {
            if(Thread.currentThread().isInterrupted()){
                break;
            }
            for (AbstractTransactionVisitor visitor : visitors) {
                result.add(visitor.visit(transaction));
            }
        }
        return result;
    }

    public SwapPrices getWeightedMean(Collection<? extends Transaction> transactionsList) throws InterruptedException {
        BuySellMaps result = getWeightedMeanSwapMaps(transactionsList);
        return getWeightedMean(result);
    }

    public SwapPrices getWeightedMean(BuySellMaps swapResult) {
        return new SwapPrices(weightedMean(swapResult.getBuyMap()), weightedMean(swapResult.getSellMap()));
    }

    static double weightedMean(Map<BuySellMaps.Key, Number> map) {


        double num = 0;
        double denom = 0;
        for (Map.Entry<BuySellMaps.Key, Number> entry : map.entrySet()) {
            num += entry.getKey().price().doubleValue() * entry.getValue().doubleValue();
            denom += entry.getValue().doubleValue();
        }

        return num / denom;

    }

}
