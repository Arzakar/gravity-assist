package ru.klimashin.ballistic.model.entity;

import ru.klimashin.ballistic.model.math.Point3D;
import ru.klimashin.ballistic.model.math.Vector3D;

public enum CelestialBody {

	SOLAR(
		"Солнце",							//name
		null,								//radius
		1.9885 * Math.pow(10, 30),			//mass
		1.32712440018 * Math.pow(10, 20),	//gravitationalParameter
		null,								//gravitationalRadius, 
		null,								//orbitRaius, 
		null,								//rotationPeriod, 
		null,								//eccentricity,
		null,								//semiMajorAxis, 
		null,								//semiMinorAxis, 
		null,								//linearEccentricity, 
		null,								//focalParameter,
		new Point3D(0, 0, 0),				//position, 
		null,								//speed, 
		null),								//acceleration
		
	
	EARTH(
		"Земля",							//name
		null,								//radius
		5.9722 * Math.pow(10, 24),			//mass
		3.986004355 * Math.pow(10, 14),		//gravitationalParameter
		Math.pow(10, 9),					//gravitationalRadius, 
		1.496 * Math.pow(10, 11),			//orbitRaius, 
		365.25 * 86400,						//rotationPeriod, 
		0d,									//eccentricity,
		1.496 * Math.pow(10, 11),			//semiMajorAxis, 
		1.496 * Math.pow(10, 11),			//semiMinorAxis, 
		0d,									//linearEccentricity, 
		1.496 * Math.pow(10, 11),			//focalParameter,
		null,								//position, 
		null,								//speed, 
		null								//acceleration
	);

	
	
	private String name;
	private Double radius;
	private Double mass;
	private Double gravitationalParameter;
	private Double gravitationalRadius;
	private Double orbitRaius;
	private Double rotationPeriod;
	private Double eccentricity;
	private Double semiMajorAxis;
	private Double semiMinorAxis;
	private Double linearEccentricity;
	private Double focalParameter;
	private Point3D position;
	private Vector3D speed;
	private Vector3D acceleration;
	

	
	private CelestialBody() { }

	private CelestialBody(String name, Double mass, Double gravitationalParameter, Point3D position) {
		this.name = name;
		this.mass = mass;
		this.gravitationalParameter = gravitationalParameter;
		this.position = position;
	}
	
	private CelestialBody(String name, 
			Double radius, 
			Double mass, 
			Double gravitationalParameter,
			Double gravitationalRadius, 
			Double orbitRaius, 
			Double rotationPeriod, 
			Double eccentricity,
			Double semiMajorAxis, 
			Double semiMinorAxis, 
			Double linearEccentricity, 
			Double focalParameter,
			Point3D position, 
			Vector3D speed, 
			Vector3D acceleration) {
		this.name = name;
		this.radius = radius;
		this.mass = mass;
		this.gravitationalParameter = gravitationalParameter;
		this.gravitationalRadius = gravitationalRadius;
		this.orbitRaius = orbitRaius;
		this.rotationPeriod = rotationPeriod;
		this.eccentricity = eccentricity;
		this.semiMajorAxis = semiMajorAxis;
		this.semiMinorAxis = semiMinorAxis;
		this.linearEccentricity = linearEccentricity;
		this.focalParameter = focalParameter;
		this.position = position;
		this.speed = speed;
		this.acceleration = acceleration;
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
	
	

	public String getName() {
		return name;
	}
	
	

	public Double getRadius() {
		return radius;
	}
	
	

	public Double getMass() {
		return mass;
	}
	
	

	public Double getGravitationalParameter() {
		return gravitationalParameter;
	}
	
	

	public Double getGravitationalRadius() {
		return gravitationalRadius;
	}
	
	

	public Double getOrbitRaius() {
		return orbitRaius;
	}
	
	

	public Double getRotationPeriod() {
		return rotationPeriod;
	}
	
	

	public Double getEccentricity() {
		return eccentricity;
	}
	
	

	public Double getSemiMajorAxis() {
		return semiMajorAxis;
	}
	
	

	public Double getSemiMinorAxis() {
		return semiMinorAxis;
	}
	
	

	public Double getLinearEccentricity() {
		return linearEccentricity;
	}
	
	

	public Double getFocalParameter() {
		return focalParameter;
	}
	
}
