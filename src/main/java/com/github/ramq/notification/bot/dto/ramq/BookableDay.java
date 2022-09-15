package com.github.ramq.notification.bot.dto.ramq;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BookableDay {

    @JsonProperty("Date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDate date;

    @JsonProperty("Status")
    private int status;
}
