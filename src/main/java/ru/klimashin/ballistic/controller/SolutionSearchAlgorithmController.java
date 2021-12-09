package ru.klimashin.ballistic.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.klimashin.model.entity.celestial.Earth;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;
import ru.klimashin.ballistic.model.calculator.earthToEarth.ModelParameters;
import ru.klimashin.ballistic.model.calculator.earthToEarth.EarthToEarthTrajectory;
import ru.klimashin.ballistic.model.calculator.earthToEarth.Monitor;
import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.entity.Engine;
import ru.klimashin.ballistic.model.entity.Spacecraft;
import ru.klimashin.ballistic.model.util.GeneralFormulas;
import ru.klimashin.ballistic.model.util.math.Point3D;
import ru.klimashin.ballistic.model.util.math.Vector3D;

import static java.lang.Math.toRadians;
import static ru.klimashin.ballistic.model.util.GeneralFormulas.daysToSeconds;

@Log
public class SolutionSearchAlgorithmController {

    double mass;

    double thrustFrom;
    double thrustTo;
    double thrustStep;

    double spacecraftDegreePosition;
    double spacecraftStartSpeed;

    int firstPartDurationFrom;
    int firstPartDurationTo;
    int firstPartDurationStep;

    int firstPartThrustAngleFrom;
    int firstPartThrustAngleTo;
    int firstPartThrustAngleStep;

    int secondPartDurationFrom;
    int secondPartDurationTo;
    int secondPartDurationStep;

    int secondPartThrustAngleFrom;
    int secondPartThrustAngleTo;
    int secondPartThrustAngleStep;

    double minDistance;

    Task mainProcess;
    boolean mainProcessWaitFlag = false;

    Thread monitoringProcess;
    Monitor monitor = new Monitor();

    @FXML
    public void startSearch() {
        if (mainProcess == null) {
            if (!initDataIsTrue()) {
                return;
            }

            int sumOperations = 1;
            sumOperations *= (firstPartDurationTo - firstPartDurationFrom) / firstPartDurationStep + 1;
            sumOperations *= (secondPartDurationTo - secondPartDurationFrom) / secondPartDurationStep + 1;
            sumOperations *= (firstPartThrustAngleTo - firstPartThrustAngleFrom) / firstPartThrustAngleStep + 1;
            sumOperations *= (secondPartThrustAngleTo - secondPartThrustAngleFrom) / secondPartThrustAngleStep + 1;

            monitor.setSumOperations(sumOperations);

            runMainProcess();
            runMonitoringProcess();

            buttonStartCalculate.setText("Приостановить");
        } else {
            turnMainProcess();
        }
    }

