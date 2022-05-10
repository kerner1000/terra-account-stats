package com.github.kerner1000.terra.transactions;

import com.github.kerner1000.terra.commons.Coin;
import com.github.kerner1000.terra.commons.ExtractedSwap;
import com.github.kerner1000.terra.commons.SwapPairs;
import com.github.kerner1000.terra.json.data.MsgValue;
import com.github.kerner1000.terra.transactions.swap.SwapTransactionVisitor;
import com.github.kerner1000.terra.transactions.swap.TerraSwapAstroportLoopSwapExtractorMars;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AstroportTransactionVisitorMars extends SwapTransactionVisitor {

    private final static TerraSwapAstroportLoopSwapExtractorMars swapExtractor = new TerraSwapAstroportLoopSwapExtractorMars();

    protected ExtractedSwap getExtractedSwap(String txHash, MsgValue msgValue) {
        if (Coin.MARS.getContract().equals(msgValue.getContract()) || SwapPairs.Astroport.MARS_UST.equals(msgValue.getContract()))  {
            if (msgValue.getExecuteMessage() != null) {
                var result = swapExtractor.extract(txHash, msgValue.getExecuteMessage());
//                log.debug("Found Astroport swap:\n{}", result);
                return result;
            } else {
                log.debug("Astroport swap without execute message, Tx: {}", txHash);
            }
        }
        return null;
    }
}