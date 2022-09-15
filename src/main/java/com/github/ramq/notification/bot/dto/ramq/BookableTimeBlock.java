package com.github.ramq.notification.bot.dto.ramq;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BookableTimeBlock {

    @JsonProperty("Start")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime start;

    @JsonProperty("End")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime end;
}
