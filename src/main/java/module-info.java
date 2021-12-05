module com.klimashin {

    requires lombok;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports com.klimashin;

    opens com.klimashin to javafx.graphics;

}
