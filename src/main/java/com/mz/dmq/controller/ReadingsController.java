package com.mz.dmq.controller;

import com.mz.dmq.model.reading.CreateReadingRequest;
import com.mz.dmq.model.reading.Title;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Controller
@RequestMapping("/readings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadingsController {
    Function<String, Title> storeTitleIfAbsent;

    @Autowired
    public ReadingsController(
            @Qualifier("storeTitleIfAbsent") Function<String, Title> storeTitleIfAbsent) {
        this.storeTitleIfAbsent = storeTitleIfAbsent;
    }

    @ModelAttribute(name = "profile")
    public Map<String, Object> profile(@AuthenticationPrincipal OidcUser principal) {
        return Optional.ofNullable(principal).map(OidcUser::getClaims).orElseGet(Map::of);
    }


    @GetMapping("/new")
    public String createReadingForm(Model model) {
        model.addAttribute("reading", new CreateReadingRequest());
        return "create-reading";
    }

    /**
     * Crea una lectura para un usuario a partir de
     * titulo, capitulo, pagina(opcional), tiempo(opcional), sitio/enlace, briefing(opcional), opiniones(opcional)
     */
    @PostMapping
    public String createReading(
            @Valid @ModelAttribute(value = "reading") CreateReadingRequest reading,
            Errors errors,
            Model model) {
        Optional.ofNullable(errors).filter(Errors::hasErrors).ifPresentOrElse(
                e -> log.error("Found errors: {}", errors),
                () -> {
                    log.info("Storing {}", reading);
                    Title title = storeTitleIfAbsent.apply(reading.getTitle());
                    log.info("New title is {}", title);
                    model.addAttribute("successMsg", "Lectura agendada");
                });
        return "create-reading";
    }
}
