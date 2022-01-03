package ru.klimashin.ballistic.model.util.math;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Basic3D {

	private double x;
	private double y;
	private double z;

	public Basic3D() {	
		this.x = 0;
		this.y = 0;
		this.z = 0;
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

	@Override
	public String toString() {
		return "Basic3D [ x = " + x + ", y = " + y + ", z = " + z + "]";
	}
}
