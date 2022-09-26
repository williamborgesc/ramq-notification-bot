package com.github.ramq.notification.bot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ramq.notification.bot.dto.ramq.BookableDay;
import com.github.ramq.notification.bot.dto.ramq.BookableTimeBlock;
import com.github.ramq.notification.bot.dto.ramq.Bookings;
import com.github.ramq.notification.bot.dto.ramq.BookingsRequest;
import com.github.ramq.notification.bot.dto.ramq.RamqServiceResource;
import com.github.ramq.notification.bot.dto.ramq.SchedulerResource;
import com.github.ramq.notification.bot.dto.ramq.StaffBookabilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RamqServiceTest {

    @InjectMocks
    @Spy
    private RamqService ramqService;

    @Mock
    private RestTemplate restTemplateMock;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup(){
        ReflectionTestUtils.setField(ramqService, "jsonRegex", "t(.{2})t");
        ReflectionTestUtils.setField(ramqService, "bookingsUri", "http://test.co");
        ReflectionTestUtils.setField(ramqService, "serviceRegex", "service");
        ReflectionTestUtils.setField(ramqService, "serviceUri", "http://test.co");
        doReturn("test").when(restTemplateMock).getForObject(anyString(), eq(String.class));
        doReturn(createFakeBookingsResponse()).when(restTemplateMock).postForObject(anyString(), any(BookingsRequest.class), eq(Bookings.class));
    }

    @Test
    void retrieveBookableTimeBlocks_shouldCurrentDayAsStartDate() throws JsonProcessingException {
        doReturn(createFakeSchedulerResource()).when(objectMapper).readValue(anyString(), eq(SchedulerResource.class));
        ArgumentCaptor<BookingsRequest> bookingsRequestCaptor = ArgumentCaptor.forClass(BookingsRequest.class);
        doReturn(LocalDateTime.of(LocalDate.of(2022, Month.JUNE, 5), LocalTime.now())).when(ramqService).getReferenceDate();

        ramqService.retrieveBookableTimeBlocks();

        verify(restTemplateMock).postForObject(anyString(), bookingsRequestCaptor.capture(), eq(Bookings.class));
        assertEquals(LocalDateTime.of(LocalDate.of(2022, Month.JUNE, 5), LocalTime.of(0,0,0)), bookingsRequestCaptor.getValue().getStart());
    }

    @Test
    void retrieveBookableTimeBlocks_shouldUseFirstDayOfNextMonthAsEndDate() throws JsonProcessingException {
        doReturn(createFakeSchedulerResource()).when(objectMapper).readValue(anyString(), eq(SchedulerResource.class));
        ArgumentCaptor<BookingsRequest> bookingsRequestCaptor = ArgumentCaptor.forClass(BookingsRequest.class);
        doReturn(LocalDateTime.of(LocalDate.of(2022, Month.JUNE, 5), LocalTime.now())).when(ramqService).getReferenceDate();

        ramqService.retrieveBookableTimeBlocks();

        verify(restTemplateMock).postForObject(anyString(), bookingsRequestCaptor.capture(), eq(Bookings.class));
        assertEquals(LocalDateTime.of(LocalDate.of(2022, Month.JULY, 1), LocalTime.of(0,0,0)), bookingsRequestCaptor.getValue().getEnd());
    }

    private SchedulerResource createFakeSchedulerResource() {
        RamqServiceResource serviceResource = new RamqServiceResource();
        serviceResource.setName("serviceResource");
        serviceResource.setStaffList(singletonList("staff"));

        SchedulerResource schedulerResource = new SchedulerResource();
        schedulerResource.setServices(singletonList(serviceResource));

        return schedulerResource;
    }

    private Bookings createFakeBookingsResponse() {
        BookableTimeBlock bookableTimeBlock = new BookableTimeBlock();
        bookableTimeBlock.setStart(LocalDateTime.now());

        StaffBookabilities staffBookabilities = new StaffBookabilities();
        staffBookabilities.setBookableDays(singletonList(new BookableDay()));
        staffBookabilities.setBookableTimeBlocks(singletonList(bookableTimeBlock));

        Bookings bookings = new Bookings();
        bookings.setStaffBookabilities(singletonList(staffBookabilities));

        return bookings;
    }
}