package com.klimashin.controller;

import com.klimashin.model.entity.Engine;
import com.klimashin.model.entity.Spacecraft;
import com.klimashin.model.entity.celestial.Earth;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@Log
public class SolutionSearchAlgorithm {

    @FXML
    public void startSearch() {
        if(!initDataIsTrue()) {
            return;
        }

        Spacecraft spacecraft = new Spacecraft("Expiremental",
                Double.valueOf(textFieldMass.getText()),
                new Engine("Experimental", 0d),
                1, null, null,null);


    }

    private boolean initDataIsTrue() {
        log.info("Началась валидация");
        List<String> errorList = new ArrayList<>();


        try {
            log.info("Проверка массы");
            double mass = Double.valueOf(textFieldMass.getText());
            log.info("Проверка массы прошла успешно");
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
            double from = Double.valueOf(textFieldFirstPartAngleFrom.getText());
            double to = Double.valueOf(textFieldFirstPartAngleTo.getText());
            double step = Double.valueOf(textFieldFirstPartAngleStep.getText());

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
            double from = Double.valueOf(textFieldSecondPartAngleFrom.getText());
            double to = Double.valueOf(textFieldSecondPartAngleTo.getText());
            double step = Double.valueOf(textFieldSecondPartAngleStep.getText());

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

    @FXML
    private TextArea textAreaResultLog;

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
    private TextField textFieldSecondPartTimeFrom;

    @FXML
    private TextField textFieldSecondPartTimeTo;

    @FXML
    private TextField textFieldSecondPartTimeStep;

    @FXML
    private TextField textFieldSecondPartAngleFrom;

    @FXML
    private TextField textFieldSecondPartAngleTo;

    @FXML
    private TextField textFieldSecondPartAngleStep;

    @FXML
    private TextField textFieldMinDistance;
}
