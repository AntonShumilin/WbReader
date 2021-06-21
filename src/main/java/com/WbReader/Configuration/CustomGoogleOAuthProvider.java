package com.WbReader.Configuration;

import com.WbReader.CustomExeptions.CustomException;
import com.WbReader.CustomExeptions.UserNotFoundException;
import com.WbReader.Data.User;
import com.WbReader.Dto.Token;
import com.WbReader.Dto.TokenRq;
import com.WbReader.Dto.UserInfo;
import com.WbReader.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Service
public class CustomGoogleOAuthProvider {

    @Autowired
    UserService userService;

//    @Autowired
//    RestTemplate restTemplate;

    @Autowired
    OAuthProperties oAuthProperties;



    public String getOAuthRequest () {
//        GoogleAuthRq googleAuthRq = new GoogleAuthRq(
//                oAuthProperties.getClientId(),
//                oAuthProperties.getRedirectUri(),
//                "code",
//                "email");

        return oAuthProperties.getUserAuthorizationUri() +
                "?client_id=" +
                oAuthProperties.getClientId() +
                "&redirect_uri=" +
                oAuthProperties.getRedirectUri() +
                "&response_type=code&scope=email";

//                "redirect:https://accounts.google.com/o/oauth2/v2/auth?client_id=411244877568-ouh2m9ug0fsiprg9s64cgffi9vuo2e4o.apps.googleusercontent.com&redirect_uri=http://localhost:8080/oauth&response_type=code&scope=email";
    }

    public String getAccessToken(String authCode) throws CustomException {
        String result = null;
        TokenRq tokenRq = new TokenRq(
                "authorization_code",
                oAuthProperties.getClientId(),
                oAuthProperties.getClientSecret(),
                authCode,
                oAuthProperties.getRedirectUri());
        RequestEntity<TokenRq> req = RequestEntity
                .post(URI.create(oAuthProperties.getAccessTokenUri()))
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .body(tokenRq);
        try {
            ResponseEntity<Token> resp = new RestTemplate().exchange(req, Token.class);
            if (resp.getBody() != null && resp.getBody().getAccess_token() != null) {
                result = resp.getBody().getAccess_token();
            }
        } catch (Exception e) {
            throw new UserNotFoundException("Google oauth error");
        }
        return Optional.ofNullable(result).orElseThrow(new UserNotFoundException("Google oauth error"));
    }

    public String getUserUnfo (String accessToken) throws CustomException {
        String result = null;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken);
        RequestEntity<String> req = new RequestEntity<>(headers, HttpMethod.POST, URI.create(oAuthProperties.getUserInfoUri()));
        try {
            ResponseEntity<UserInfo> userInfo = new RestTemplate().exchange(req, UserInfo.class);
            if (userInfo.getBody() != null && userInfo.getBody().getEmail() != null) {
                result = userInfo.getBody().getEmail();
            }
        } catch (Exception e) {
            throw new UserNotFoundException("Error receiving user info");
        }
        return Optional.ofNullable(result).orElseThrow(new UserNotFoundException("Error receiving user info"));
    }

    public void authenticate (String email) {
        User user = userService.findByUserName(email).orElseGet(() ->
            userService.addNewUser(email, new StringBuilder(email).reverse().toString(), false)
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(user, new StringBuilder(email).reverse().toString());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


}
