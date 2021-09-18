package com.mz.dmq.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController extends BaseController {

//    @GetMapping("/")
//    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
//        Optional.ofNullable(principal)
//                .map(OidcUser::getClaims)
//                .ifPresent(c -> model.addAttribute("profile", c));
//        return "index";
//    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal Object principal) {
        Optional.ofNullable(principal).map(this::principal2userProfile).ifPresent(c -> model.addAttribute("profile", c));
        return "index";
    }
}