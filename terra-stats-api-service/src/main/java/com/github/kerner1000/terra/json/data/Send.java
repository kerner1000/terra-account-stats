package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Base64;

@Data
public class Send {

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    Number amount;

    String contract;

     Swap msg;

    @JsonProperty("msg")
    public void setMsg(JsonNode msg) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getDecoder().decode(msg.textValue());
        String decodedString = new String(decodedBytes);
        ExecuteMessage swap = objectMapper.readValue(decodedString, ExecuteMessage.class);
        this.msg = swap.getSwap();
    }
}
