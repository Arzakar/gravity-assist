package com.klimashin.model.entity.celestial;

import com.klimashin.model.util.math.Point3D;

public final class Solar extends CelestialBody {

    private static final String SOLAR_NAME = "Solar";
    private static final Double SOLAR_RADIUS = 6.960 * Math.pow(10, 8);
    private static final Double SOLAR_MASS = 1.9885 * Math.pow(10, 30);
    private static final Double SOLAR_GRAVITATIONAL_PARAMETER = 1.32712440018 * Math.pow(10, 11);
    private static final Double SOLAR_ORBIT_RADIUS = 0d;
    private static final Double SOLAR_GRAVITATIONAL_RADIUS = null;
    private static final Double SOLAR_ROTATION_PERIOD = 0d;
    private static final Double SOLAR_ECCENTRICITY = 0d;
    private static final Double SOLAR_SEMI_MAJOR_AXIS = 0d;
    private static final Double SOLAR_SEMI_MINOR_AXIS = 0d;
    private static final Double SOLAR_LINEAR_ECCENTRICITY = 0d;
    private static final Double SOLAR_FOCAL_PARAMETER = 0d;

    public Solar() {
        super(SOLAR_NAME,
                SOLAR_RADIUS,
                SOLAR_MASS,
                SOLAR_GRAVITATIONAL_PARAMETER,
                SOLAR_ORBIT_RADIUS,
                SOLAR_GRAVITATIONAL_RADIUS,
                SOLAR_ROTATION_PERIOD,
                SOLAR_ECCENTRICITY,
                SOLAR_SEMI_MAJOR_AXIS,
                SOLAR_SEMI_MINOR_AXIS,
                SOLAR_LINEAR_ECCENTRICITY,
                SOLAR_FOCAL_PARAMETER,
                new Point3D(0d, 0d, 0d),
                null,
                null);
    }
}
