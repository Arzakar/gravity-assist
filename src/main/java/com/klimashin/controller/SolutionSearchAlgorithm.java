package com.klimashin.controller;

import com.klimashin.model.entity.Engine;
import com.klimashin.model.entity.Spacecraft;
import com.klimashin.model.entity.celestial.Earth;
import com.klimashin.model.trajectory.TrajectoryVariables;
import com.klimashin.model.trajectory.earthtoearth.EarthToEarthTrajectory;
import com.klimashin.model.util.AngleAndTimeParameters;
import com.klimashin.model.util.math.GeneralFormulas;
import com.klimashin.model.util.math.Point3D;
import com.klimashin.model.util.math.Vector3D;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log
public class SolutionSearchAlgorithm {

    @FXML
    public void startSearch() {
        if(!initDataIsTrue()) {
            return;
        }

        Spacecraft spacecraft = new Spacecraft("Experimental",
                Double.valueOf(textFieldMass.getText()),
                new Engine("Experimental", 0d),
                1, null, null,null);

        AngleAndTimeParameters parameters = new AngleAndTimeParameters(0, 100, 0, 0, 0, 0);

        double thrustCurrent = Double.valueOf(textFieldThrustFrom.getText());
        double thrustTo = Double.valueOf(textFieldThrustTo.getText());
        double thrustStep = Double.valueOf(textFieldThrustStep.getText());

        int firstPartTimeFrom = Integer.valueOf(textFieldFirstPartTimeFrom.getText());
        int firstPartTimeTo = Integer.valueOf(textFieldFirstPartTimeTo.getText());
        int firstPartTimeStep = Integer.valueOf(textFieldFirstPartTimeStep.getText());

        int secondPartTimeFrom = Integer.valueOf(textFieldSecondPartTimeFrom.getText());
        int secondPartTimeTo = Integer.valueOf(textFieldSecondPartTimeTo.getText());
        int secondPartTimeStep = Integer.valueOf(textFieldSecondPartTimeStep.getText());

        int firstPartAngleFrom = Integer.valueOf(textFieldFirstPartAngleFrom.getText());
        int firstPartAngleTo = Integer.valueOf(textFieldFirstPartAngleTo.getText());
        int firstPartAngleStep = Integer.valueOf(textFieldFirstPartAngleStep.getText());

        int secondPartAngleFrom = Integer.valueOf(textFieldSecondPartAngleFrom.getText());
        int secondPartAngleTo = Integer.valueOf(textFieldSecondPartAngleTo.getText());
        int secondPartAngleStep = Integer.valueOf(textFieldSecondPartAngleStep.getText());

        double spacecraftAnglePosition = Math.toRadians(Double.valueOf(textFieldSpacecraftPosition.getText()));
        double spacecraftSpeed = Double.valueOf(textFieldSpacecraftSpeed.getText());
        double minDistance = Double.valueOf(textFieldMinDistance.getText());

        int sumOperation = (int) ((firstPartTimeTo - firstPartTimeFrom) / firstPartTimeStep + 1);
        sumOperation *= (int) ((secondPartTimeTo - secondPartTimeFrom) / secondPartTimeStep + 1);
        sumOperation *= (int) ((firstPartAngleTo - firstPartAngleFrom) / firstPartAngleStep + 1);
        sumOperation *= (int) ((secondPartAngleTo - secondPartAngleFrom) / secondPartAngleStep + 1);
        sumOperation *= (int) ((thrustTo - thrustCurrent) / thrustStep + 1);

        final int finalSumOperation = sumOperation;

        final Earth EARTH_PARAMETERS = new Earth();

        new Thread(() -> {
            int currentOperation = 0;
            List<StringBuilder> results = new ArrayList<>();

            while(thrustCurrent <= thrustTo) {
                List<AngleAndTimeParameters> failureParameters = new ArrayList<>();
                List<AngleAndTimeParameters> alreadyCompletedParameters = new ArrayList<>();

                for(int firstPartTime = firstPartTimeFrom; firstPartTime <= firstPartTimeTo; firstPartTime += firstPartTimeStep) {
                    parameters.setFirstPartDuration((int) GeneralFormulas.daysToSeconds(firstPartTime));

                    for(int secondPartTime = secondPartTimeFrom; secondPartTime <= secondPartTimeTo; secondPartTime += secondPartTimeStep) {
                        parameters.setSecondPartDuration((int) GeneralFormulas.daysToSeconds(secondPartTime));

                        for(int firstPartAngle = firstPartAngleFrom; firstPartAngle <= firstPartAngleTo; firstPartAngle += firstPartAngleStep) {
                            parameters.setFirstTrustAngle(Math.toRadians(firstPartAngle));

                            boolean needBreak = false;
                            for(AngleAndTimeParameters p : failureParameters) {
                                if(p.getFirstTrustAngle() == firstPartAngle) {
                                    needBreak = true;
                                }
                            }

                            if(needBreak) {
                                currentOperation += (secondPartAngleTo - secondPartAngleFrom) * secondPartAngleStep + 1;
                                break;
                            }

                            for(int secondPartAngle = secondPartAngleFrom; secondPartAngle <= secondPartAngleTo; secondPartAngle += secondPartAngleStep) {
                                parameters.setSecondThrustAngle(Math.toRadians(secondPartAngle));

                                EarthToEarthTrajectory trajectory = new EarthToEarthTrajectory(spacecraft, parameters);

                                Point3D startEarthPosition = new Point3D(EARTH_PARAMETERS.getOrbitRadius(), 0, 0);
                                trajectory.getEarth().setPosition(startEarthPosition);

                                Point3D startSpacecraftPosition = new Point3D(trajectory.getEarth().getPosition());
                                startSpacecraftPosition.setX(startSpacecraftPosition.getX()
                                        + EARTH_PARAMETERS.getOrbitRadius() * Math.cos(spacecraftAnglePosition));
                                startSpacecraftPosition.setY(startSpacecraftPosition.getY()
                                        + EARTH_PARAMETERS.getOrbitRadius() * Math.sin(spacecraftAnglePosition));

                                trajectory.calculateTrajectory(startEarthPosition, startSpacecraftPosition,
                                        new Vector3D(0, spacecraftSpeed, 0), TrajectoryVariables.CALCULATION);

                                if(trajectory.getSpacecraftPosition().size() <= firstPartTime / parameters.getDeltaTime()) {
                                    AngleAndTimeParameters failure = parameters;
                                    failure.setFirstPartDuration(firstPartTime);
                                    failure.setFirstTrustAngle(firstPartAngle);
                                    failure.setSecondPartDuration(secondPartTime);
                                    failure.setSecondThrustAngle(secondPartAngle);

                                    failureParameters.add(failure);

                                    currentOperation++;

                                    break;
                                }

                                List<Double> distanceList = new ArrayList<>();
                                distanceList.addAll(trajectory.getDistanceToEarth());
                                Collections.sort(distanceList);

                                if(distanceList.get(0) < minDistance) {
                                    StringBuilder result = new StringBuilder(String.format("%.3d", thrustCurrent) + " "
                                            + String.format("%3d", firstPartTime) + " "
                                            + String.format("%3d", secondPartTime) + " "
                                            + String.format("%4d", firstPartAngle) + " "
                                            + String.format("%4d", secondPartAngle) + " "
                                            + String.format("%13.2f", distanceList.get(0)) + "\n");

                                    results.add(result);
                                }

                                currentOperation++;

                                final int finalCurrentOperation = currentOperation;
                                final int finalFirstPartTime = firstPartTime;
                                final double finalFirstPartAngle = firstPartAngle;
                                final int finalSecondPartTime = secondPartTime;
                                final double finalSecondPartAngle = secondPartAngle;

                                Platform.runLater(() -> {
                                    writeToInfoLabel(finalCurrentOperation, finalSumOperation, thrustCurrent, finalFirstPartTime,
                                            finalFirstPartAngle, finalSecondPartTime, finalSecondPartAngle);

                                });


                            }
                        }
                    }
                }
            }
        }).start();
    }

