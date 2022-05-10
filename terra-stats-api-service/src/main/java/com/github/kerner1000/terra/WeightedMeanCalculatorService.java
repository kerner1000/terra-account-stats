package com.github.kerner1000.terra;

import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.commons.BuySellMapsForCoin;
import com.github.kerner1000.terra.commons.Coin;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.AbstractTransactionVisitor;

import java.util.List;

public abstract class WeightedMeanCalculatorService {

    private final Coin coin;

    protected WeightedMeanCalculatorService(Coin coin) {
        this.coin = coin;
    }

    public BuySellMapsForCoin visit(List<Transaction> transactions) throws InterruptedException {
        
        BuySellMaps result = new BuySellMaps();

        for (Transaction transaction : transactions) {
            if(Thread.currentThread().isInterrupted()){
                break;
            }
            for (AbstractTransactionVisitor visitor : getVisitors()) {
                result.add(visitor.visit(transaction));
            }
        }
        return new BuySellMapsForCoin(coin, result);
    }

    protected abstract Iterable<? extends AbstractTransactionVisitor> getVisitors();
}
