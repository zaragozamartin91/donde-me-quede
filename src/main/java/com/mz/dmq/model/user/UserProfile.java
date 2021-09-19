package com.mz.dmq.model.user;

import java.util.HashMap;
import java.util.Map;

public class UserProfile {
    public static final UserProfile DEFAULT = new UserProfile(Map.of());

    private final Map<String, Object> claims;

    private UserProfile(Map<String, Object> claims) {
        this.claims = new HashMap<>(claims);
    }

    public static UserProfile fromClaims(Map<String, Object> claims) {
        return new UserProfile(claims);
    }

    public Map<String, Object> getClaims() {
        return new HashMap<>(claims);
    }

    public Object get(String key) {
        return claims.get(key);
    }

    public String getNickname() {
        return get("nickname").toString();
    }

    public String getName() {
        return get("name").toString();
    }

    public String getPicture() {
        return get("picture").toString();
    }

    public String getEmail() {
        return get("email").toString();
    }
}
/* {
  "sub" : "auth0|61463c20f49f0600716eb72a",
  "aud" : [ "slsHDC12Kyy9tayuDjVue7JIt0GG3y6U" ],
  "email_verified" : false,
  "updated_at" : {
    "nano" : 628000000,
    "epochSecond" : 1631992864
  },
  "nickname" : "foo",
  "name" : "foo@bar.com",
  "iss" : "https://mzaragoza.us.auth0.com/",
  "exp" : {
    "nano" : 0,
    "epochSecond" : 1632028877
  },
  "nonce" : "m_xZ9WzGpcpGRmIWaxNRqo6LbUmIxfk0Io50nscfVZU",
  "iat" : {
    "nano" : 0,
    "epochSecond" : 1631992877
  },
  "picture" : "https://s.gravatar.com/avatar/f3ada405ce890b6f8204094deb12d8a8?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Ffo.png",
  "email" : "foo@bar.com"
} */