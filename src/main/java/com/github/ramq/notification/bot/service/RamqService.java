package com.github.ramq.notification.bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ramq.notification.bot.dto.ramq.Bookings;
import com.github.ramq.notification.bot.dto.ramq.BookingsRequest;
import com.github.ramq.notification.bot.dto.ramq.RamqServiceResource;
import com.github.ramq.notification.bot.dto.ramq.SchedulerResource;
import com.github.ramq.notification.bot.dto.ramq.BookableTimeBlock;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RamqService {

    @Value("${application.ramq.json-regex}")
    private String jsonRegex;

    @Value("${application.ramq.service-regex}")
    private String serviceRegex;

    @Value("${application.ramq.bookings.uri}")
    private String bookingsUri;

    @Value("${application.ramq.bookings.service-uri}")
    private String serviceUri;
    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public Set<BookableTimeBlock> retrieveBookableTimeBlocks() {
        log.info("Begin retrieveBookableTimeBlocks");

        List<String> staffList = retrieveStaffList();
        BookingsRequest bookingRequest = createBookingRequest(staffList);
        Bookings bookings = restTemplate.postForObject(serviceUri, bookingRequest, Bookings.class);

        Set<BookableTimeBlock> bookableTimeBlocks = new HashSet<>();
        bookings.getStaffBookabilities().forEach(staffBookabilities -> {
            List<Integer> days = staffBookabilities.getBookableDays().stream()
                    .filter(bookableDay -> bookableDay.getStatus() == 1)
                    .map(bookableDay -> bookableDay.getDate().getDayOfYear())
                    .collect(Collectors.toList());

            staffBookabilities.getBookableTimeBlocks().forEach(bookableTimeBlock -> {
                if (days.contains(bookableTimeBlock.getStart().getDayOfYear())) {
                    bookableTimeBlocks.add(bookableTimeBlock);
                }
            });
        });

        log.info("End retrieveBookableTimeBlocks");
        return bookableTimeBlocks;
    }

    @SneakyThrows
    private List<String> retrieveStaffList() {
        log.debug("Begin retrieveStaffList");

        String payload = extractJsonFromHtml();
        log.debug("Payload retrieved: {}", payload);

        SchedulerResource resource = objectMapper.readValue(payload, SchedulerResource.class);

        return resource.getServices().stream()
                .filter(service -> service.getName().contains(serviceRegex))
                .findFirst()
                .map(RamqServiceResource::getStaffList)
                .orElseThrow(() -> new RuntimeException(String.format("Service with name containing '%s' not found with jsonPayload: %s", serviceRegex, payload)));
    }

    private BookingsRequest createBookingRequest(List<String> staffList) {
        log.debug("Begin createBookingRequest");
        BookingsRequest bookingsRequest = new BookingsRequest();
        bookingsRequest.setStaffList(staffList);

        //TODO set values dynamically
        bookingsRequest.setStart("2022-08-31T00:00:00");
        bookingsRequest.setEnd("2022-10-02T00:00:00");
        bookingsRequest.setTimeZone("America/Toronto");

        return bookingsRequest;
    }

    private String extractJsonFromHtml() throws IOException {
        log.debug("Begin extractJsonFromHtml");
        URL url = new URL(bookingsUri);

        log.debug("Get content from url={}", bookingsUri);
        String pagePayload = IOUtils.toString(url, StandardCharsets.ISO_8859_1);

        return extractScriptPayload(pagePayload);
    }

    private String extractScriptPayload(final String string) {
        log.debug("Begin extractScriptPayload with string={}", string);

        Pattern pattern = Pattern.compile(jsonRegex, Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Error parsing HTML from RAMQ page: " + bookingsUri);
    }
}
