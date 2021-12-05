package com.klimashin.model.entity.celestial;

import com.klimashin.model.util.math.GeneralFormulas;
import com.klimashin.model.util.math.Point3D;
import com.klimashin.model.util.math.Vector3D;

public final class Earth extends CelestialBody {

    private static final String EARTH_NAME = "Earth";
    private static final Double EARTH_RADIUS = 6_371d;
    private static final Double EARTH_MASS = 5.9722 * Math.pow(10, 24);
    private static final Double EARTH_GRAVITATIONAL_PARAMETER = 398_600.3455;
    private static final Double EARTH_ORBIT_RADIUS = 149.598261 * Math.pow(10, 6);
    private static final Double EARTH_GRAVITATIONAL_RADIUS = Math.pow(10, 6);
    private static final Double EARTH_ROTATION_PERIOD = GeneralFormulas.daysToSeconds(365.25);
    private static final Double EARTH_ECCENTRICITY = 0d;
    private static final Double EARTH_SEMI_MAJOR_AXIS = 149.598261 * Math.pow(10, 6);
    private static final Double EARTH_SEMI_MINOR_AXIS = 149.598261 * Math.pow(10, 6);
    private static final Double EARTH_LINEAR_ECCENTRICITY = 0d;
    private static final Double EARTH_FOCAL_PARAMETER = 149.598261 * Math.pow(10, 6);

    public Earth() {
        super(EARTH_NAME,
                EARTH_RADIUS,
                EARTH_MASS,
                EARTH_GRAVITATIONAL_PARAMETER,
                EARTH_ORBIT_RADIUS,
                EARTH_GRAVITATIONAL_RADIUS,
                EARTH_ROTATION_PERIOD,
                EARTH_ECCENTRICITY,
                EARTH_SEMI_MAJOR_AXIS,
                EARTH_SEMI_MINOR_AXIS,
                EARTH_LINEAR_ECCENTRICITY,
                EARTH_FOCAL_PARAMETER,
                null,
                null,
                null);
    }

    public Earth(Point3D position, Vector3D speed, Vector3D acceleration) {
        super(EARTH_NAME,
                EARTH_RADIUS,
                EARTH_MASS,
                EARTH_GRAVITATIONAL_PARAMETER,
                EARTH_ORBIT_RADIUS,
                EARTH_GRAVITATIONAL_RADIUS,
                EARTH_ROTATION_PERIOD,
                EARTH_ECCENTRICITY,
                EARTH_SEMI_MAJOR_AXIS,
                EARTH_SEMI_MINOR_AXIS,
                EARTH_LINEAR_ECCENTRICITY,
                EARTH_FOCAL_PARAMETER,
                position,
                speed,
                acceleration);
    }

}
