package com.klimashin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.klimashin.model.util.math.Point3D;
import com.klimashin.model.util.math.Vector3D;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spacecraft {

    private String name;
    private double mass;
    private Engine engine;
    private int engineCount;
    private Point3D position;
    private Vector3D speed;
    private Vector3D acceleration;

    public double getThrust() {
        return engine.getThrust() * engineCount;
    }
}
