package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Additional {

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private List<ExecuteMessage> result = new ArrayList<>();

    private List<ExecuteMessage> extract(List<String> executeMessageStrings) {

        for (String messageString : executeMessageStrings) {
            try {
                ExecuteMessage messageObject = objectMapper.readValue(messageString, new TypeReference<ExecuteMessage>() {
                });
                result.add(messageObject);
            } catch (Exception e) {
//                log.debug("Failed to extract execute messages from string {}", messageString);
            }
        }
        return result;
    }

    List<String> contract;

    @ToString.Exclude
    List<String> executeMessageStrings;

    @JsonProperty("execute_message")
    public void setExecuteMessageStrings(List<String> executeMessageStrings) {
        this.executeMessageStrings = executeMessageStrings;
        this.executeMessages = extract(executeMessageStrings);
    }

    List<ExecuteMessage> executeMessages;
}
