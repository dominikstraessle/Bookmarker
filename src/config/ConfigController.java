package config;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ConfigController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField confTxtServer;

    @FXML
    private JFXTextField confTxtPort;

    @FXML
    private JFXTextField confTxtUser;

    @FXML
    private JFXPasswordField confTxtPass;

    @FXML
    private JFXTextField proxyTxtServer;

    @FXML
    private JFXTextField proxyTxtPort;

    @FXML
    private JFXTextField proxyTxtUser;

    @FXML
    private JFXPasswordField proxyTxtPass;

    @FXML
    private JFXButton confBtnCheck;

    @FXML
    private JFXButton confBtnGo;

    @FXML
    void handleCheck(ActionEvent event) {

    }

    @FXML
    void handleGo(ActionEvent event) {

    }

    @FXML
    void initialize() {
        checkLoadSuccess();
    }

    private void checkLoadSuccess() {
        assert confTxtServer != null : "fx:id=\"confTxtServer\" was not injected: check your FXML file 'config.fxml'.";
        assert confTxtPort != null : "fx:id=\"confTxtPort\" was not injected: check your FXML file 'config.fxml'.";
        assert confTxtUser != null : "fx:id=\"confTxtUser\" was not injected: check your FXML file 'config.fxml'.";
        assert confTxtPass != null : "fx:id=\"confTxtPass\" was not injected: check your FXML file 'config.fxml'.";
        assert proxyTxtServer != null : "fx:id=\"proxyTxtServer\" was not injected: check your FXML file 'config.fxml'.";
        assert proxyTxtPort != null : "fx:id=\"proxyTxtPort\" was not injected: check your FXML file 'config.fxml'.";
        assert proxyTxtUser != null : "fx:id=\"proxyTxtUser\" was not injected: check your FXML file 'config.fxml'.";
        assert proxyTxtPass != null : "fx:id=\"proxyTxtPass\" was not injected: check your FXML file 'config.fxml'.";
        assert confBtnCheck != null : "fx:id=\"confBtnCheck\" was not injected: check your FXML file 'config.fxml'.";
        assert confBtnGo != null : "fx:id=\"confBtnGo\" was not injected: check your FXML file 'config.fxml'.";
    }
}
