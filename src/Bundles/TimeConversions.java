package Bundles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConversions {

    /**
     * Converts a given LocalDateTime from the user system's default to UTC
     *
     * @param ldt the LocalDateTime to convert
     * @return the given LocalDateTime converted to UTC
     */
    public static LocalDateTime convertToUtc(LocalDateTime ldt) {
        ZoneId systemZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");

        // Convert to equivalent ZonedDateTime
        ZonedDateTime systemZoneTime = ldt.atZone(systemZoneId);

        // Convert to UTC
        ZonedDateTime zonedUtc = systemZoneTime.withZoneSameInstant(utcZoneId);

        return zonedUtc.toLocalDateTime();
    }

    /**
     * Converts a given LocalDateTime from the user system's default to EST
     *
     * @param ldt the LocalDateTime to convert
     * @return the given LocalDateTime converted to EST
     */
    public static LocalDateTime convertToEst(LocalDateTime ldt) {
        ZoneId systemZoneId = ZoneId.systemDefault();
        ZoneId estZoneId = ZoneId.of("America/New_York");

        // Convert to equivalent ZonedDateTime
        ZonedDateTime systemZoneTime = ldt.atZone(systemZoneId);

        // Convert to EST
        ZonedDateTime zonedEst = systemZoneTime.withZoneSameInstant(estZoneId);

        return zonedEst.toLocalDateTime();
    }

    /**
     * Converts a given LocalDateTime from the EST to the user system's default time zone
     *
     * @param ldt the LocalDateTime to convert
     * @return the given LocalDateTime converted to the user system's default time zone
     */
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
