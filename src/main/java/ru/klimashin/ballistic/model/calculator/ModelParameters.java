package ru.klimashin.ballistic.model.calculator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelParameters {

	private int startTime;
	private int deltaTime;

	private int firstPartDuration;
	private int secondPartDuration;

	private double firstThrustAngle;
	private double secondThrustAngle;

}
