package com.example.plan.enums;

public enum Duration {

    ΜΙΑ_ΕΒΔΟΜΑΔΑ("ΜΙΑ_ΕΒΔΟΜΑΔΑ"),
    ΔΥΟ_ΕΒΔΟΜΑΔΕΣ("ΔΥΟ_ΕΒΔΟΜΑΔΕΣ"),
    ΤΡΕΙΣ_ΕΒΔΟΜΑΔΕΣ("ΤΡΕΙΣ_ΕΒΔΟΜΑΔΕΣ"),
    ΤΕΣΣΕΡΗΣ_ΕΒΔΟΜΑΔΕΣ("ΤΕΣΣΕΡΗΣ_ΕΒΔΟΜΑΔΕΣ");


    private final String durationString;

    Duration(String durationString) {
        this.durationString = durationString;
    }
    public static Duration parse(String durationString) {
        for (Duration duration : values()) {
            if (duration.durationString.equalsIgnoreCase(durationString)) {
                return duration;
            }
        }
        return null;
    }

    /*ONE_WEEK("Μια εβδομάδα"),
    TWO_WEEKS("Δύο εβδομάδες"),
    THREE_WEEKS("Τρεις εβδομάδες"),
    FOUR_WEEKS("Τέσσερης εβδομάδες");

    private final String label;

    Duration(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }*/
}