    private boolean initDataIsTrue() {

        List<String> errorList = new ArrayList<>();

        try {
            double mass = Double.valueOf(textFieldMass.getText());

            if(mass <= 0) {
                errorList.add("Масса КА: масса должна быть больше 0");
            }
        } catch(NumberFormatException ex) {
            log.info("Проверка не пройдена");
            errorList.add("Масса КА");
        }

        try {
            double from = Double.valueOf(textFieldThrustFrom.getText());
            double to = Double.valueOf(textFieldThrustTo.getText());
            double step = Double.valueOf(textFieldThrustStep.getText());

            if(from > to) {
                errorList.add("Диапазон тяги двигателя: значение в поле <от> должно быть меньше <до>");
            }

            if(from < to && to - from < step) {
                errorList.add("Диапазон тяги двигателя: значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон тяги двигателя");
        }

        try {
            double position = Double.valueOf(textFieldSpacecraftPosition.getText());

            if(position < 0 | position > 359) {
                errorList.add("Позиция старта КА: значение угла должно быть в диапазоне 0...359");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Позиция старта КА");
        }

        try {
            Double.valueOf(textFieldSpacecraftSpeed.getText());
        } catch (NumberFormatException ex) {
            errorList.add("Начальная скорость КА");
        }

        try {
            int from = Integer.valueOf(textFieldFirstPartTimeFrom.getText());
            int to = Integer.valueOf(textFieldFirstPartTimeTo.getText());
            int step =Integer.valueOf(textFieldFirstPartTimeStep.getText());

            if(from > to) {
                errorList.add("Диапазон длительности (Первый участок): значение в поле <от> должно быть меньше <до>");
            }

            if(from < to && to - from < step) {
                errorList.add("Диапазон длительности (Первый участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон длительности (Первый участок)");
        }

        try {
            int from = Integer.valueOf(textFieldFirstPartAngleFrom.getText());
            int to = Integer.valueOf(textFieldFirstPartAngleTo.getText());
            int step = Integer.valueOf(textFieldFirstPartAngleStep.getText());

            if(from > to) {
                errorList.add("Диапазон угла тяги (Первый участок): значение в поле <от> должно быть меньше <до>");
            }

            if(from < to && to - from < step) {
                errorList.add("Диапазон угла тяги (Первый участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон угла тяги (Первый участок)");
        }

        try {
            int from = Integer.valueOf(textFieldSecondPartTimeFrom.getText());
            int to = Integer.valueOf(textFieldSecondPartTimeTo.getText());
            int step = Integer.valueOf(textFieldSecondPartTimeStep.getText());

            if(from > to) {
                errorList.add("Диапазон длительности (Второй участок): значение в поле <от> должно быть меньше <до>");
            }

            if(from < to && to - from < step) {
                errorList.add("Диапазон длительности (Второй участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон длительности (Второй участок)");
        }

        try {
            int from = Integer.valueOf(textFieldSecondPartAngleFrom.getText());
            int to = Integer.valueOf(textFieldSecondPartAngleTo.getText());
            int step = Integer.valueOf(textFieldSecondPartAngleStep.getText());

            if(from > to) {
                errorList.add("Диапазон угла тяги (Второй участок): значение в поле <от> должно быть меньше <до>");
            }

            if(from < to && to - from < step) {
                errorList.add("Диапазон угла тяги (Второй участок): значение <шаг> слишком большое");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Диапазон угла тяги (Второй участок)");
        }

        try {
            double distance = Double.valueOf(textFieldMinDistance.getText());

            Earth earth = new Earth();
            if(distance <= 0 | distance <= earth.getRadius()) {
                errorList.add("Мин. сближение с Землёй: должно быть больше радиуса Земли");
            }
        } catch (NumberFormatException ex) {
            errorList.add("Мин. сближение с Землёй");
        }

        String errorMessage = "Следующие поля заполненены неправильно: \n";
        for(String s : errorList) {
            errorMessage += "   - " + s + "\n";
        }

        if(!errorList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка ввода данных");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void writeToInfoLabel(int currentOperation, int sumOperations, double currentThrust, int firstPartDuration, double firstPartAngle,
                                  int secondPartDuration, double secondPartAngle) {
        int percent = (int) ((currentOperation / sumOperations) * 100);
        labelInfo.setText("Выполнено " + currentOperation + " операций из " + sumOperations + ". "
                + "Поиск завершён на " + percent + " \n"
                + "Тяга двигателя " + currentThrust + " \n"
                + "Сейчас рассматривается " + firstPartDuration + " " + secondPartDuration + " " + firstPartAngle + " " + secondPartAngle);
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
