package add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import manage.Manager;
import model.Bookmark;
import model.Environment;
import model.Tag;

/**
 * Controller of the addBookmark.fxml
 *
 * @author Dominik Strässle
 */
public class AddBookmarkController {

    /**
     * Reference to the Manager that initialized the controller
     */
    private Manager manager;
    /**
     * The actual Dialog Stage, the ok and cancel button must have the ability to interact with it.
     */
    private Stage dialogStage;

    @FXML
    public JFXButton addBtnAddEnv;

    @FXML
    private ResourceBundle resources;

    @FXML
    private JFXTextField addTxtUrl;

    @FXML
    private JFXTextField addTxtTitle;

    @FXML
    private JFXTextField addTxtTags;

    @FXML
    private JFXComboBox<Environment> addComboEnv;

    @FXML
    private JFXTextArea addTxtDesc;

    @FXML
    private Label addLblAdded;

    @FXML
    private JFXButton addBtnCancel;

    @FXML
    private JFXButton addBtnAdd;

    /**
     * Eventhandler for {@link #addBtnAdd}. When the Button is clicked, it checks if the information is valid.
     * Then it creates a new Bookmark with the given information.
     * For adding the correct references of @{@link Tag} to the bookmark it uses the Tag.add Method.
     * Then the Bookmark will be added to the Bookmark list and the stage closed.
     *
     * @param event Button clicked event.
     */
    @FXML
    void handleAdd(ActionEvent event) {
        if (checkFields()) {
            //all fields are valid

            //create new Bookmark
            Bookmark bookmark = new Bookmark(
                    Bookmark.getNewID(),
                    addTxtDesc.getText(),
                    addTxtTitle.getText(),
                    addTxtUrl.getText(),
                    LocalDateTime.now(),
                    addComboEnv.getSelectionModel().getSelectedItem(),
                    Tag.add(addTxtTags.getText()));
            try {
                //try to add the bookmark, the IO with the database can fail
                Bookmark.add(bookmark);
            } catch (SQLException exception) {
                //Show alert and Log
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
            //not all fields are valid
            //show a warning and do not add the Bookmark
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
     * @return true if all required fields are filled, else false.
     */
    private boolean checkFields() {
        if (addTxtUrl.getText().equals("")) return false;//url
        if (addTxtTitle.getText().equals("")) return false;//title
        if (addTxtTags.getText().equals("")) return false;//tags
        if (addTxtDesc.getText().equals("")) return false;//desc
        if (addComboEnv.getSelectionModel().getSelectedItem() == null) return false;//env
        return true;//everything valid
    }

    /**
     * {@link #addBtnCancel} is pressed
     *
     * @param event
     */
    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    /**
     * initialize the view.
     */
    @FXML
    void initialize() {
        //set the actual Date and Time to the added Label.
        addLblAdded.setText(LocalDateTime.now().toString());//Now
        //bind all Environments to the combobox
        addComboEnv.itemsProperty().bind(Environment.environmentsPropertyProperty());
    }


    /**
     * Setter
     *
     * @param manager Reference to the Manager
     */
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    /**
     * Setter
     *
     * @param dialogStage Refernce to the Dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Handles the {@link #addBtnAddEnv} Button
     *
     * @param actionEvent
     */
    @FXML
    public void handleAddEnv(ActionEvent actionEvent) {
        try {
            manager.showAddEnvironment(dialogStage);
        } catch (IOException exception) {
            //failed to load the addEnvironment.fxml
            Manager.alertException(
                    resources.getString("error"),
                    resources.getString("error.01"),
                    manager.getPrimaryStage(),
                    exception);
        }
    }
}
