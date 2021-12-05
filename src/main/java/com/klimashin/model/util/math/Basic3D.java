package com.klimashin.model.util.math;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basic3D {

	@NonNull
	private double x;

	@NonNull
	private double y;

	@NonNull
	private double z;

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
