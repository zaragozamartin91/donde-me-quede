package com.mz.dmq.model.auth0;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Auth0errMsg {
    //{"statusCode":400,"error":"Bad Request","message":"\"phone_verified\" is not allowed","errorCode":"invalid_body"}
    int statusCode;
    String error;
    String message;
    String errorCode;
}
