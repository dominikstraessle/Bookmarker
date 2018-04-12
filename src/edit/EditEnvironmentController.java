package edit;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import manage.Manager;
import model.Bookmark;
import model.Environment;

/**
 * Controller of the addEnvironment.fxml
 *
 * @author Dominik Strässle
 */
public class EditEnvironmentController {

    /**
     * Stage
     */
    private Stage dialogStage;

    /**
     * the environment without changes
     */
    private Environment oldEnvironment;

    @FXML
    private JFXTextField editTxtName;
    @FXML
    private ResourceBundle resources;

    @FXML
    private JFXColorPicker editClrColor;

    @FXML
    private JFXTextArea editTxtDesc;

    /**
     * When the Button is clicked, it checks if the fields are filled valid.
     * Then it creates a new Environment with the changed values but the old id.
     * The old Environment will be changed in the memory and database
     *
     * @param event Button clicked event.
     */
    @FXML
    void handleEdit(ActionEvent event) {
        if (checkFields()) {

            try {
                //create a pseudo new environment with the changes but same id
                Environment newEnvironment = new Environment(oldEnvironment.getId(), editTxtName.getText(), editTxtDesc.getText(), editClrColor.getValue());

                //if there are no changes return
//                if (newEnvironment.equals(oldEnvironment)) {
//                    dialogStage.close();
//                    return;
//                }

                //edit the oldenvironment
                Environment.edit(oldEnvironment, newEnvironment);
            } catch (SQLException exception) {
                //failed to save a environment, IO with database failed
                Manager.alertException(
                        resources.getString("error"),
                        resources.getString("error.10"),
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
        Bookmark.refreshBookmarksResultsProperty();
    }


    /**
     * Checks if all required fields are filled.
     *
     * @return true if all required fiels are filled, else false.
     */
    private boolean checkFields() {
        if (editTxtName.getText().equals("")) return false;//name
        if (editTxtDesc.getText().equals("")) return false;//desc
        if (editClrColor.getValue() == null) return false;//color
        if (oldEnvironment == null) return false;//environment is null
        return true;//everything is valid
    }

    /**
     * Close the stage
     *
     * @param event cancel button clicked
     */
    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    public void initialize() {

    }


    /**
     * Sets the oldEnvironment and fills all fields with the already existing values
     *
     * @param oldEnvironment environment to edit
     */
    public void setEnvironment(Environment oldEnvironment) {
        this.oldEnvironment = oldEnvironment;

        //set the old values
        editTxtName.setText(oldEnvironment.getName());
        editTxtDesc.setText(oldEnvironment.getDesc());
        editClrColor.setValue(oldEnvironment.getColor());
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
