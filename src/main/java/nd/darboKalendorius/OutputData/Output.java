package nd.darboKalendorius.OutputData;

import java.util.List;

public class Output {
    private boolean doable;
    private String mode;
    private List<OutputDay> outputDays;

    public boolean isDoable() {
        return doable;
    }

    public void setDoable(boolean doable) {
        this.doable = doable;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<OutputDay> getOutputDays() {
        return outputDays;
    }

    public void setOutputDays(List<OutputDay> outputDays) {
        this.outputDays = outputDays;
    }
}
