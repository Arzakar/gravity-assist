package ru.klimashin.ballistic.model.calculator.earthToEarth;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Monitor {

    private int completedIterations;
    private int sumOperations;
    private int firstPartDuration;
    private int secondPartDuration;
    private double firstPartAngle;
    private double secondPartAngle;
    private LocalTime startTime;
    private LocalTime currentTime;
}
