package com.mz.dmq.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserProfile {
    public static final UserProfile DEFAULT = UserProfile.builder().build();

    @Builder.Default String username = "UNKNOWN";

    public Object get(String key) {
        switch (key.toLowerCase()) {
            case "username":
                return username;
            default:
                throw new IllegalArgumentException("Key " + key + " not available in UserProfile");
        }
    }
}
