package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.ExtractedSwap;
import com.github.kerner1000.terra.SwapPairs;
import com.github.kerner1000.terra.json.data.MsgValue;
import com.github.kerner1000.terra.transactions.swap.SwapTransactionVisitor;
import com.github.kerner1000.terra.transactions.swap.TerraSwapAstroportLoopSwapExtractor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoopSwapTransactionVisitor extends SwapTransactionVisitor {

    private final static TerraSwapAstroportLoopSwapExtractor swapExtractor = new TerraSwapAstroportLoopSwapExtractor();

    @Override
    protected ExtractedSwap getExtractedSwap(String txHash, MsgValue msgValue) {
        if(SwapPairs.Loop.LUNA_UST.equals(msgValue.getContract())){
            if(msgValue.getExecuteMessage() != null) {
                var result = swapExtractor.extract(txHash, msgValue.getExecuteMessage());
                log.debug("Found Loop swap: {}", result);
                return result;
            } else {
                log.debug("Loop swap without execute message, Tx: {}", txHash);
            }
        }
        return null;
    }
}
