package ru.klimashin.ballistic.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;
import ru.klimashin.ballistic.model.calculator.earthToEarth.ModelParameters;
import ru.klimashin.ballistic.model.calculator.earthToEarth.EarthToEarthTrajectory;
import ru.klimashin.ballistic.model.calculator.earthToEarth.Monitor;
import ru.klimashin.ballistic.model.entity.CelestialBody;
import ru.klimashin.ballistic.model.entity.Engine;
import ru.klimashin.ballistic.model.entity.Spacecraft;
import ru.klimashin.ballistic.model.util.math.Point3D;
import ru.klimashin.ballistic.model.util.math.Vector3D;

import static java.lang.Math.toRadians;
import static ru.klimashin.ballistic.model.util.GeneralFormulas.*;

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

    HashSet<String> completeParameters = new HashSet<>();

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
            sumOperations *= (thrustTo - thrustFrom) / thrustStep + 1;

            monitor.setSumOperations(sumOperations);
            monitor.setStartTime(LocalTime.now());
            monitor.setCurrentTime(LocalTime.now());
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
                earth.setPosition(new Point3D(earth.getOrbitRadius(), 0, 0));

                CelestialBody solar = CelestialBody.SOLAR;

                Spacecraft spacecraft = new Spacecraft("Experimental Spacecraft", mass, null, 1, null, null, null);
                spacecraft.setSpeed(new Vector3D(0, spacecraftStartSpeed, 0));
                spacecraft.setPosition(new Point3D(earth.getPosition().minus(new Point3D(earth.getGravitationalRadius(), 0, 0))));
                textAreaResultLog.appendText(headResultLog(spacecraft.getSpeed(), spacecraft.getPosition(), spacecraft.getMass()));

                ModelParameters mp = new ModelParameters(0, 100, 0, 0, 0, 0);

                for (double currentThrust = thrustFrom; currentThrust <= thrustTo; currentThrust += thrustStep) {
                    Engine engine = new Engine("Experimental Engine”", currentThrust);
                    spacecraft.setEngine(engine);
                    textAreaResultLog.appendText(thrustToResultLog(currentThrust));

                    for (int firstPartDuration = firstPartDurationFrom; firstPartDuration <= firstPartDurationTo; firstPartDuration += firstPartDurationStep) {
                        mp.setFirstPartDuration(daysToSeconds(firstPartDuration));

                        for (int secondPartDuration = secondPartDurationFrom; secondPartDuration <= secondPartDurationTo; secondPartDuration += secondPartDurationStep) {
                            mp.setSecondPartDuration(daysToSeconds(secondPartDuration));

                            for (int firstPartAngle = firstPartThrustAngleFrom; firstPartAngle <= firstPartThrustAngleTo; firstPartAngle += firstPartThrustAngleStep) {
                                mp.setFirstThrustAngle(toRadians(firstPartAngle));
                                /**
                                 * Важно вызвать здесь сборщик мусора, чтобы ОЗУ не заполнялась выше 3Гб
                                 */
                                System.gc();

                                for (int secondPartAngle = secondPartThrustAngleFrom; secondPartAngle <= secondPartThrustAngleTo; secondPartAngle += secondPartThrustAngleStep) {
                                    synchronized (mainProcess) {
                                        if (mainProcess != null & mainProcessWaitFlag) {
                                            mainProcess.wait();
                                        }
                                    }

                                    if(needBreak(firstPartDuration, secondPartDuration, firstPartAngle, secondPartAngle)) {
                                        completedIterations++;
                                        writeToMonitor(completedIterations, firstPartDuration, secondPartDuration,
                                                firstPartAngle, secondPartAngle, LocalTime.now());
                                        break;
                                    }

                                    mp.setSecondThrustAngle(toRadians(secondPartAngle));

                                    earth.setPosition(new Point3D(earth.getOrbitRadius(), 0, 0));
                                    earth.setSpeed(new Vector3D(0, 29784.48, 0));

                                    spacecraft.setPosition(new Point3D(earth.getPosition().minus(new Point3D(earth.getGravitationalRadius(), 0, 0))));
                                    spacecraft.setSpeed(new Vector3D(0, spacecraftStartSpeed, 0));
                                    spacecraft.setAcceleration(null);

                                    EarthToEarthTrajectory trj = new EarthToEarthTrajectory(mp, earth, solar, spacecraft);
                                    trj.startCalculate();

                                    List<Double> distance = new ArrayList<>(trj.getDistancesToEarth());
                                    Collections.sort(distance);

                                    if (distance.get(0) < minDistance) {
                                        textAreaResultLog.appendText(parametersToResultLog(firstPartDuration, secondPartDuration,
                                                firstPartAngle, secondPartAngle, distance.get(0)));

                                        addAllCompleteParametersFromSecondPartDuration(firstPartDuration, secondPartDuration,
                                                secondPartDurationTo, secondPartDurationStep, firstPartAngle, secondPartAngle,
                                                distance.get(0));
                                    }

                                    completedIterations++;
                                    writeToMonitor(completedIterations, firstPartDuration, secondPartDuration,
                                            firstPartAngle, secondPartAngle, LocalTime.now());
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

    private boolean needBreak(int firstPartDuration, int secondPartDuration, int firstPartAngle, int secondPartAngle) {
        return completeParameters.stream().anyMatch((complete) -> {
            complete = complete.substring(0, 18);
            String currentParameters =
                    parametersToResultLog(firstPartDuration, secondPartDuration, firstPartAngle, secondPartAngle, 0)
                            .substring(0, 18);
            return complete.equals(currentParameters);
        });
    }

    private String headResultLog(Vector3D spacecraftSpeed, Point3D spacecraftPosition, double mass) {
        return "Cкорость космического аппарата: " + String.format("%.2f", spacecraftSpeed.getScalar()) + "\n"
                + "Поцизия космического аппарата: " + "\n"
                + "X: " + String.format("%.2f", mToKm(spacecraftPosition.getX())) + "\n"
                + "Y: " + String.format("%.2f", mToKm(spacecraftPosition.getY())) + "\n"
                + "Z: " + String.format("%.2f", mToKm(spacecraftPosition.getZ())) + "\n"
                + "Масса космического аппарата: " + String.format("%.2f", mass) + "\n\n";
    }

    private String thrustToResultLog(double thrust) {
        return String.format("Сейчас рассматривается тяга: %.2f", thrust) + "\n";
    }

    private String parametersToResultLog(int firstPartDuration, int secondPartDuration,
                                         int firstPartAngle, int secondPartAngle, double distance) {
        return String.format("%4d", firstPartDuration) + " "
                + String.format("%4d", secondPartDuration) + " "
                + String.format("%5d", firstPartAngle) + " "
                + String.format("%5d", secondPartAngle) + " "
                + String.format("%10.2f", mToKm(distance)) + "\n";
    }

    private void writeToMonitor(int completedIterations, int firstPartDuration, int secondPartDuration,
                                int firstPartAngle, int secondPartAngle, LocalTime currentTime) {
        synchronized (monitor) {
            monitor.setCompletedIterations(completedIterations);
            monitor.setFirstPartDuration(firstPartDuration);
            monitor.setSecondPartDuration(secondPartDuration);
            monitor.setFirstPartAngle(firstPartAngle);
            monitor.setSecondPartAngle(secondPartAngle);
            monitor.setCurrentTime(currentTime);
        }
    }

    private void addAllCompleteParametersFromSecondPartDuration(int firstPartDuration, int secondPartDuration,
                                                                int secondPartDurationTo, int secondPartDurationStep,
                                                                int firstPartAngle, int secondPartAngle, double distance) {
        for(int spd = secondPartDuration; spd <= secondPartDurationTo; spd += secondPartDurationStep) {
            completeParameters.add(parametersToResultLog(firstPartDuration, spd,
                    firstPartAngle, secondPartAngle, distance));
        }
    }

    private void turnMainProcess() {
        synchronized (mainProcess) {
            if (mainProcess != null) {
                if(!mainProcessWaitFlag) {
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
    }

    private void runMonitoringProcess() {
        monitoringProcess = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    synchronized (monitor) {
                        double percent = (monitor.getCompletedIterations() * 100);
                        percent = percent / monitor.getSumOperations();
                        Duration deltaTime = Duration.between(monitor.getStartTime(), monitor.getCurrentTime());
                        long sumTime = (long) ((deltaTime.toMinutes() * 100) / percent);
                        labelInfo.setText("Выполнено " + monitor.getCompletedIterations()
                                + " операций из " + monitor.getSumOperations() + ".\n"
                                + "Поиск завершён на " + String.format("%.2f", percent) + " %\n"
                                + "Осталось времени (примерно): " + sumTime + " мин\n"
                                + "Сейчас рассматривается "
                                + monitor.getFirstPartDuration() + " "
                                + monitor.getSecondPartDuration() + " "
                                + monitor.getFirstPartAngle() + " "
                                + monitor.getSecondPartAngle());
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

        validateMassField(errorList);
        validateThrustField(errorList);
        validateSpacecraftDegreePosition(errorList);
        validateSpacecraftStartSpeed(errorList);
        validateFirstPartDuration(errorList);
        validateFirstPartThrustAngle(errorList);
        validateSecondPartDuration(errorList);
        validateSecondPartThrustAngle(errorList);
        validateMinDistance(errorList);

        StringBuilder errorMessage = new StringBuilder("Следующие поля заполненены неправильно: \n");
        for (String s : errorList) {
            errorMessage.append(" - " + s + "\n");
        }

        if (!errorList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка ввода данных");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void validateMassField(List<String> errorList) {
        try {
            mass = Double.parseDouble(textFieldMass.getText());

            if (mass <= 0) {
                errorList.add("Масса КА: масса должна быть больше 0");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Масса КА");
        }
    }

    private void validateThrustField(List<String> errorList) {
        try {
            thrustFrom = Double.parseDouble(textFieldThrustFrom.getText());
            thrustTo = Double.parseDouble(textFieldThrustTo.getText());
            thrustStep = Double.parseDouble(textFieldThrustStep.getText());

            if (thrustFrom > thrustTo) {
                errorList.add("Диапазон тяги двигателя: значение в поле <от> должно быть меньше <до>");
            }

            if (thrustFrom < thrustTo && thrustTo - thrustFrom < thrustStep) {
                errorList.add("Диапазон тяги двигателя: значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон тяги двигателя");
        }
    }

    private void validateSpacecraftDegreePosition(List<String> errorList) {
        try {
            spacecraftDegreePosition = Double.parseDouble(textFieldSpacecraftPosition.getText());

            if (spacecraftDegreePosition < 0 | spacecraftDegreePosition > 359) {
                errorList.add("Позиция старта КА: значение угла должно быть в диапазоне 0...359");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Позиция старта КА");
        }
    }

    private void validateSpacecraftStartSpeed(List<String> errorList) {
        try {
            spacecraftStartSpeed = kmToM(Double.parseDouble(textFieldSpacecraftSpeed.getText()));
        } catch (NumberFormatException ex) {
            errorList.add("Начальная скорость КА");
        }
    }

    private void validateFirstPartDuration(List<String> errorList) {
        try {
            firstPartDurationFrom = Integer.parseInt(textFieldFirstPartTimeFrom.getText());
            firstPartDurationTo = Integer.parseInt(textFieldFirstPartTimeTo.getText());
            firstPartDurationStep = Integer.parseInt(textFieldFirstPartTimeStep.getText());

            if (firstPartDurationFrom > firstPartDurationTo) {
                errorList.add("Диапазон длительности (Первый участок): значение в поле <от> должно быть меньше <до>");
            }

            if (firstPartDurationFrom < firstPartDurationTo && firstPartDurationTo - firstPartDurationFrom < firstPartDurationStep) {
                errorList.add("Диапазон длительности (Первый участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон длительности (Первый участок)");
        }
    }

    private void validateFirstPartThrustAngle(List<String> errorList) {
        try {
            firstPartThrustAngleFrom = Integer.parseInt(textFieldFirstPartAngleFrom.getText());
            firstPartThrustAngleTo = Integer.parseInt(textFieldFirstPartAngleTo.getText());
            firstPartThrustAngleStep = Integer.parseInt(textFieldFirstPartAngleStep.getText());

            if (firstPartThrustAngleFrom > firstPartThrustAngleTo) {
                errorList.add("Диапазон угла тяги (Первый участок): значение в поле <от> должно быть меньше <до>");
            }

            if (firstPartThrustAngleFrom < firstPartThrustAngleTo && firstPartThrustAngleTo - firstPartThrustAngleFrom < firstPartThrustAngleStep) {
                errorList.add("Диапазон угла тяги (Первый участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон угла тяги (Первый участок)");
        }
    }

    private void validateSecondPartDuration(List<String> errorList) {
        try {
            secondPartDurationFrom = Integer.parseInt(textFieldSecondPartTimeFrom.getText());
            secondPartDurationTo = Integer.parseInt(textFieldSecondPartTimeTo.getText());
            secondPartDurationStep = Integer.parseInt(textFieldSecondPartTimeStep.getText());

            if (secondPartDurationFrom > secondPartDurationTo) {
                errorList.add("Диапазон длительности (Второй участок): значение в поле <от> должно быть меньше <до>");
            }

            if (secondPartDurationFrom < secondPartDurationTo && secondPartDurationTo - secondPartDurationFrom < secondPartDurationStep) {
                errorList.add("Диапазон длительности (Второй участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон длительности (Второй участок)");
        }
    }

    private void validateSecondPartThrustAngle(List<String> errorList) {
        try {
            secondPartThrustAngleFrom = Integer.parseInt(textFieldSecondPartAngleFrom.getText());
            secondPartThrustAngleTo = Integer.parseInt(textFieldSecondPartAngleTo.getText());
            secondPartThrustAngleStep = Integer.parseInt(textFieldSecondPartAngleStep.getText());

            if (secondPartThrustAngleFrom > secondPartThrustAngleTo) {
                errorList.add("Диапазон угла тяги (Второй участок): значение в поле <от> должно быть меньше <до>");
            }

            if (secondPartThrustAngleFrom < secondPartThrustAngleTo && secondPartThrustAngleTo - secondPartThrustAngleFrom < secondPartThrustAngleStep) {
                errorList.add("Диапазон угла тяги (Второй участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон угла тяги (Второй участок)");
        }
    }

    private void validateMinDistance(List<String> errorList) {
        try {
            minDistance = kmToM(Double.parseDouble(textFieldMinDistance.getText()));

            CelestialBody earth = CelestialBody.EARTH;
            if (minDistance <= 0 | minDistance <= earth.getRadius()) {
                errorList.add("Мин. сближение с Землёй: должно быть больше радиуса Земли");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Мин. сближение с Землёй");
        }
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
