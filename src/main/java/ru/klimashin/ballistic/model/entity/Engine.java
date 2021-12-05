package ru.klimashin.ballistic.model.entity;

public class Engine {

	private String name;
	
	private double thrust;
	
	
	
	public Engine() {}
	
	public Engine(String name, double thrust) {
		this.name = name;
		this.thrust = thrust;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public double getThrust() {
		return thrust;
	}

	public void setThrust(double thrust) {
		this.thrust = thrust;
	}
	
}
