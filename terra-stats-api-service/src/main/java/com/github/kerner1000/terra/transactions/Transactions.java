package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.*;
import com.github.kerner1000.terra.json.data.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;

@Slf4j
public class Transactions {

    static <T> Collector<T, ?, Double> averagingWeighted(ToDoubleFunction<T> valueFunction, ToDoubleFunction<T> weightFunction) {
        class Box {
            double num = 0;
            double denom = 0;
        }
        return Collector.of(
                Box::new,
                (b, e) -> {
                    b.num += valueFunction.applyAsDouble(e) * weightFunction.applyAsDouble(e);
                    b.denom += weightFunction.applyAsDouble(e);
                },
                (b1, b2) -> {
                    b1.num += b2.num;
                    b1.denom += b2.denom;
                    return b1;
                },
                b -> b.num / b.denom
        );
    }

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

    public static SwapPrices getWeightedMean(BuySellMaps swapResult) {
        return new SwapPrices(swapResult.getBuyMap().entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue)), swapResult.getSellMap().entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue)));
    }

}
