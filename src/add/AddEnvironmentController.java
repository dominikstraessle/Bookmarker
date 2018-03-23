package add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import manage.Manager;
import model.Environment;

/**
 * Controller of the addEnvironment.fxml
 *
 * @author Dominik Strässle
 */
public class AddEnvironmentController {

    /**
     * Stage
     */
    private Stage dialogStage;

    @FXML
    private JFXTextField addTxtName;
    @FXML
    private ResourceBundle resources;

    @FXML
    private JFXColorPicker addClrColor;

    @FXML
    private JFXTextArea addTxtDesc;

    @FXML
    private JFXButton addBtnCancel;

    @FXML
    private JFXButton addBtnAdd;

    /**
     * Eventhandler for {@link #addBtnAdd}. When the Button is clicked, it checks if the fields are filled valid.
     * Then it creates a new Environment with the given information.
     * The new Environment will be added to the @{@link Environment#environments} List.
     *
     * @param event Button clicked event.
     */
    @FXML
    void handleAdd(ActionEvent event) {
        if (checkFields()) {

            try {
                Environment.add(new Environment(
                        Environment.getNewID(),//ID
                        addTxtName.getText(),//Name
                        addTxtDesc.getText(),//Desc
                        addClrColor.getValue()));//Color
            } catch (SQLException exception) {
                //failed to add a environment, IO with database failed
                Manager.alertException(
                        resources.getString("error"),
                        resources.getString("error.02"),
                        this.dialogStage,
                        exception
                );
            }
            //close the stage
            dialogStage.close();
        } else {
            //fields are not filled valid
            Manager.alertWarning(
                    resources.getString("add.invalid"),
                    resources.getString("add.invalid"),
                    resources.getString("add.invalid.content"),
                    this.dialogStage);
        }
    }


    /**
     * Checks if all required fields are filled.
     *
     * @return true if all required fiels are filled, else false.
     */
    private boolean checkFields() {
        if (addTxtName.getText().equals("")) return false;//name
        if (addTxtDesc.getText().equals("")) return false;//desc
        if (addClrColor.getValue() == null) return false;//color
        return true;//everything is valid
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    public void initialize() {

    }

    /**
     * the current {@link #dialogStage} can be set
     *
     * @param dialogStage given Stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
