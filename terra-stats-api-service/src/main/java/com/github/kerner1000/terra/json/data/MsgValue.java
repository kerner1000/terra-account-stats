package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@Data
public class MsgValue {

    String contract;

    //    @ToString.Exclude
    JsonNode executeMessageString;

    @JsonIgnore
    ExecuteMessage executeMessage;

    @JsonProperty("execute_msg")
    public void setExecuteMessageString(JsonNode executeMessageString) {
        this.executeMessageString = executeMessageString;
        List<ExecuteMessage> messages = null;
        try {
            messages = Additional.extract(executeMessageString.toString());
        } catch (JsonProcessingException e1) {
            var text = executeMessageString.textValue();
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(text);
                String decodedString = new String(decodedBytes);
                log.debug("Decoded message: {}", decodedString);
            }catch (Exception e2){
//                log.debug("Failed to extract execute messages from string {}, ex: {}", executeMessageString, e2);
            }
//            log.debug("Failed to extract execute messages from string {}", executeMessageString);
        }
        if (messages != null && messages.size() > 0)
            setExecuteMessage(messages.get(0));
    }
}
