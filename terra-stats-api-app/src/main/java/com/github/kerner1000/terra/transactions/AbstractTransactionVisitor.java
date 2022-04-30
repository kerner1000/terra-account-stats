package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.WeightedMeanSwapMaps;
import com.github.kerner1000.terra.json.data.Transaction;

public abstract class AbstractTransactionVisitor implements TransactionVisitor<WeightedMeanSwapMaps> {

    @Override
    public abstract WeightedMeanSwapMaps visit(Transaction transaction);
}
