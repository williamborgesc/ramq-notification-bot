package com.github.ramq.notification.bot.dto.ramq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StaffBookabilities {

    @JsonProperty("BookableTimeBlocks")
    private List<BookableTimeBlock> bookableTimeBlocks;

    @JsonProperty("BookableDays")
    private List<BookableDay> bookableDays;

}
