package com.mz.dmq.gateway.auth0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.dmq.model.auth0.Auth0token;
import com.mz.dmq.model.auth0.Auth0user;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Component
public class Auth0userGateway {
    private final Auth0tokenGateway tokenGateway;
    private final String createUserUri;

    @Autowired
    public Auth0userGateway(
            Auth0tokenGateway tokenGateway,
            @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}") String issuerUri) {
        this.tokenGateway = tokenGateway;
        this.createUserUri = UriComponentsBuilder.fromHttpUrl(issuerUri).pathSegment("api", "v2", "users").toUriString();
    }

    public Auth0user create(Auth0user user) {
        Auth0token token = tokenGateway.getToken();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("content-type", List.of("application/json"));
        headers.put("Authorization", List.of(token.toAuthorization()));
        HttpEntity<Auth0user> entity = new HttpEntity<>(user, headers);
        return new RestTemplate().postForObject(createUserUri, entity, Auth0user.class);
    }

    public static void main(String[] args) throws JsonProcessingException {
        String issuerUri = "https://mzaragoza.us.auth0.com/";
        Auth0tokenGateway tokenGateway = new Auth0tokenGateway(
                "slsHDC12Kyy9tayuDjVue7JIt0GG3y6U",
                "DmGykhz6D6HQULM9_I_Imbnl-MO2nNp0nEsirx-e498YnJMfMIY-W1HMPU4RNDFE",
                issuerUri);

        Auth0userGateway userGateway = new Auth0userGateway(tokenGateway, issuerUri);

        String nadiaJson = "{" +
                "  \"email\": \"nadia16.galli@gmail.com\"," +
                "  \"user_metadata\": {}," +
                "  \"blocked\": false," +
                "  \"app_metadata\": {}," +
                "  \"given_name\": \"Nadia\"," +
                "  \"family_name\": \"Galli\"," +
                "  \"name\": \"Nadia Galli\"," +
                "  \"nickname\": \"Nani\"," +
                "  \"connection\": \"Username-Password-Authentication\"," +
                "  \"password\": \"nadia1234*\"," +
                "  \"verify_email\": true" +
                "}";
        Auth0user auth0user = new ObjectMapper().readValue(nadiaJson, Auth0user.class);

        Auth0user newUser = userGateway.create(auth0user);

        System.out.println(newUser);
    }
}
