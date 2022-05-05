package com.github.kerner1000.terra.json;

import com.github.kerner1000.terra.commons.ExtractedSwap;
import com.github.kerner1000.terra.json.data.ExecuteMessage;

public interface SwapExtractor {

    ExtractedSwap extract(String txHash, ExecuteMessage executeMessage);
}
