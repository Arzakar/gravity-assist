package com.klimashin.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AngleAndTimeParameters {

    private int startTime;
    private int deltaTime;

    private int firstPartDuration;
    private double firstTrustAngle;

    private int secondPartDuration;
    private double secondThrustAngle;

}
