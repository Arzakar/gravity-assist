package ru.klimashin.ballistic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {

	@FXML
	Button primaryButton;
	
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
        
    }
}