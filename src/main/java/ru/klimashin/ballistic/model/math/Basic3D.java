package ru.klimashin.ballistic.model.math;

public class Basic3D {

	private double x;
	private double y;
	private double z;
	
	
	
	public Basic3D() {	
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Basic3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	

	
	public Basic3D plus(Basic3D basic3D) {
		return new Basic3D(this.x + basic3D.getX(), this.y + basic3D.getY(), this.z + basic3D.getZ());
	}
	
	public Basic3D minus(Basic3D basic3D) {
		return new Basic3D(this.x - basic3D.getX(), this.y - basic3D.getY(), this.z - basic3D.getZ());
	}

	public Basic3D mult(double k) {
		return new Basic3D(this.x * k, this.y * k, this.z * k);
	}
	
	public Basic3D div(double k) {
		return new Basic3D(this.x / k, this.y / k, this.z / k);
	}
	
	
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	
	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	
	@Override
	public String toString() {
		return "Basic3D [ x = " + x + ", y = " + y + ", z = " + z + "]";
	}
}
