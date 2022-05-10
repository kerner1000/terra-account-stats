package com.github.kerner1000.terra;

import com.github.kerner1000.terra.commons.Coin;
import com.github.kerner1000.terra.transactions.*;

import java.util.Arrays;

public class MarsWeightedMeanCalculatorService extends WeightedMeanCalculatorService {

    public MarsWeightedMeanCalculatorService() {
        super(Coin.MARS);
    }

    protected Iterable<? extends AbstractTransactionVisitor> getVisitors() {
        return Arrays.asList(new AstroportTransactionVisitorMars(), new TerraswapTransactionVisitorMars());
    }
}
