package com.mz.dmq.controller;

import com.mz.dmq.model.user.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    @GetMapping("/profile")
//    public String profile(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
//        model.addAttribute("profile", oidcUser.getClaims());
//        model.addAttribute("profileJson", claimsToJson(oidcUser.getClaims()));
//        return "profile";
//    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal Object principal) {
        UserProfile userProfile = principal2userProfile(principal);
        model.addAttribute("profile", userProfile);
        model.addAttribute("profileJson", toJson(userProfile));
        return "profile";
    }
}
