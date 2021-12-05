package ru.klimashin.ballistic.model;

import java.util.ArrayList;
import java.util.List;

import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.entity.Spacecraft;
import ru.klimashin.ballistic.model.math.Point3D;
import ru.klimashin.ballistic.model.math.Vector3D;
import ru.klimashin.ballistic.model.physic.BasicFormules;
import ru.klimashin.ballistic.model.physic.SpaceMechanic;


public class Trajectory {

	private List<Point3D> earthPositions = new ArrayList<>();
	private List<Point3D> spacecraftPositions = new ArrayList<>();
	private List<Double> distanceBetweenEarthAndSpacecraft = new ArrayList<>();
	
	private CelestialBody earth;
	private CelestialBody solar;
	
	private Spacecraft spacecraft;
	
	private ModelParameters mp;
	
	
	public Trajectory(ModelParameters mp, CelestialBody earth, CelestialBody solar, Spacecraft spacecraft) {
		this.mp = mp;
		this.earth = earth;
		this.solar = solar;
		this.spacecraft = spacecraft;
	}
	
	public void startCalculate() {

		double distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());
		
		earthPositions.add(earth.getPosition());
		spacecraftPositions.add(spacecraft.getPosition());
		distanceBetweenEarthAndSpacecraft.add(distance);
		
		Vector3D unitToSolar = new Vector3D();
		Vector3D gravForce = new Vector3D();
		
		Vector3D unitFromThrust = new Vector3D();
		Vector3D thrustForce = new Vector3D();
		
		Vector3D sumForce = new Vector3D();
		
		int t = mp.getStartTime();
		
		while (t <= mp.getFirstPartDuration() + mp.getSecondPartDuration()) {
			double rangeToSolar = spacecraft.getPosition().distanceToPoint(solar.getPosition());
			
			unitToSolar = Vector3D.betweenPoints(spacecraft.getPosition(), solar.getPosition()).getUnitVector();
			gravForce = new Vector3D(unitToSolar.mult(BasicFormules.gravitationForce(solar, spacecraft)));
			
			/* deprecated
			Vector3D unitToSolar = Vector3D.betweenPoints(spacecraft.getPosition(), solar.getPosition()).getUnitVector();
			Vector3D gravForce = new Vector3D(unitToSolar.mult(BasicFormules.gravitationForce(solar, spacecraft)));
			*/
			
			double angle;
			
			if(t <= mp.getFirstPartDuration()) {
				angle = mp.getFirstThrustAngle();
			} else {
				angle = mp.getSecondThrustAngle();
			}
			
			unitFromThrust = new Vector3D(unitToSolar).rotate(angle);
			thrustForce = new Vector3D(unitFromThrust.mult(spacecraft.getEngine().getThrust() * spacecraft.getEngineCount()));
			
			/* deprecated
			Vector3D unitFromThrust = new Vector3D(unitToSolar).rotate(angle);
			Vector3D thrustForce = new Vector3D(unitFromThrust.mult(spacecraft.getEngine().getThrust() * spacecraft.getEngineCount()));
			*/
			
			List<Vector3D> forces = new ArrayList<>();
			forces.add(gravForce);
			forces.add(thrustForce);
			
			sumForce = Vector3D.sumVectors(forces);
			
			/* deprecated
			Vector3D sumForce = Vector3D.sumVectors(forces);
			*/
			
			spacecraft.setAcceleration(BasicFormules.acceleration(sumForce, spacecraft.getMass()));
			spacecraft.setSpeed(BasicFormules.changeSpeed(spacecraft.getSpeed(), spacecraft.getAcceleration(), mp.getDeltaTime()));
			spacecraft.setPosition(BasicFormules.changePosition(spacecraft.getPosition(), spacecraft.getSpeed(), mp.getDeltaTime()));
			
			t = t + mp.getDeltaTime();
			
			earth.setPosition(SpaceMechanic.positionFromTime(earth, t));
			distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());
			
			earthPositions.add(earth.getPosition());
			spacecraftPositions.add(spacecraft.getPosition());
			distanceBetweenEarthAndSpacecraft.add(distance);
			
		}
		
	}

	
	
	public List<Point3D> getEarthPositions() {
		return earthPositions;
	}

	public void setEarthPositions(List<Point3D> earthPositions) {
		this.earthPositions = earthPositions;
	}

	public List<Point3D> getSpacecraftPositions() {
		return spacecraftPositions;
	}

	public void setSpacecraftPositions(List<Point3D> spacecraftPositions) {
		this.spacecraftPositions = spacecraftPositions;
	}

	public List<Double> getDistanceBetweenEarthAndSpacecraft() {
		return distanceBetweenEarthAndSpacecraft;
	}

	public void setDistanceBetweenEarthAndSpacecraft(List<Double> distanceBetweenEarthAndSpacecraft) {
		this.distanceBetweenEarthAndSpacecraft = distanceBetweenEarthAndSpacecraft;
	}
	
}

