package ru.klimashin.ballistic.model;

public class ModelParameters {

	private int startTime;
	private int firstPartDuration;
	private int secondPartDuration;
	private int deltaTime;
	
	private double firstThrustAngle;
	private double secondThrustAngle;
	
	
	
	public ModelParameters(int startTime, int firstPartDuration, int secondPartDuration, int deltaTime,
			double firstThrustAngle, double secondThrustAngle) {
		super();
		this.startTime = startTime;
		this.firstPartDuration = firstPartDuration;
		this.secondPartDuration = secondPartDuration;
		this.deltaTime = deltaTime;
		this.firstThrustAngle = firstThrustAngle;
		this.secondThrustAngle = secondThrustAngle;
	}



	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}


	public int getFirstPartDuration() {
		return firstPartDuration;
	}

	public void setFirstPartDuration(int firstPartDuration) {
		this.firstPartDuration = firstPartDuration;
	}


	public int getSecondPartDuration() {
		return secondPartDuration;
	}

	public void setSecondPartDuration(int secondPartDuration) {
		this.secondPartDuration = secondPartDuration;
	}


	public int getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(int deltaT) {
		this.deltaTime = deltaT;
	}


	public double getFirstThrustAngle() {
		return firstThrustAngle;
	}

	public void setFirstThrustAngle(double firstThrustAngle) {
		this.firstThrustAngle = firstThrustAngle;
	}


	public double getSecondThrustAngle() {
		return secondThrustAngle;
	}

	public void setSecondThrustAngle(double secondThrustAngle) {
		this.secondThrustAngle = secondThrustAngle;
	}
	
}
