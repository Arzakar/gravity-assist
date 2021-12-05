package ru.klimashin.ballistic.model.physic;

import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.math.Point3D;

public class SpaceMechanic {

	public static double radialSpeed(double gravitationalParameter, double focalParameter,
			double eccentricity, double trueAnomaly) {
		
		return Math.sqrt(gravitationalParameter / focalParameter) * eccentricity * Math.sin(trueAnomaly);
	}
	
	public static double transversalSpeed(double gravitationalParameter, double focalParameter,
			double eccentricity, double trueAnomaly) {
		
		return Math.sqrt(gravitationalParameter / focalParameter) * (1 + eccentricity * Math.cos(trueAnomaly) );
	}
	
	
	
	public static Point3D positionFromTime(CelestialBody body, double time) {
		return new Point3D(body.getOrbitRaius() * Math.cos( (time / body.getRotationPeriod()) * 2 * Math.PI ),
				body.getOrbitRaius() * Math.sin( (time / body.getRotationPeriod()) * 2 * Math.PI ),
				0);
	}
	
}
