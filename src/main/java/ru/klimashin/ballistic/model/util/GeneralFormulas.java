package ru.klimashin.ballistic.model.util;

public final class GeneralFormulas {

    private static final int RATE = 86400;

    public static double secondsToDays(int seconds) {
        return seconds / RATE;
    }

    public static double secondsToDays(double seconds) {
        return seconds / RATE;
    }

    public static int daysToSeconds(int days) {
        return days * RATE;
    }

    public static int daysToSeconds(double days) {
        return (int) days * RATE;
    }
}
