package com.WbReader.Controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {
    @JsonProperty("access_token")
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }
}
