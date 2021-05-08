package com.kdimitrov.edentist.server.common.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class WorkableLocalDateTimeUtil {

    public static final int WORKDAY_END = 20;
    public static final int WORKDAY_START = 8;

    public static LocalDateTime workable(LocalDateTime localDateTime) {
        if (isSaturday(localDateTime)) {
            return LocalDateTime.of(localDateTime.plusDays(2).toLocalDate(), LocalTime.of(8, 0));
        } else if (isSunday(localDateTime)) {
            return LocalDateTime.of(localDateTime.plusDays(1).toLocalDate(), LocalTime.of(8, 0));
        } else if (isAfterWorkDay(localDateTime)) {
            return workable(LocalDateTime.of(localDateTime.plusDays(1).toLocalDate(), LocalTime.of(8, 0)));
        } else if (isBeforeWorkDay(localDateTime)) {
            return workable(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(8, 0)));
        }
        return localDateTime;
    }

    private static boolean isAfterWorkDay(LocalDateTime localDateTime) {
        return localDateTime.getHour() >= WORKDAY_END;
    }

    private static boolean isBeforeWorkDay(LocalDateTime localDateTime) {
        return localDateTime.getHour() < WORKDAY_START;
    }

    private static boolean isSaturday(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getValue() == 6;
    }

    private static boolean isSunday(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getValue() == 7;
    }
}