    private void runMainProcess() {
        mainProcess = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                int completedIterations = 0;

                CelestialBody earth = CelestialBody.EARTH;
                CelestialBody solar = CelestialBody.SOLAR;

                Engine engine = new Engine("Experimental Engine”", 0.190);
                Spacecraft spacecraft = new Spacecraft("Experimental Spacecraft", mass, engine, 1, null, null, null);

                ModelParameters mp = new ModelParameters(0, 100, 0, 0, 0, 0);

                List<StringBuilder> results = new ArrayList<>();

                for (int firstPartDuration = firstPartDurationFrom; firstPartDuration <= firstPartDurationTo; firstPartDuration += firstPartDurationStep) {
                    mp.setFirstPartDuration(daysToSeconds(firstPartDuration));

                    for (int secondPartDuration = secondPartDurationFrom; secondPartDuration <= secondPartDurationTo; secondPartDuration += secondPartDurationStep) {
                        mp.setSecondPartDuration(daysToSeconds(secondPartDuration));

                        for (int firstPartAngle = firstPartThrustAngleFrom; firstPartAngle <= firstPartThrustAngleTo; firstPartAngle += firstPartThrustAngleStep) {
                            mp.setFirstThrustAngle(toRadians(firstPartAngle));

                            for (int secondPartAngle = secondPartThrustAngleFrom; secondPartAngle <= secondPartThrustAngleTo; secondPartAngle += secondPartThrustAngleStep) {
                                synchronized (mainProcess) {
                                    if (mainProcess != null & mainProcessWaitFlag) {
                                        mainProcess.wait();
                                    }
                                }
                                mp.setSecondThrustAngle(toRadians(secondPartAngle));

                                earth.setPosition(new Point3D(earth.getOrbitRadius(), 0, 0));
                                earth.setSpeed(new Vector3D(0, 29784.48, 0));

                                spacecraft.setPosition(new Point3D(earth.getPosition().minus(new Point3D(earth.getGravitationalRadius(), 0, 0))));
                                spacecraft.setSpeed(new Vector3D(0, 29684.48, 0));
                                spacecraft.setAcceleration(null);

                                EarthToEarthTrajectory trj = new EarthToEarthTrajectory(mp, earth, solar, spacecraft);
                                trj.startCalculate();

                                List<Double> distance = new ArrayList<>();

                                distance.addAll(trj.getDistancesToEarth());
                                Collections.sort(distance);

                                if (distance.get(0) < 60_000_000) {
                                    StringBuilder result = new StringBuilder(String.format("%3d", firstPartDuration) + " " + String.format("%3d", secondPartDuration) + " " + String.format("%4d", firstPartAngle) + " " + String.format("%4d", secondPartAngle) + " " + String.format("%13.2f", distance.get(0)) + "\n");

                                    results.add(result);
                                    textAreaResultLog.appendText(mp + " " + distance.get(0).toString() + "\n");
                                }

                                completedIterations++;
                                synchronized (monitor) {
                                    monitor.setCompletedIterations(completedIterations);
                                    monitor.setFirstPartDuration(firstPartDuration);
                                    monitor.setSecondPartDuration(secondPartDuration);
                                    monitor.setFirstPartAngle(firstPartAngle);
                                    monitor.setSecondPartAngle(secondPartAngle);
                                }
                            }
                        }
                    }
                }

                return null;
            }
        };

        new Thread(mainProcess).start();
    }

    private void turnMainProcess() {
        synchronized (mainProcess) {
            if (mainProcess != null && !mainProcessWaitFlag) {
                mainProcessWaitFlag = true;
                buttonStartCalculate.setText("Возобновить");
                log.info("Главнвый процесс приостановлен");
            } else {
                mainProcessWaitFlag = false;
                mainProcess.notify();
                buttonStartCalculate.setText("Приостановить");
                log.info("Гланвый процесс возобновлён");
            }
        }
    }

    private void runMonitoringProcess() {
        monitoringProcess = new Thread(() -> {
            while (true) {
                int percent = (monitor.getCompletedIterations() / monitor.getSumOperations()) * 100;
                Platform.runLater(() -> {
                    synchronized (monitor) {
                        labelInfo.setText("Выполнено " + monitor.getCompletedIterations()
                                + " операций из " + monitor.getSumOperations() + ". "
                                + "Поиск завершён на " + percent + " \n");
//                                +"Сейчас рассматривается "
//                                + monitor.getFirstPartDuration() + " "
//                                + monitor.getSecondPartDuration() + " "
//                                + monitor.getFirstPartAngle() + " "
//                                + monitor.getSecondPartAngle());
                    }
                });
                try {
                    monitoringProcess.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        monitoringProcess.start();
    }

    private boolean initDataIsTrue() {

        List<String> errorList = new ArrayList<>();

        try {
            mass = Double.valueOf(textFieldMass.getText());

            if (mass <= 0) {
                errorList.add("Масса КА: масса должна быть больше 0");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Масса КА");
        }

        try {
            thrustFrom = Double.valueOf(textFieldThrustFrom.getText());
            thrustTo = Double.valueOf(textFieldThrustTo.getText());
            thrustStep = Double.valueOf(textFieldThrustStep.getText());

            if (thrustFrom > thrustTo) {
                errorList.add("Диапазон тяги двигателя: значение в поле <от> должно быть меньше <до>");
            }

            if (thrustFrom < thrustTo && thrustTo - thrustFrom < thrustStep) {
                errorList.add("Диапазон тяги двигателя: значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон тяги двигателя");
        }

        try {
            spacecraftDegreePosition = Double.valueOf(textFieldSpacecraftPosition.getText());

            if (spacecraftDegreePosition < 0 | spacecraftDegreePosition > 359) {
                errorList.add("Позиция старта КА: значение угла должно быть в диапазоне 0...359");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Позиция старта КА");
        }

        try {
            spacecraftStartSpeed = Double.valueOf(textFieldSpacecraftSpeed.getText());
        } catch (NumberFormatException ex) {
            errorList.add("Начальная скорость КА");
        }

        try {
            firstPartDurationFrom = Integer.valueOf(textFieldFirstPartTimeFrom.getText());
            firstPartDurationTo = Integer.valueOf(textFieldFirstPartTimeTo.getText());
            firstPartDurationStep = Integer.valueOf(textFieldFirstPartTimeStep.getText());

            if (firstPartDurationFrom > firstPartDurationTo) {
                errorList.add("Диапазон длительности (Первый участок): значение в поле <от> должно быть меньше <до>");
            }

            if (firstPartDurationFrom < firstPartDurationTo && firstPartDurationTo - firstPartDurationFrom < firstPartDurationStep) {
                errorList.add("Диапазон длительности (Первый участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон длительности (Первый участок)");
        }

        try {
            firstPartThrustAngleFrom = Integer.valueOf(textFieldFirstPartAngleFrom.getText());
            firstPartThrustAngleTo = Integer.valueOf(textFieldFirstPartAngleTo.getText());
            firstPartThrustAngleStep = Integer.valueOf(textFieldFirstPartAngleStep.getText());

            if (firstPartThrustAngleFrom > firstPartThrustAngleTo) {
                errorList.add("Диапазон угла тяги (Первый участок): значение в поле <от> должно быть меньше <до>");
            }

            if (firstPartThrustAngleFrom < firstPartThrustAngleTo && firstPartThrustAngleTo - firstPartThrustAngleFrom < firstPartThrustAngleStep) {
                errorList.add("Диапазон угла тяги (Первый участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон угла тяги (Первый участок)");
        }

        try {
            secondPartDurationFrom = Integer.valueOf(textFieldSecondPartTimeFrom.getText());
            secondPartDurationTo = Integer.valueOf(textFieldSecondPartTimeTo.getText());
            secondPartDurationStep = Integer.valueOf(textFieldSecondPartTimeStep.getText());

            if (secondPartDurationFrom > secondPartDurationTo) {
                errorList.add("Диапазон длительности (Второй участок): значение в поле <от> должно быть меньше <до>");
            }

            if (secondPartDurationFrom < secondPartDurationTo && secondPartDurationTo - secondPartDurationFrom < secondPartDurationStep) {
                errorList.add("Диапазон длительности (Второй участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон длительности (Второй участок)");
        }

        try {
            secondPartThrustAngleFrom = Integer.valueOf(textFieldSecondPartAngleFrom.getText());
            secondPartThrustAngleTo = Integer.valueOf(textFieldSecondPartAngleTo.getText());
            secondPartThrustAngleStep = Integer.valueOf(textFieldSecondPartAngleStep.getText());

            if (secondPartThrustAngleFrom > secondPartThrustAngleTo) {
                errorList.add("Диапазон угла тяги (Второй участок): значение в поле <от> должно быть меньше <до>");
            }

            if (secondPartThrustAngleFrom < secondPartThrustAngleTo && secondPartThrustAngleTo - secondPartThrustAngleFrom < secondPartThrustAngleStep) {
                errorList.add("Диапазон угла тяги (Второй участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон угла тяги (Второй участок)");
        }

        try {
            minDistance = Double.valueOf(textFieldMinDistance.getText());

            Earth earth = new Earth();
            if (minDistance <= 0 | minDistance <= earth.getRadius()) {
                errorList.add("Мин. сближение с Землёй: должно быть больше радиуса Земли");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Мин. сближение с Землёй");
        }

        String errorMessage = "Следующие поля заполненены неправильно: \n";
        for (String s : errorList) {
            errorMessage += "   - " + s + "\n";
        }

        if (!errorList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка ввода данных");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }

    @FXML
    TextArea textAreaResultLog;

    @FXML
    Label labelInfo;

    @FXML
    Button buttonStartCalculate;

    @FXML
    TextField textFieldMass;

    @FXML
    TextField textFieldThrustFrom;
    @FXML
    TextField textFieldThrustTo;
    @FXML
    TextField textFieldThrustStep;

    @FXML
    TextField textFieldSpacecraftPosition;
    @FXML
    TextField textFieldSpacecraftSpeed;

    @FXML
    TextField textFieldFirstPartTimeFrom;
    @FXML
    TextField textFieldFirstPartTimeTo;
    @FXML
    TextField textFieldFirstPartTimeStep;

    @FXML
    TextField textFieldFirstPartAngleFrom;
    @FXML
    TextField textFieldFirstPartAngleTo;
    @FXML
    TextField textFieldFirstPartAngleStep;

    @FXML
    TextField textFieldSecondPartTimeFrom;
    @FXML
    TextField textFieldSecondPartTimeTo;
    @FXML
    TextField textFieldSecondPartTimeStep;

    @FXML
    TextField textFieldSecondPartAngleFrom;
    @FXML
    TextField textFieldSecondPartAngleTo;
    @FXML
    TextField textFieldSecondPartAngleStep;

    @FXML
    TextField textFieldMinDistance;

}
