package com.mz.dmq.gateway;

import com.mz.dmq.model.auth0.Auth0token;
import com.mz.dmq.model.auth0.Auth0tokenRequest;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class Auth0tokenGateway {
    private static final String GRANT_TYPE = "client_credentials";

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String audience;

    volatile private Auth0token currToken;

    @Autowired
    public Auth0tokenGateway(
            @Value("${spring.security.oauth2.client.registration.auth0.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.auth0.client-secret}") String clientSecret,
            @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}") String issuerUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = UriComponentsBuilder.fromHttpUrl(issuerUri).pathSegment("oauth", "token").toUriString();
        this.audience = UriComponentsBuilder.fromHttpUrl(issuerUri).pathSegment("api", "v2").toUriString().concat("/");
    }

    /*
    HttpResponse<String> response = Unirest.post("https://mzaragoza.us.auth0.com/oauth/token")
  .header("content-type", "application/json")
  .body("{\"client_id\":\"slsHDC12Kyy9tayuDjVue7JIt0GG3y6U\",\"client_secret\":\"DmGykhz6D6HQULM9_I_Imbnl-MO2nNp0nEsirx-e498YnJMfMIY-W1HMPU4RNDFE\",\"audience\":\"https://mzaragoza.us.auth0.com/api/v2/\",\"grant_type\":\"client_credentials\"}")
  .asString();
 */

    public synchronized Auth0token getToken() {
        currToken = Mono.justOrEmpty(Optional.ofNullable(currToken))
                .switchIfEmpty(Mono.fromSupplier(this::fetchToken))
                .filter(this::valid)
                .switchIfEmpty(Mono.fromSupplier(this::fetchToken))
                .block();

        return currToken;
    }

    private boolean valid(Auth0token token) {
        return !expired(token);
    }

    @SneakyThrows
    private boolean expired(Auth0token token) {
        JWT parsed = JWTParser.parse(token.getAccessToken());
        Date expdate = parsed.getJWTClaimsSet().getExpirationTime();
        // if token is close to expire, cosider it expired
        return Instant.now().compareTo(expdate.toInstant().minus(10, ChronoUnit.MINUTES)) > 0;
    }

    public Auth0token fetchToken() {
        log.info("Fetching token from {}", tokenUrl);
        Auth0tokenRequest auth0tokenRequest = new Auth0tokenRequest(clientId, clientSecret, audience, GRANT_TYPE);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("content-type", List.of("application/json"));
        HttpEntity<Auth0tokenRequest> entity = new HttpEntity<>(auth0tokenRequest, headers);
        return new RestTemplate().postForObject(tokenUrl, entity, Auth0token.class);
    }

    public static void main(String[] args) throws ParseException {
        Auth0tokenGateway gateway = new Auth0tokenGateway(
                "slsHDC12Kyy9tayuDjVue7JIt0GG3y6U",
                "DmGykhz6D6HQULM9_I_Imbnl-MO2nNp0nEsirx-e498YnJMfMIY-W1HMPU4RNDFE",
                "https://mzaragoza.us.auth0.com/");
        Auth0token token = gateway.getToken();
        System.out.println(token);

//        Base64.Decoder decoder = Base64.getDecoder();
//
//        String[] chunks = token.getAccessToken().split("\\.");
//        String header = new String(decoder.decode(chunks[0]));
//        String payload = new String(decoder.decode(chunks[1]));
//        System.out.println(header);
//        System.out.println(payload);

//        JWT jwt = JWTParser.parse("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImJGM3M4UUZidEhKU0dwZnBkZFlBaSJ9.eyJpc3MiOiJodHRwczovL216YXJhZ296YS51cy5hdXRoMC5jb20vIiwic3ViIjoic2xzSERDMTJLeXk5dGF5dURqVnVlN0pJdDBHRzN5NlVAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vbXphcmFnb3phLnVzLmF1dGgwLmNvbS9hcGkvdjIvIiwiaWF0IjoxNjE2NzE2NDYyLCJleHAiOjE2MTY4MDI4NjIsImF6cCI6InNsc0hEQzEyS3l5OXRheXVEalZ1ZTdKSXQwR0czeTZVIiwic2NvcGUiOiJyZWFkOmNsaWVudF9ncmFudHMgY3JlYXRlOmNsaWVudF9ncmFudHMgcmVhZDp1c2VycyBjcmVhdGU6dXNlcnMgcmVhZDp1c2Vyc19hcHBfbWV0YWRhdGEgY3JlYXRlOnVzZXJzX2FwcF9tZXRhZGF0YSByZWFkOnVzZXJfY3VzdG9tX2Jsb2NrcyBjcmVhdGU6dXNlcl9jdXN0b21fYmxvY2tzIGNyZWF0ZTp1c2VyX3RpY2tldHMgcmVhZDpjbGllbnRzIHJlYWQ6Y2xpZW50X2tleXMgcmVhZDpjb25uZWN0aW9ucyByZWFkOnJlc291cmNlX3NlcnZlcnMgcmVhZDpkZXZpY2VfY3JlZGVudGlhbHMgcmVhZDpydWxlcyByZWFkOnJ1bGVzX2NvbmZpZ3MgcmVhZDpob29rcyByZWFkOmFjdGlvbnMgcmVhZDplbWFpbF9wcm92aWRlciByZWFkOnN0YXRzIHJlYWQ6dGVuYW50X3NldHRpbmdzIHJlYWQ6bG9ncyByZWFkOmxvZ3NfdXNlcnMgcmVhZDpzaGllbGRzIHJlYWQ6YW5vbWFseV9ibG9ja3MgcmVhZDp0cmlnZ2VycyByZWFkOmdyYW50cyByZWFkOmd1YXJkaWFuX2ZhY3RvcnMgcmVhZDpndWFyZGlhbl9lbnJvbGxtZW50cyByZWFkOnVzZXJfaWRwX3Rva2VucyByZWFkOmN1c3RvbV9kb21haW5zIHJlYWQ6ZW1haWxfdGVtcGxhdGVzIHJlYWQ6bWZhX3BvbGljaWVzIHJlYWQ6cm9sZXMgcmVhZDpwcm9tcHRzIHJlYWQ6YnJhbmRpbmcgcmVhZDpsb2dfc3RyZWFtcyByZWFkOnNpZ25pbmdfa2V5cyByZWFkOmxpbWl0cyByZWFkOnJvbGVfbWVtYmVycyByZWFkOmVudGl0bGVtZW50cyIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyJ9.m2aw8U7iGKxJ9yHniDQuTdHxksFrZvvnEBXPrVcE12cSQ0P0Gg7L5c5HtBKTATDQH2MSOZaQFC-3K80cW0qMX04Otdk4pvaKkdY41AKEiESSzva9HLN8aUeVGABfIJIjQ8j4qPbXS9qTnMHhC59oCX4zQTZ7_TFe9dSBxDocvE42noUYQEevTSYE8Dbdy1hkrMYjfeHRqpC_mfhZuMbMStsNG7nL7uTd8gHIjTmd3o9NdxyAmvvJ4OHHFoEFQJRyC94r8G22353lKn-TwUlQBVFgaMr7Xbsw679VTtGmuosnDinQ4M4y1tGJYt9n1JNBQEG86wvsnIikIrNrB0uz_A");
//        Date expiration = jwt.getJWTClaimsSet().getExpirationTime();
//
//        LocalDateTime expTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        System.out.println(expTime);

//        String result = Mono.justOrEmpty(Optional.<String>empty())
//                .switchIfEmpty(Mono.fromSupplier(() -> {
//                    System.out.println("running");
//                    return "mateo";
//                })).block();
//
//        System.out.println(result);

    }
}
