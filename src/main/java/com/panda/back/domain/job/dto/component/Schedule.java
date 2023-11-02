package com.panda.back.domain.job.dto.component;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Schedule {
    private String timeZone;
    private double expiresAt;
    private List<Integer> months;
    private List<Integer> mdays;
    private List<Integer> hours;
    private List<Integer> minutes;
    private List<Integer> wdays;

    public Schedule(LocalDateTime endDateTime) {
        this.timeZone = "Asia/Seoul";
        this.expiresAt = makeExpiresAt(endDateTime);
        this.months = new ArrayList<>(Arrays.asList(endDateTime.getMonthValue()));
        this.mdays = new ArrayList<>(Arrays.asList(endDateTime.getDayOfMonth()));
        this.hours = new ArrayList<>(Arrays.asList(endDateTime.getHour()));
        this.minutes = new ArrayList<>(Arrays.asList(endDateTime.getMinute()));
        this.wdays = new ArrayList<>(Arrays.asList((endDateTime.getDayOfWeek().getValue() % 7)));
    }
    private static double makeExpiresAt(LocalDateTime auctionEnd) {
        LocalDateTime exDateTime = auctionEnd.plusMinutes(3);
        int year = exDateTime.getYear();
        int month = exDateTime.getMonthValue();
        int day = exDateTime.getDayOfMonth();
        int hour = exDateTime.getHour();
        int minute = exDateTime.getMinute();
        int seconds = exDateTime.getSecond();

        double expiredAt = (year * Math.pow(10, 10))
                + (month * Math.pow(10, 8))
                + (day * Math.pow(10, 6))
                + (hour * Math.pow(10, 4))
                + (minute * Math.pow(10, 2))
                + seconds;
        return expiredAt;
    }
}
