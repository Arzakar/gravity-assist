package com.klimashin.model.util.physic;

import com.klimashin.model.entity.celestial.CelestialBody;
import com.klimashin.model.util.math.Basic3D;
import com.klimashin.model.util.math.Point3D;

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
		return new Point3D(body.getOrbitRadius() * Math.cos( (time / body.getRotationPeriod()) * 2 * Math.PI ),
				body.getOrbitRadius() * Math.sin( (time / body.getRotationPeriod()) * 2 * Math.PI ),
				0);
	}
	
}
