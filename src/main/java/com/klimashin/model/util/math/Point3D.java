package com.klimashin.model.util.math;

public class Point3D extends Basic3D {

	public Point3D() {
		super(0, 0, 0);
	}

	public Point3D(double x, double y, double z) {
		super(x, y, z);
	}

	public Point3D(Basic3D basic3D) {
		super(basic3D.getX(), basic3D.getY(), basic3D.getZ());
	}

	public double distanceToPoint(Point3D point) {
		return Math.sqrt( Math.pow(getX() - point.getX(), 2) 
				+ Math.pow(getY() - point.getY(), 2) 
				+ Math.pow(getZ() - point.getZ(), 2) );
	}
	
	public static double distanceBetweenPoints(Point3D firstPoint, Point3D secondPoint) {
		return Math.sqrt( Math.pow(firstPoint.getX() - secondPoint.getX(), 2) 
				+ Math.pow(firstPoint.getY() - secondPoint.getY(), 2) 
				+ Math.pow(firstPoint.getZ() - secondPoint.getZ(), 2) );
	}
	
}
