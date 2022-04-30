package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.ToString;

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
        var messages = Additional.extract(executeMessageString.toString());
        if(messages != null && messages.size() > 0)
        setExecuteMessage(messages.get(0));
    }
}
