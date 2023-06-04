package nd.darboKalendorius.OutputData;

import nd.darboKalendorius.HolidayInfo.EasterAndOther;
import nd.darboKalendorius.HolidayInfo.HolidayDates;
import nd.darboKalendorius.InputData.InputDay;
import nd.darboKalendorius.InputData.Input;
import nd.darboKalendorius.InputData.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutputService {

    private final InputService inputService;

    @Autowired
    public OutputService(InputService inputService) {
        this.inputService = inputService;
    }

    public Output getOutputData() {
        Input input = inputService.getInputData();
        return calculateOutput(input);
    }

    private Output calculateOutput(Input input){

        //sarašas datų ir laisvų valandų kiekio tą dieną
        Map<LocalDate, Integer> workdays = new HashMap<>();
        Map<LocalDate, Integer> weekends = new HashMap<>();
        Map<LocalDate, Integer> holidays = new HashMap<>();

        //išskirsto dienas į darbo, savaitgalius ir šventines
        dayClassification(input, workdays, weekends, holidays);


        //laisvų valandų kiekiai
        int workdaysSum = workdays.values().stream().reduce(0, Integer::sum);
        int weekendsSum = weekends.values().stream().reduce(0, Integer::sum);
        int holidaysSum = holidays.values().stream().reduce(0, Integer::sum);

        double[] percents = {0, 0};
        Output output = new Output();
        output.setDoable(true);

        //apskaičiuoja procentalų darbo krūvį savaitgaliais ir šventinėm dienom
        //ir patikrina ar įmanoma atlikti darbą
        calculatePercentages(input, workdaysSum, weekendsSum, holidaysSum, percents, output);


        //jei darbo atlikti neimanoma grįžta
        if(!output.isDoable()) return output;

        double weekendPercent = percents[0];  //savaitgalių procentalus krūvis
        double holidayPercent = percents[1];  //šventinių dienų procentalus krūvis

        List<OutputDay> outputDays = new ArrayList<>();
        int usedHours = 0;

        LocalDate start = LocalDate.parse(input.getStartDate());
        LocalDate end = LocalDate.parse(input.getDeadline());

        //ciklas nuo darbo laikotarpio pradžios iki pabaigos
        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1))
        {
            //apskaičiuoja bendra procentą kiek šiai dienai reikia skirti valandų darbui
            double percentage = (input.getHours() - usedHours) / (workdaysSum + Math.ceil(weekendsSum * weekendPercent) + Math.ceil(holidaysSum * holidayPercent));

            OutputDay outputDay = new OutputDay();
            outputDay.setDate(date.toString());

            int h;  //darbui skirtų valandų kiekis

            if(workdays.containsKey(date)) {
                outputDay.setDayType("Workday");
                h = (int) Math.round(workdays.get(date) * percentage);
                workdaysSum -= workdays.get(date);
            }
            else if(weekends.containsKey(date)) {
                outputDay.setDayType("Weekend");
                h = (int) Math.round(weekends.get(date) * weekendPercent * percentage);
                weekendsSum -= weekends.get(date);
            }
            else {
                outputDay.setDayType("Holiday");
                h = (int) Math.round(holidays.get(date) * holidayPercent * percentage);
                holidaysSum -= holidays.get(date);
            }

            usedHours += h;
            outputDay.setWorkHours(h);
            outputDays.add(outputDay);
        }

        output.setOutputDays(outputDays);
        return output;
    }

    private void dayClassification(Input input,
                                   Map<LocalDate, Integer> workdays,
                                   Map<LocalDate, Integer> weekends,
                                   Map<LocalDate, Integer> holidays) {

        LocalDate start = LocalDate.parse(input.getStartDate());
        LocalDate end = LocalDate.parse(input.getDeadline());

        List<String> yearlyHolidays = new HolidayDates().getHolidays();                                 //pastovios metinės šventės
        List<LocalDate> easterPlus = new EasterAndOther().getHolidays(start.getYear(), end.getYear());  //nepastovios šventės

        //ciklas nuo darbo laikotarpio pradžios iki pabaigos
        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {

            int hours = busyHours(date, input);                                  //užimtos valandos tą dieną
            String monthAndDay = date.getMonthValue()+"-"+date.getDayOfMonth();  //mėnesis ir diena  MM-DD

            if(yearlyHolidays.contains(monthAndDay) || easterPlus.contains(date)) holidays.put(date, 16 - hours);
            else if(date.getDayOfWeek().name().equals("SATURDAY") || date.getDayOfWeek().name().equals("SUNDAY")) weekends.put(date, 16 - hours);
            else workdays.put(date, 16 - hours);
        }
    }

    private int busyHours(LocalDate date, Input input) {

        for (InputDay day : input.getDays()) {
            if(date.toString().equals(day.getDate())) {
                return Math.min(day.getBusyHours(), 16);
            }
        }
        return 0;
    }

    private void calculatePercentages(Input input, int workdaysSum, int weekendsSum, int holidaysSum, double[] percents, Output output) {

        //percents[0]  -  savaitgalių darbo procentali dalis
        //percents[1]  -  šventinių dienų darbo procentali dalis

        if(workdaysSum + Math.ceil((double) weekendsSum/2) >= input.getHours()) {
            percents[0] = 0.5;
            output.setMode("holidays-0%, weekends-50%");
            return;
        }

        if (workdaysSum + Math.ceil((double) weekendsSum/2) + Math.ceil((double) holidaysSum/2) >= input.getHours()) {
            percents[0] = 0.5;
            percents[1] = 0.5;
            output.setMode("holidays-50%, weekends-50%");
            return;
        }

        if (workdaysSum + weekendsSum + Math.ceil((double) holidaysSum/2) >= input.getHours()) {
            percents[0] = 1;
            percents[1] = 0.5;
            output.setMode("holidays-50%, weekends-100%");
            return;
        }

        if (workdaysSum + weekendsSum + holidaysSum >= input.getHours()) {
            percents[0] = 1;
            percents[1] = 1;
            output.setMode("holidays-100%, weekends-100%");
            return;
        }

        output.setDoable(false);
    }
}
