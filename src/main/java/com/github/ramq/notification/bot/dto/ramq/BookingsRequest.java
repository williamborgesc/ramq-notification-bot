package com.github.ramq.notification.bot.dto.ramq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BookingsRequest {

    @JsonProperty("StaffList")
    private List<String> staffList;

    @JsonProperty("Start")
    private String start;

    @JsonProperty("End")
    private String end;

    @JsonProperty("TimeZone")
    private String timeZone;
}
