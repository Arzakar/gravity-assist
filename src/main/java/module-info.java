module com.klimashin {

    requires lombok;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.logging;

    exports ru.klimashin.ballistic;
    exports ru.klimashin.ballistic.controller;

    opens ru.klimashin.ballistic to javafx.fxml;
    opens ru.klimashin.ballistic.controller to javafx.fxml;

}
