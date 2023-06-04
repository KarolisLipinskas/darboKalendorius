package nd.darboKalendorius.HolidayInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EasterAndOther {
    List<LocalDate> holidays = new ArrayList<>();

    public void setHolidays(int startYear, int endYear) {

        for(int year = startYear; year <= endYear; year++) {

            //Easter days
            LocalDate easter = getEasterDay(year);
            holidays.add(easter);
            holidays.add(easter.plusDays(1));

            //Mothers and fathers days
            for(int month = 5; month <= 6; month++) {
                LocalDate start = LocalDate.of(year, month, 1);
                while (!start.getDayOfWeek().name().equals("SUNDAY")) start = start.plusDays(1);
                holidays.add(start);
            }
        }
    }

    public List<LocalDate> getHolidays(int startYear, int endYear) {
        this.setHolidays(startYear, endYear);
        return holidays;
    }

    private static LocalDate getEasterDay(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = ((19 * a) + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + (2 * e) + (2 * i) - h - k) % 7;
        int m = (a + (11 * h) + (22 * l)) / 451;
        int n = (h + l - (7 * m) + 114) / 31;     // month
        int p = (h + l - (7 * m) + 114) % 31 + 1; // day

        return LocalDate.of(year,n,p);
    }
}
