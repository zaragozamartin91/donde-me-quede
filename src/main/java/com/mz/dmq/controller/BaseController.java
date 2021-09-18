package com.mz.dmq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.dmq.model.user.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;

@Slf4j
abstract public class BaseController {
    protected UserProfile principal2userProfile(Object principal) {
        User userDetails = (User) principal;
        return UserProfile.builder().username(userDetails.getUsername()).build();
    }

    protected String toJson(Object claims) {
        String errMsg = "Error parsing object as JSON";
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (JsonProcessingException jpe) {
            log.error(errMsg, jpe);
            return errMsg;
        }
    }
}
