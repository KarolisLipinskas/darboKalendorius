package nd.darboKalendorius.InputData;

import java.util.List;

public class Input {
    private int hours;
    private String startDate;
    private String deadline;
    private List<InputDay> days;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<InputDay> getDays() {
        return days;
    }

    public void setDays(List<InputDay> days) {
        this.days = days;
    }
}
