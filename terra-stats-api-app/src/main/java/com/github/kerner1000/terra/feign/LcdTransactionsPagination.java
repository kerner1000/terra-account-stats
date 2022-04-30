package com.github.kerner1000.terra.feign;

import com.github.kerner1000.terra.json.data.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class LcdTransactionsPagination {

    int next;

    int Limit;

    List<Transaction> txs;
}
