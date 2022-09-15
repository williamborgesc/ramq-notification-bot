package com.github.ramq.notification.bot.schedule;

import com.github.ramq.notification.bot.dto.ramq.BookableTimeBlock;
import com.github.ramq.notification.bot.service.NotificationService;
import com.github.ramq.notification.bot.service.RamqService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RamqSchedule {

    private final NotificationService notificationService;

    private final RamqService ramqService;

    //TODO stop scheduler when find new times (maybe notify that there's no more spots available
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void checkForAppointmentSlots() {
        log.info("Begin checkForAppointmentSlots");

        if (checkAvailability()) {
            log.info("Found new slots, sending notification email");
            notificationService.sendNotification();
        }
        log.info("End checkForAppointmentSlots");
    }

    @SneakyThrows
    private boolean checkAvailability() {
        log.info("Begin checkAvailability");
        Set<BookableTimeBlock> bookableTimeBlocks = ramqService.retrieveBookableTimeBlocks();
        log.info("Got bookableTimeBlocks={}", bookableTimeBlocks);

        log.info("End checkAvailability");
        return !bookableTimeBlocks.isEmpty();
    }
}
