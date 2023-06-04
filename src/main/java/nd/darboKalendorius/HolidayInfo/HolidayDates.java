package nd.darboKalendorius.HolidayInfo;

import java.util.ArrayList;
import java.util.List;

public class HolidayDates {
    List<String> holidays = new ArrayList<>();

    public void setHolidays() {
        holidays.add("1-1");
        holidays.add("2-16");
        holidays.add("3-11");
        holidays.add("5-1");
        holidays.add("6-24");
        holidays.add("7-6");
        holidays.add("8-15");
        holidays.add("11-1");
        holidays.add("11-2");
        holidays.add("12-24");
        holidays.add("12-25");
        holidays.add("12-26");
    }

    public List<String> getHolidays() {
        this.setHolidays();
        return holidays;
    }
}
