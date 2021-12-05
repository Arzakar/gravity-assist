package com.klimashin.model.entity.celestial;

import lombok.*;
import com.klimashin.model.util.math.Point3D;
import com.klimashin.model.util.math.Vector3D;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CelestialBody {

    private String name;
    private Double radius;
    private Double mass;
    private Double gravitationalParameter;
    private Double orbitRadius;
    private Double gravitationalRadius;
    private Double rotationPeriod;
    private Double eccentricity;
    private Double semiMajorAxis;
    private Double semiMinorAxis;
    private Double linearEccentricity;
    private Double focalParameter;
    private Point3D position;
    private Vector3D speed;
    private Vector3D acceleration;

}
