package ru.klimashin.ballistic.model.calculator.earthToEarth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.Math.toDegrees;
import static ru.klimashin.ballistic.model.util.GeneralFormulas.secondsToDays;

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

	@Override
	public String toString() {
		return secondsToDays(firstPartDuration) + " "
				+ String.format("%.2f", toDegrees(firstThrustAngle)) + " "
				+ secondsToDays(secondPartDuration) + " "
				+ String.format("%.2f", toDegrees(secondThrustAngle));
	}
}
