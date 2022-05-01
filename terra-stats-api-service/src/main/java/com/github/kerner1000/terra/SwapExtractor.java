package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.ExecuteMessage;

public interface SwapExtractor {

    ExtractedSwap extract(ExecuteMessage executeMessage);
}
