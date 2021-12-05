module com.klimashin {

    requires lombok;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.logging;

    exports com.klimashin;
    exports com.klimashin.controller;

    opens com.klimashin to javafx.fxml;
    opens com.klimashin.controller to javafx.fxml;


}
