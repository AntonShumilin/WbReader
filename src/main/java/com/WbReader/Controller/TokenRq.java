package com.WbReader.Controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenRq implements Serializable {

    @JsonProperty("grant_type")
    private String grant_type;
    @JsonProperty("client_id")
    private String client_id;
    @JsonProperty("client_secret")
    private String client_secret;
    @JsonProperty("code")
    private String code;
    @JsonProperty("redirect_uri")
    private String redirect_uri;

    public TokenRq(String grant_type, String client_id, String client_secret, String code, String redirect_uri) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.code = code;
        this.redirect_uri = redirect_uri;
    }
}
