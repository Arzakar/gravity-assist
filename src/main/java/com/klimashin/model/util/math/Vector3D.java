package com.klimashin.model.util.math;

import lombok.Data;

@Data
public class Vector3D extends Basic3D {

	public Vector3D() {
		super(0, 0, 0);
	}

	public Vector3D(double x, double y, double z) {
		super(x, y, z);
	}

	public Vector3D(Basic3D basic3D) {
		super(basic3D.getX(), basic3D.getY(), basic3D.getZ());
	}

	public Vector3D(Vector3D vector3D) {
		super(vector3D.getX(), vector3D.getY(), vector3D.getZ());
	}

	public static Vector3D sumVectors(Vector3D ... vectors) {
		Vector3D resultVector = new Vector3D();
		
		for(Vector3D vector : vectors) {
			resultVector = new Vector3D(resultVector.plus(vector));
		}
		
		return resultVector;
	}
	
	public static Vector3D toPoint(Point3D point) {
		return new Vector3D(point.getX(), point.getY(), point.getZ());
	}
	
	public static Vector3D betweenPoints(Point3D fromPoint, Point3D toPoint) {
		return new Vector3D(toPoint.getX() - fromPoint.getX(),
				toPoint.getY() - fromPoint.getY(),
				toPoint.getZ() - fromPoint.getZ());
	}

	public double getScalar() {
		return Math.sqrt( Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2) );
	}
	
	public Vector3D getUnitVector() {
		Vector3D resultVector = this;
		return new Vector3D(resultVector.div(getScalar()));
	}
	
	public Vector3D rotate(double angle) {
		return new Vector3D( (Math.cos(angle) * getX()) + (-Math.sin(angle) * getY()) + (0 * getZ()),
				(Math.sin(angle) * getX()) + ( Math.cos(angle) * getY()) + (0 * getZ()),
				(              0 * getX()) + (               0 * getY()) + (1 * getZ()) );
	}

}
