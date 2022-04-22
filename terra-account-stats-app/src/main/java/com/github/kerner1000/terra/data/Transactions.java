package com.github.kerner1000.terra.data;

import java.util.List;

public class Transactions {

    private final List<Transaction> transactions;

    public Transactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }


}
