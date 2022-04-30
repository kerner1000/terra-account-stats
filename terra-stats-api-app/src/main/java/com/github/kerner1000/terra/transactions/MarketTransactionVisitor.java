package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.ExtractedSwap;
import com.github.kerner1000.terra.SwapPairs;
import com.github.kerner1000.terra.json.data.MsgValue;
import com.github.kerner1000.terra.transactions.swap.MarketSwapExtractor;
import com.github.kerner1000.terra.transactions.swap.SwapTransactionVisitor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarketTransactionVisitor extends SwapTransactionVisitor {

    private final static MarketSwapExtractor swapExtractor = new MarketSwapExtractor();

    protected ExtractedSwap getExtractedSwap(MsgValue msgValue) {
        if (SwapPairs.Market.LUNA_UST.equals(msgValue.getContract())) {
            if (msgValue.getExecuteMessage() != null) {
                var result = swapExtractor.extract(msgValue.getExecuteMessage());
//                log.debug("Found Market swap: {}", result);
                return result;
            }
        }
        return null;
    }
}