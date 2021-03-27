package com.mz.dmq.model.auth0;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Auth0tokenRequest {
    // .body("{\"client_id\":\"slsHDC12Kyy9tayuDjVue7JIt0GG3y6U\",\"client_secret\":\"DmGykhz6D6HQULM9_I_Imbnl-MO2nNp0nEsirx-e498YnJMfMIY-W1HMPU4RNDFE\",\"audience\":\"https://mzaragoza.us.auth0.com/api/v2/\",\"grant_type\":\"client_credentials\"}")

    String clientId;
    String clientSecret;
    String audience;
    String grantType;
}
