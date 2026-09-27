package com.pratima.makemyenotes.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class DateTimeUtility {

    public LocalDateTime convertDateToLocalDateTime(Date date){
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return localDateTime;
    }

    public Date convertLocalDateTimeToDate(LocalDateTime date) {
        if (date == null) return null;
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

}
