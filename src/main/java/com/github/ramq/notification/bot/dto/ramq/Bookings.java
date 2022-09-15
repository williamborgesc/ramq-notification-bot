package com.github.ramq.notification.bot.dto.ramq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Bookings {

    @JsonProperty("StaffBookabilities")
    private List<StaffBookabilities> staffBookabilities;

}
