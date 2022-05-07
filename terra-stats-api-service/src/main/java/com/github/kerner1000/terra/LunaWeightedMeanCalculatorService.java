package com.github.kerner1000.terra;

import com.github.kerner1000.terra.commons.Coin;
import com.github.kerner1000.terra.transactions.*;

import java.util.Arrays;

public class LunaWeightedMeanCalculatorService extends WeightedMeanCalculatorService {

    public LunaWeightedMeanCalculatorService() {
        super(Coin.LUNA);
    }

    protected Iterable<? extends AbstractTransactionVisitor> getVisitors() {
        return Arrays.asList(new TerraswapTransactionVisitor(), new AstroportTransactionVisitor(), new MarketTransactionVisitor(), new LoopSwapTransactionVisitor());
    }
}
