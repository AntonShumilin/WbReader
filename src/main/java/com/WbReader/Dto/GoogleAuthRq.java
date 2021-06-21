package com.WbReader.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleAuthRq {

    @JsonProperty("client_id")
    private String client_id;
    @JsonProperty("redirect_uri")
    private String redirect_uri;
    @JsonProperty("response_type")
    private String response_type;
    @JsonProperty("scope")
    private String scope;

    public GoogleAuthRq(String client_id, String redirect_uri, String response_type, String scope) {
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.response_type = response_type;
        this.scope = scope;
    }
}
