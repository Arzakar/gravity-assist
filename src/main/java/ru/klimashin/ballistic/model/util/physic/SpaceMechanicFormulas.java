package ru.klimashin.ballistic.model.util.physic;

import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.util.math.Point3D;

public class SpaceMechanicFormulas {

	public static double radialSpeed(double gravitationalParameter, double focalParameter,
			double eccentricity, double trueAnomaly) {
		
		return Math.sqrt(gravitationalParameter / focalParameter) * eccentricity * Math.sin(trueAnomaly);
	}
	
	public static double transversalSpeed(double gravitationalParameter, double focalParameter,
			double eccentricity, double trueAnomaly) {
		
		return Math.sqrt(gravitationalParameter / focalParameter) * (1 + eccentricity * Math.cos(trueAnomaly) );
	}
	
	
	
	public static Point3D positionFromTime(CelestialBody body, double time) {
		return new Point3D(body.getOrbitRadius() * Math.cos( (time / body.getRotationPeriod()) * 2 * Math.PI ),
				body.getOrbitRadius() * Math.sin( (time / body.getRotationPeriod()) * 2 * Math.PI ),
				0);
	}
	
}
