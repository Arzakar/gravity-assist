package ru.klimashin.ballistic.model.calculator.earthToEarth;

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
public class EarthToEarthTrajectory {

	private List<Point3D> earthPositions = new ArrayList<>();
	private List<Point3D> spacecraftPositions = new ArrayList<>();
	private List<Double> distancesToEarth = new ArrayList<>();

	private CelestialBody earth;
	private CelestialBody solar;
	
	private Spacecraft spacecraft;
	
	private ModelParameters mp;

	public EarthToEarthTrajectory(ModelParameters mp, CelestialBody earth, CelestialBody solar, Spacecraft spacecraft) {
		this.mp = mp;
		this.earth = earth;
		this.solar = solar;
		this.spacecraft = spacecraft;
	}
	
	public void startCalculate() {
		double distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());
		writeInLists(earth, spacecraft, distance);
		
		Vector3D unitVectorToSolar;
		Vector3D gravitationForce;
		Vector3D unitVectorFromThrust;
		Vector3D thrustForce;
		Vector3D sumForce;
		
		int t = mp.getStartTime();
		double angle;

		while (t <= mp.getFirstPartDuration() + mp.getSecondPartDuration()) {
			unitVectorToSolar = Vector3D.betweenPoints(spacecraft.getPosition(), solar.getPosition()).getUnitVector();
			gravitationForce = new Vector3D(unitVectorToSolar.mult(BasicFormulas.gravitationForce(solar, spacecraft)));

			if(t <= mp.getFirstPartDuration()) {
				angle = mp.getFirstThrustAngle();
			} else {
				angle = mp.getSecondThrustAngle();
			}
			
			unitVectorFromThrust = new Vector3D(unitVectorToSolar).rotate(angle);
			thrustForce = new Vector3D(unitVectorFromThrust.mult(spacecraft.getEngine().getThrust() * spacecraft.getEngineCount()));

			sumForce = Vector3D.sumVectors(gravitationForce, thrustForce);

			spacecraft.setAcceleration(BasicFormulas.acceleration(sumForce, spacecraft.getMass()));
			spacecraft.setSpeed(BasicFormulas.changeSpeed(spacecraft.getSpeed(), spacecraft.getAcceleration(), mp.getDeltaTime()));
			spacecraft.setPosition(BasicFormulas.changePosition(spacecraft.getPosition(), spacecraft.getSpeed(), mp.getDeltaTime()));
			
			t = t + mp.getDeltaTime();
			
			earth.setPosition(SpaceMechanicFormulas.positionFromTime(earth, t));
			distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());

			writeInLists(earth, spacecraft, distance);

		}
		
	}

	private void writeInLists(CelestialBody earth, Spacecraft spacecraft, double distance) {
		earthPositions.add(earth.getPosition());
		//earthSpeed.add(earth.getSpeed());
		//earthAcceleration.add(earth.getAcceleration());

		spacecraftPositions.add(spacecraft.getPosition());
		//spacecraftSpeed.add(spacecraft.getSpeed());
		//spacecraftAcceleration.add(spacecraft.getAcceleration());

		distancesToEarth.add(distance);
	}

}

