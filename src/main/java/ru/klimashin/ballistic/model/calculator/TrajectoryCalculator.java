package ru.klimashin.ballistic.model.calculator;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.java.Log;
import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.entity.Spacecraft;
import ru.klimashin.ballistic.model.util.math.Point3D;
import ru.klimashin.ballistic.model.util.math.Vector3D;
import ru.klimashin.ballistic.model.util.physic.BasicFormulas;
import ru.klimashin.ballistic.model.util.physic.SpaceMechanicFormulas;

@Log
@Data
public class TrajectoryCalculator {

	private List<Point3D> earthPositions = new ArrayList<>();
	private List<Point3D> spacecraftPositions = new ArrayList<>();
	private List<Double> distanceBetweenEarthAndSpacecraft = new ArrayList<>();
	
	private CelestialBody earth;
	private CelestialBody solar;
	
	private Spacecraft spacecraft;
	
	private ModelParameters mp;
	
	
	public TrajectoryCalculator(ModelParameters mp, CelestialBody earth, CelestialBody solar, Spacecraft spacecraft) {
		this.mp = mp;
		this.earth = earth;
		this.solar = solar;
		this.spacecraft = spacecraft;
	}
	
	public void startCalculate() {

		double distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());
		
		earthPositions.add(earth.getPosition());
		spacecraftPositions.add(spacecraft.getPosition());
		distanceBetweenEarthAndSpacecraft.add(distance);
		
		Vector3D unitToSolar;
		Vector3D gravForce;
		
		Vector3D unitFromThrust;
		Vector3D thrustForce;
		
		Vector3D sumForce;
		
		int t = mp.getStartTime();

		while (t <= mp.getFirstPartDuration() + mp.getSecondPartDuration()) {
			unitToSolar = Vector3D.betweenPoints(spacecraft.getPosition(), solar.getPosition()).getUnitVector();
			gravForce = new Vector3D(unitToSolar.mult(BasicFormulas.gravitationForce(solar, spacecraft)));

			double angle;
			
			if(t <= mp.getFirstPartDuration()) {
				angle = mp.getFirstThrustAngle();
			} else {
				angle = mp.getSecondThrustAngle();
			}
			
			unitFromThrust = new Vector3D(unitToSolar).rotate(angle);
			thrustForce = new Vector3D(unitFromThrust.mult(spacecraft.getEngine().getThrust() * spacecraft.getEngineCount()));

			List<Vector3D> forces = new ArrayList<>();
			forces.add(gravForce);
			forces.add(thrustForce);
			
			sumForce = Vector3D.sumVectors(forces);

			spacecraft.setAcceleration(BasicFormulas.acceleration(sumForce, spacecraft.getMass()));
			spacecraft.setSpeed(BasicFormulas.changeSpeed(spacecraft.getSpeed(), spacecraft.getAcceleration(), mp.getDeltaTime()));
			spacecraft.setPosition(BasicFormulas.changePosition(spacecraft.getPosition(), spacecraft.getSpeed(), mp.getDeltaTime()));
			
			t = t + mp.getDeltaTime();
			
			earth.setPosition(SpaceMechanicFormulas.positionFromTime(earth, t));
			distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());
			
			earthPositions.add(earth.getPosition());
			spacecraftPositions.add(spacecraft.getPosition());
			distanceBetweenEarthAndSpacecraft.add(distance);
			
		}
		
	}

}

