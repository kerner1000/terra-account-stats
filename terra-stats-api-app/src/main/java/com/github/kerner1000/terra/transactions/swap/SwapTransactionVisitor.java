package com.github.kerner1000.terra.transactions.swap;

import com.github.kerner1000.terra.ExtractedSwap;
import com.github.kerner1000.terra.BuySellMaps;
import com.github.kerner1000.terra.json.data.*;
import com.github.kerner1000.terra.transactions.AbstractTransactionVisitor;

public abstract class SwapTransactionVisitor extends AbstractTransactionVisitor {

    private final BuySellMaps result = new BuySellMaps();

    @Override
    public BuySellMaps visit(Transaction transaction) {
        if (transaction != null) {
            Tx tx = transaction.getTx();
            if (tx != null) {
                TxValue value = tx.getValue();
                if (value != null) {
                    for (Msg msg : value.getMsgList()) {
                        if (msg != null) {
                            MsgValue msgValue = msg.getValue();
                            ExtractedSwap extractedSwap = getExtractedSwap(msgValue);
                            if (extractedSwap != null)
                                result.add(extractedSwap.swapMaps());
                        }
                    }
                }
            }
        }
        return result;
    }

    protected abstract ExtractedSwap getExtractedSwap(MsgValue msgValue);
}