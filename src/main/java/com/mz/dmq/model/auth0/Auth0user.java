package com.mz.dmq.model.auth0;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Auth0user {

    String email;
    String phoneNumber;
    Map<String, Object> userMetadata;
    // using non primitive booleans on optional boolean fields
    Boolean blocked;
    Boolean emailVerified;
    Boolean phoneVerified;
    Map<String, Object> appMetadata;
    String givenName;
    String familyName;
    String name;
    String nickname;
    String picture;
    String userId;
    String connection;
    @ToString.Exclude String password;
    Boolean verifyEmail;
    String username;
}
