package com.WbReader.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "security.oauth2.client")
@Configuration("oAuthProperties")
public class OAuthProperties {

    private String clientId;
    private String clientSecret;
    private String userAuthorizationUri;
    private String redirectUri;
    private String userInfoUri;
    private String accessTokenUri;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserAuthorizationUri() {
        return userAuthorizationUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setUserAuthorizationUri(String userAuthorizationUri) {
        this.userAuthorizationUri = userAuthorizationUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }
}

