package com.klimashin.model.trajectory.earthtoearth;

import lombok.Getter;
import lombok.Setter;
import com.klimashin.model.entity.Spacecraft;
import com.klimashin.model.entity.celestial.Earth;
import com.klimashin.model.entity.celestial.Solar;
import com.klimashin.model.util.AngleAndTimeParameters;
import com.klimashin.model.util.math.Point3D;
import com.klimashin.model.util.math.Vector3D;
import com.klimashin.model.util.physic.BasicFormules;
import com.klimashin.model.util.physic.SpaceMechanic;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EarthToEarthTrajectory {

    private final Solar solar = new Solar();
    private Earth earth = new Earth();

    private Spacecraft spacecraft;
    private AngleAndTimeParameters angleAndTimeParameters;

    private List<Point3D> earthPosition = new ArrayList<>();
    private List<Vector3D> earthSpeed = new ArrayList<>();
    private List<Vector3D> earthAcceleration = new ArrayList<>();

    private List<Point3D> spacecraftPosition = new ArrayList<>();
    private List<Vector3D> spacecraftSpeed = new ArrayList<>();
    private List<Vector3D> spacecraftAcceleration = new ArrayList<>();

    private List<Double> distanceToEarth = new ArrayList<>();

    public EarthToEarthTrajectory(Spacecraft spacecraft, AngleAndTimeParameters angleAndTimeParameters) {
        this.spacecraft = spacecraft;
        this.angleAndTimeParameters = angleAndTimeParameters;
    }

    public void calculateFullTrajectory(Point3D startEarthPosition, Point3D startSpacecraftPosition,
                                        Vector3D startSpacecraftSpeed) {
        earth.setAcceleration(new Vector3D());
        earth.setSpeed(new Vector3D());
        earth.setPosition(startEarthPosition);

        spacecraft.setAcceleration(new Vector3D());
        spacecraft.setSpeed(startSpacecraftSpeed);
        spacecraft.setPosition(startSpacecraftPosition);

        double distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());

        writeInLists(earth, spacecraft, distance);

        int t = angleAndTimeParameters.getStartTime();
        double thrustAngle;
        Vector3D unitVectorToSolar;
        Vector3D gravitationForce;
        Vector3D unitVectorFromThrust;
        Vector3D thrustForce;
        Vector3D sumForce;
        while(t <= angleAndTimeParameters.getFirstPartDuration() + angleAndTimeParameters.getSecondPartDuration()) {
            unitVectorToSolar = Vector3D.betweenPoints(spacecraft.getPosition(), solar.getPosition());
            gravitationForce = new Vector3D(unitVectorToSolar.mult(BasicFormules.gravitationForce(solar, spacecraft)));

            if(t <= angleAndTimeParameters.getFirstPartDuration()) {
                thrustAngle = angleAndTimeParameters.getFirstTrustAngle();
            } else {
                thrustAngle = angleAndTimeParameters.getSecondThrustAngle();
            }

            unitVectorFromThrust = new Vector3D(unitVectorToSolar.rotate(thrustAngle));
            thrustForce = new Vector3D(unitVectorFromThrust.mult(spacecraft.getThrust()));

            sumForce = BasicFormules.sumForce(gravitationForce, thrustForce);

            t += angleAndTimeParameters.getDeltaTime();

            earth.setPosition(SpaceMechanic.positionFromTime(earth, t));

            spacecraft.setAcceleration(BasicFormules.acceleration(sumForce, spacecraft.getMass()));
            spacecraft.setSpeed(BasicFormules.changeSpeed(spacecraft.getSpeed(), spacecraft.getAcceleration(),
                    angleAndTimeParameters.getDeltaTime()));
            spacecraft.setPosition(BasicFormules.changePosition(spacecraft.getPosition(), spacecraft.getSpeed(),
                    angleAndTimeParameters.getDeltaTime()));

            distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());

            writeInLists(earth, spacecraft, distance);
        }

    }

    public void calculateMainPartTrajectory(Point3D startEarthPosition, Point3D startSpacecraftPosition,
                                        Vector3D startSpacecraftSpeed) {
        earth.setAcceleration(new Vector3D());
        earth.setSpeed(new Vector3D());
        earth.setPosition(startEarthPosition);

        spacecraft.setAcceleration(new Vector3D());
        spacecraft.setSpeed(startSpacecraftSpeed);
        spacecraft.setPosition(startSpacecraftPosition);

        double distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());

        writeInLists(earth, spacecraft, distance);

        int t = angleAndTimeParameters.getStartTime();
        double thrustAngle;
        Vector3D unitVectorToSolar;
        Vector3D gravitationForce;
        Vector3D unitVectorFromThrust;
        Vector3D thrustForce;
        Vector3D sumForce;
        while(t <= angleAndTimeParameters.getFirstPartDuration() + angleAndTimeParameters.getSecondPartDuration()) {
            unitVectorToSolar = Vector3D.betweenPoints(spacecraft.getPosition(), solar.getPosition());
            gravitationForce = new Vector3D(unitVectorToSolar.mult(BasicFormules.gravitationForce(solar, spacecraft)));

            if(t <= angleAndTimeParameters.getFirstPartDuration()) {
                thrustAngle = angleAndTimeParameters.getFirstTrustAngle();
            } else {
                thrustAngle = angleAndTimeParameters.getSecondThrustAngle();
            }

            unitVectorFromThrust = new Vector3D(unitVectorToSolar.rotate(thrustAngle));
            thrustForce = new Vector3D(unitVectorFromThrust.mult(spacecraft.getThrust()));

            sumForce = BasicFormules.sumForce(gravitationForce, thrustForce);

            t += angleAndTimeParameters.getDeltaTime();

            earth.setPosition(SpaceMechanic.positionFromTime(earth, t));

            spacecraft.setAcceleration(BasicFormules.acceleration(sumForce, spacecraft.getMass()));
            spacecraft.setSpeed(BasicFormules.changeSpeed(spacecraft.getSpeed(), spacecraft.getAcceleration(),
                    angleAndTimeParameters.getDeltaTime()));
            spacecraft.setPosition(BasicFormules.changePosition(spacecraft.getPosition(), spacecraft.getSpeed(),
                    angleAndTimeParameters.getDeltaTime()));

            distance = Point3D.distanceBetweenPoints(earth.getPosition(), spacecraft.getPosition());

            writeInLists(earth, spacecraft, distance);

            if(distance <= earth.getGravitationalRadius()) {
                break;
            }
        }

    }

    private void writeInLists(Earth earth, Spacecraft spacecraft, double distance) {
        earthPosition.add(earth.getPosition());
        earthSpeed.add(earth.getSpeed());
        earthAcceleration.add(earth.getAcceleration());

        spacecraftPosition.add(spacecraft.getPosition());
        spacecraftSpeed.add(spacecraft.getSpeed());
        spacecraftAcceleration.add(spacecraft.getAcceleration());

        distanceToEarth.add(distance);
    }

}