package com.mz.dmq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.dmq.model.user.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class BaseController {
    @ModelAttribute(name = "profile")
    public UserProfile profile(@AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> claims = Optional.ofNullable(principal).map(OidcUser::getClaims).orElseGet(Map::of);
        return UserProfile.fromClaims(claims);
    }

    protected String toJson(Object object) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing object as JSON", jpe);
        }
        return "Error parsing object as JSON";
    }
}
