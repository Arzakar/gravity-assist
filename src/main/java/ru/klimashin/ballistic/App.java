package ru.klimashin.ballistic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.klimashin.ballistic.controller.SolutionSearchAlgorithmController;
import ru.klimashin.ballistic.model.calculator.ModelParameters;
import ru.klimashin.ballistic.model.calculator.TrajectoryCalculator;
import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.entity.Engine;
import ru.klimashin.ballistic.model.entity.Spacecraft;
import ru.klimashin.ballistic.model.util.math.Point3D;
import ru.klimashin.ballistic.model.util.math.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	
    	try {
    		AnchorPane root = (AnchorPane) FXMLLoader.load(SolutionSearchAlgorithmController.class.getResource("SolutionSearchAlgorithm.fxml"));
    		Scene scene = new Scene(root,600,600);

    		stage.setScene(scene);
    		stage.show();
    			
    			
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

    }

    private void draw(GraphicsContext gc) {
		CelestialBody earth = CelestialBody.EARTH;
		CelestialBody solar = CelestialBody.SOLAR;
		
		earth.setPosition(new Point3D(earth.getOrbitRadius(), 0, 0));
		earth.setSpeed(new Vector3D(0, 29784.48, 0));
		
		Engine engine = new Engine("Р­Р Р”", 0.040);
		Spacecraft spacecraft = new Spacecraft("РљРђ", 150, engine, 2,
				new Point3D(earth.getPosition().plus(new Point3D(earth.getGravitationalRadius(), 0, 0))), earth.getSpeed(), null);
		
		ModelParameters mp = new ModelParameters(0, 60 * 86400, 130 * 86400, 100, Math.toRadians(-30), Math.toRadians(120));
		
		TrajectoryCalculator trj = new TrajectoryCalculator(mp, earth, solar, spacecraft);
		trj.startCalculate();
		
		List<Double> distance = new ArrayList<>();
		List<Point3D> earthPos = trj.getEarthPositions();
		List<Point3D> scPos = trj.getSpacecraftPositions();
		
		distance.addAll(trj.getDistanceBetweenEarthAndSpacecraft());
		Collections.sort(distance);
		System.out.println(distance.get(0));
		System.out.println(distance.size());
		
		
		double centerX = gc.getCanvas().getWidth() / 2;
		double centerY = gc.getCanvas().getHeight() / 2;
		
		double mk = (gc.getCanvas().getWidth() / 2) / (earth.getOrbitRadius() * 1.2);
		
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.GREEN);
		gc.setLineWidth(1);
		int i = 1;
		for(Double d : distance) {
			gc.fillOval((i * gc.getCanvas().getWidth()) / distance.size(), gc.getCanvas().getHeight() - d * mk * 20, 0.5, 0.5);
			i += 1;
		}
		
		/*
		earthPos.forEach(p -> {
			gc.fillOval(centerX + p.getX() * mk, centerY - p.getY() * mk, 0.5, 0.5);
		});
		*/
		gc.setFill(Color.RED);
		gc.setStroke(Color.RED);
		gc.setLineWidth(1);
		/*
		scPos.forEach(p -> {
			gc.fillOval(centerX + p.getX() * mk, centerY - p.getY() * mk, 1, 1);
		});*/
	}


    
    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}