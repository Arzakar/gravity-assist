package ru.klimashin.ballistic.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import ru.klimashin.ballistic.model.ModelParameters;
import ru.klimashin.ballistic.model.Trajectory;
import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.entity.Engine;
import ru.klimashin.ballistic.model.entity.Spacecraft;
import ru.klimashin.ballistic.model.math.Point3D;
import ru.klimashin.ballistic.model.math.Vector3D;

public class MainController {

	@FXML
	Button startSearchButton;
	
	@FXML
	Label infoLabel;
	
	@FXML
	public void startSearch() throws FileNotFoundException {
		
		int simOpeartion = (int) (((180 - 10) / 10 + 1) * ((180 - 10) / 10 + 1) * ((180 + 180) / 30 + 1) * ((180 + 180) / 30 + 1));
		
		CelestialBody earth = CelestialBody.EARTH;
		CelestialBody solar = CelestialBody.SOLAR;
		
		Engine engine = new Engine("PP”", 0.190);
		
		Spacecraft spacecraft = new Spacecraft("AID", 350, engine, 1, null, null, null);
		
		ModelParameters mp = new ModelParameters(0, 0, 0, 100, 0, 0);
		
		new Thread(() -> {
			double percentFinal = 0;
			List<StringBuilder> results = new ArrayList<>();

			for(int fpd = 10; fpd <= 180; fpd = fpd + 10) {
				mp.setFirstPartDuration(fpd * 86400);

				for(int spd = 10; spd <= 180; spd = spd +10) {
					mp.setSecondPartDuration(spd * 86400);

					for(int fa = -180; fa <= 180; fa = fa + 30) {
						mp.setFirstThrustAngle(Math.toRadians(fa));

						for(int sa = -180; sa <= 180; sa = sa + 30) {
							mp.setSecondThrustAngle(Math.toRadians(sa));

							earth.setPosition(new Point3D(earth.getOrbitRaius(), 0, 0));
							earth.setSpeed(new Vector3D(0, 29784.48, 0));

							spacecraft.setPosition(new Point3D(earth.getPosition().minus(new Point3D(earth.getGravitationalRadius(), 0, 0))));
							spacecraft.setSpeed(new Vector3D(0, 29684.48, 0));
							spacecraft.setAcceleration(null);

							Trajectory trj = new Trajectory(mp, earth, solar, spacecraft);
							trj.startCalculate();

							List<Double> distance = new ArrayList<>();
							//List<Point3D> earthPos = trj.getEarthPositions();
							//List<Point3D> scPos = trj.getSpacecraftPositions();

							distance.addAll(trj.getDistanceBetweenEarthAndSpacecraft());
							Collections.sort(distance);

							if(distance.get(0) < 60_000_000) {

								StringBuilder result = new StringBuilder(String.format("%3d" , fpd) + " "
										+ String.format("%3d", spd) + " "
										+ String.format("%4d", fa) + " "
										+ String.format("%4d", sa) + " "
										+ String.format("%13.2f", distance.get(0)) + "\n");

								results.add(result);
								/*
								try(FileOutputStream fos = new FileOutputStream("resultSet.txt", true)) {
									fos.write(results.toString().getBytes());
								} catch (IOException e) {
									e.printStackTrace();
								}
								*/
							}

							percentFinal++;

							final double constPF = percentFinal;
							final int constFPD = fpd;
							final int constSPD = spd;
							final int constFA = fa;
							final int constSA = sa;

							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									int pc = (int) ((constPF / simOpeartion) * 100);
									infoLabel.setText("Выполнено " + constPF + " операций из " + simOpeartion + ". "
											+ "Поиск завершён на " + pc + " \n"
											+ "Сейчас рассматривается " + constFPD + " " + constSPD + " " + constFA + " " + constSA);
								}
							});

						}
					}
				}
			}

			//Вставить сюда запись в файл
			String fileName = "M" + String.format("%.0f", spacecraft.getMass())
					+ " - " + "T" + String.format("%.3f", spacecraft.getEngine().getThrust() * spacecraft.getEngineCount())
					+ ".txt";
			try(FileOutputStream fos = new FileOutputStream(fileName, true)) {

				for(StringBuilder sb : results) {
					fos.write(sb.toString().getBytes());
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}).start();
		
	}
	
}
