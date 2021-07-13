package Bundles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConversions {

    public static LocalDateTime convertToUtc(LocalDateTime ldt) {
        ZoneId systemZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");

        // Convert to equivalent ZonedDateTime
        ZonedDateTime systemZoneTime = ldt.atZone(systemZoneId);

        // Convert to UTC
        ZonedDateTime zonedUtc = systemZoneTime.withZoneSameInstant(utcZoneId);

        return zonedUtc.toLocalDateTime();
    }

    public static LocalDateTime convertToEst(LocalDateTime ldt) {
        ZoneId systemZoneId = ZoneId.systemDefault();
        ZoneId estZoneId = ZoneId.of("America/New_York");

        // Convert to equivalent ZonedDateTime
        ZonedDateTime systemZoneTime = ldt.atZone(systemZoneId);

        // Convert to EST
        ZonedDateTime zonedEst = systemZoneTime.withZoneSameInstant(estZoneId);

        return zonedEst.toLocalDateTime();
    }

    public static LocalDateTime fromEstToLocal(LocalDateTime ldt) {

        ZoneId systemZoneId = ZoneId.systemDefault();
        ZoneId estZoneId = ZoneId.of("America/New_York");

        // Convert to equivalent ZonedDateTime
        ZonedDateTime estZoneTime = ldt.atZone(estZoneId);

        // Convert to system default time
        ZonedDateTime systemZoneTime = estZoneTime.withZoneSameInstant(systemZoneId);

        return systemZoneTime.toLocalDateTime();
    }

}
