package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.BuySellMaps;
import com.github.kerner1000.terra.json.data.Transaction;

public abstract class AbstractTransactionVisitor implements TransactionVisitor<BuySellMaps> {

    @Override
    public abstract BuySellMaps visit(Transaction transaction);
}
