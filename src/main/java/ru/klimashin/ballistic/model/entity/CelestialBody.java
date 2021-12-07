package ru.klimashin.ballistic.model.entity;

import lombok.*;
import ru.klimashin.ballistic.model.util.math.Point3D;
import ru.klimashin.ballistic.model.util.math.Vector3D;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CelestialBody {

	SOLAR(
		"Солнце",
		null,
		1.9885 * Math.pow(10, 30),
		1.32712440018 * Math.pow(10, 20),
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
			new Point3D(0, 0, 0),
		null,
		null),

	EARTH(
		"Земля",
		null,
		5.9722 * Math.pow(10, 24),
		3.986004355 * Math.pow(10, 14),
		1 * Math.pow(10, 9),
		1.496 * Math.pow(10, 11),
		365.25 * 86400,
		0d,
		1.496 * Math.pow(10, 11),
		1.496 * Math.pow(10, 11),
		0d,
		1.496 * Math.pow(10, 11),
		null,
		null,
		null
	);

	@Setter private String name;
	@Setter private Double radius;
	@Setter private Double mass;
	@Setter private Double gravitationalParameter;
	@Setter private Double gravitationalRadius;
	@Setter private Double orbitRadius;
	@Setter private Double rotationPeriod;
	@Setter private Double eccentricity;
	@Setter private Double semiMajorAxis;
	@Setter private Double semiMinorAxis;
	@Setter private Double linearEccentricity;
	@Setter private Double focalParameter;
	@Setter private Point3D position;
	@Setter private Vector3D speed;
	@Setter private Vector3D acceleration;

}
