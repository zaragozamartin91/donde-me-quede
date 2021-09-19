package com.mz.dmq.controller;

import com.mz.dmq.model.user.UserProfile;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller for requests to the {@code /profile} resource. Populates the model with the claims from the
 * {@linkplain OidcUser} for use by the view.
 */
@Controller
public class ProfileController extends BaseController {
    @GetMapping("/profile")
    public String profile(Model model, @ModelAttribute(name = "profile") UserProfile userProfile) {
        model.addAttribute("profileJson", toJson(userProfile.getClaims()));
        return "profile";
    }
}
