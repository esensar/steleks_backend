package ba.steleks.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ensar on 30/05/17.
 */

public class CalendarUtils {
    public static Calendar getUTCCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }
}
