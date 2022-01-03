package ru.klimashin.ballistic.model.util;

public final class GeneralFormulas {

    private static final int RATE = 86400;

    public static double secondsToDays(int seconds) {
        return (double) seconds / RATE;
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

    public static double kmToM(double km) {
        return km * 1000;
    }

    public static double mToKm(double m) {
        return m / 1000;
    }
}
