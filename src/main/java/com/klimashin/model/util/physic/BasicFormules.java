package com.klimashin.model.util.physic;

import com.klimashin.model.entity.Spacecraft;
import com.klimashin.model.entity.celestial.CelestialBody;
import com.klimashin.model.util.math.Point3D;
import com.klimashin.model.util.math.Vector3D;


public class BasicFormules implements FundamentalConstants {

	public static Vector3D acceleration(Vector3D force, double mass) {
		return new Vector3D(force.div(mass));
	}
	
	public static Vector3D speed(Vector3D acceleration, double time) {
		return new Vector3D(acceleration.mult(time));
	}
	
	public static Vector3D changeSpeed(Vector3D prevSpeed, Vector3D acceleration, double time) {
		return new Vector3D(prevSpeed.plus(speed(acceleration, time)));
	}
	
	public static Vector3D relocation(Vector3D speed, double time) {
		return new Vector3D(speed.mult(time));
	}
	
	public static Point3D changePosition(Point3D prevPosition, Vector3D speed, double time) {
		return new Point3D(prevPosition.plus(relocation(speed, time)));
	}

	public static Vector3D sumForce(Vector3D ... forces) {
		return Vector3D.sumVectors(forces);
	}
	
	public static double gravitationForce(double firstMass, double secondMass, double range) {
		return G * ( (firstMass * secondMass) / Math.pow(range, 2) );
	}
	
	public static double gravitationForce(CelestialBody firstBody, CelestialBody secondBody) {
		return G * ( (firstBody.getMass() * secondBody.getMass()) 
				/ Math.pow( (Point3D.distanceBetweenPoints(firstBody.getPosition(), secondBody.getPosition())) , 2) );
	}
	
	public static double gravitationForce(CelestialBody body, Spacecraft spacecraft) {
		return G * ( (body.getMass() * spacecraft.getMass()) 
				/ Math.pow( (Point3D.distanceBetweenPoints(body.getPosition(), spacecraft.getPosition())) , 2) );
	}
}
