package config.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CurrentDateTime {
    private static final String ZONE_ID = "America/Argentina/Buenos_Aires";

    public static String defaultFormat() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return currentDateTime.format(formatter);
    }

    public static LocalDateTime from(long timestamp) {
        // Convert timestamp to Instant object
        Instant instant = Instant.ofEpochSecond(timestamp);

        //Convert Instant to LocalDateTime in a specific time zone
        //it can change as it need it
        ZoneId zoneId = ZoneId.of(ZONE_ID);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime;
    }

    public static String defaultFormat(LocalDateTime localDateTime) {
        // Format the date and time in a readable format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        return formattedDateTime;
    }
}
