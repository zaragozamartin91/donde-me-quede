package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReadingRequest {
    // titulo, capitulo, pagina(opcional), tiempo(opcional), sitio/enlace, briefing(opcional), opiniones(opcional)
    @NotEmpty(message = "Titulo no puede ser vacio") String title;
    @NotEmpty(message = "Capitulo no puede ser vacio") String chapter;
    @Min(value = 0L, message = "Pagina no puede ser menor a 0") int page;
    @DateTimeFormat(pattern = "HH:mm") LocalTime time;
    String link;
    String briefing;
    String comment;
    long pageid; // wikipedia page id
}
