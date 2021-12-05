package ru.klimashin.ballistic.model.entity;

import ru.klimashin.ballistic.model.math.Point3D;
import ru.klimashin.ballistic.model.math.Vector3D;

public class Spacecraft {

	private String name;
	
	private double mass;
	
	private Engine engine;
	
	private int engineCount;
	
	private Point3D position;
	
	private Vector3D speed;
	
	private Vector3D acceleration;
	
	
	
	public Spacecraft() {}
	
	public Spacecraft(String name, double mass, Engine engine, int engineCount,
			Point3D position, Vector3D speed, Vector3D acceleration) {
		
		this.name = name;
		this.mass = mass;
		this.engine = engine;
		this.engineCount = engineCount;
		this.position = position;
		this.speed = speed;
		this.acceleration = acceleration;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	
	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	
	public int getEngineCount() {
		return engineCount;
	}

	public void setEngineCount(int engineCount) {
		this.engineCount = engineCount;
	}
	
	

	public Point3D getPosition() {
		return position;
	}
	

	public void setPosition(Point3D position) {
		this.position = position;
	}
	
	

	public Vector3D getSpeed() {
		return speed;
	}
	

	public void setSpeed(Vector3D speed) {
		this.speed = speed;
	}
	
	

	public Vector3D getAcceleration() {
		return acceleration;
	}
	

	public void setAcceleration(Vector3D acceleration) {
		this.acceleration = acceleration;
	}
	
}
