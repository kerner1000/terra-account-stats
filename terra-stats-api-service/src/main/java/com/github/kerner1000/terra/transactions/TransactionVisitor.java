package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.commons.Visitor;
import com.github.kerner1000.terra.json.data.Transaction;

public interface TransactionVisitor<O> extends Visitor<Transaction,O> {
}
