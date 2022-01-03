package ru.klimashin.ballistic.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.klimashin.ballistic.model.util.math.Point3D;
import ru.klimashin.ballistic.model.util.math.Vector3D;

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

}
