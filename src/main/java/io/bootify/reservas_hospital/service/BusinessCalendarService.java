package io.bootify.reservas_hospital.service;

import java.time.DayOfWeek;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BusinessCalendarService {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessCalendarService.class);

    private final Set<MonthDay> recurringHolidays = new HashSet<>();
    private final Set<LocalDate> fixedHolidays = new HashSet<>();

    public BusinessCalendarService(@Value("${reservas.festivos:}") String configuredHolidays) {
        parseConfiguredHolidays(configuredHolidays);
    }

    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public boolean isHoliday(LocalDate date) {
        return fixedHolidays.contains(date) || recurringHolidays.contains(MonthDay.from(date));
    }

    public boolean isNonWorkingDay(LocalDate date) {
        return isWeekend(date) || isHoliday(date);
    }

    private void parseConfiguredHolidays(String configuredHolidays) {
        if (configuredHolidays == null || configuredHolidays.isBlank()) {
            return;
        }

        String[] tokens = configuredHolidays.split(",");
        for (String raw : tokens) {
            String value = raw.trim();
            if (value.isEmpty()) {
                continue;
            }
            String[] parts = value.split("-");
            try {
                if (parts.length == 3) {
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);
                    fixedHolidays.add(LocalDate.of(year, month, day));
                } else if (parts.length == 2) {
                    int month = Integer.parseInt(parts[0]);
                    int day = Integer.parseInt(parts[1]);
                    recurringHolidays.add(MonthDay.of(month, day));
                } else {
                    LOG.warn("Ignoring holiday entry with unexpected format: {}", value);
                }
            } catch (NumberFormatException | DateTimeException ex) {
                LOG.warn("Ignoring invalid holiday entry: {}", value, ex);
            }
        }
    }
}
