package com.mz.dmq.controller;

import com.mz.dmq.model.reading.CreateReadingRequest;
import com.mz.dmq.model.reading.Reading;
import com.mz.dmq.model.reading.ReadingValue;
import com.mz.dmq.model.reading.Title;
import com.mz.dmq.model.user.UserProfile;
import com.mz.dmq.util.TriFunction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/readings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadingsController extends BaseController {
    Function<String, Title> storeTitleIfAbsent;
    TriFunction<String, Title, CreateReadingRequest, Reading> createReading;
    BiFunction<String, Integer, List<Reading>> getReadingsByUser;

    @Autowired
    public ReadingsController(
            @Qualifier("storeTitleIfAbsent") Function<String, Title> storeTitleIfAbsent,
            @Qualifier("createReading") TriFunction<String, Title, CreateReadingRequest, Reading> createReading,
            @Qualifier("getReadingsByUser") BiFunction<String, Integer, List<Reading>> getReadingsByUser) {
        this.storeTitleIfAbsent = storeTitleIfAbsent;
        this.createReading = createReading;
        this.getReadingsByUser = getReadingsByUser;
    }

    @ModelAttribute(name = "profile")
    public UserProfile profile(@AuthenticationPrincipal Object principal) {
        // TODO : try to extract this to the BaseController
        return Optional.ofNullable(principal).map(this::principal2userProfile).orElse(UserProfile.DEFAULT);
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
            Model model,
            @ModelAttribute(name = "profile") UserProfile userProfile) {
        String readerId = userProfile.getUsername();
        Optional.ofNullable(errors).filter(Errors::hasErrors).ifPresentOrElse(
                e -> log.error("Found errors: {}", errors),
                () -> {
                    log.info("Storing {}", reading);
                    Title title = storeTitleIfAbsent.apply(reading.getTitle());
                    log.info("Title is {}", title);
                    // it would be weird id no email is found... throw an error in that case
                    createReading.apply(readerId, title, reading);
                    model.addAttribute("successMsg", "Lectura agendada");
                });
        return "create-reading";
    }

    @GetMapping
    public String myReadings(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            Model model,
            @ModelAttribute(name = "profile") UserProfile userProfile) {
        String readerId = userProfile.getUsername();
        List<Reading> readings = getReadingsByUser.apply(readerId, page);
        List<ReadingValue> readingValues = readings.stream().map(r ->
                ReadingValue.builder()
                        .briefing(r.getBriefing())
                        .chapter(r.getChapter())
                        .comment(r.getComment())
                        .id(r.getId())
                        .createDate(r.getCreateDate())
                        .link(r.getLink())
                        .time(r.getTime())
                        .titleName(r.getTitle().getName())
                        .build()
        ).collect(Collectors.toList());
        model.addAttribute("readings", readingValues);
        return "my-readings";
    }

//    private String getUserEmail(OidcUser oidcUser) {
//        return Optional.of(oidcUser).map(OidcUser::getClaims).map(c -> c.get("email")).orElseThrow().toString();
//    }
}
