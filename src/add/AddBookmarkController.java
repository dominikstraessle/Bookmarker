package add;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import manage.Manager;
import model.Bookmark;
import model.Environment;
import model.Tag;

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
    private URL location;

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
     * Eventhandler for {@link #addBtnAdd}. When the Button is clicked, it checks if the information is complete.
     * Then it creates a new Bookmark with the given information.
     * For adding the correct references of @{@link Tag} to the bookmark it uses the Tag.add Method.
     * Then the Bookmark will be added to the Bookmark list and the stage closed.
     *
     * @param event Button clicked event.
     */
    @FXML
    void handleAdd(ActionEvent event) {
        if (checkFields()) {
            String title = addTxtTitle.getText();
            String url = addTxtUrl.getText();
            String desc = addTxtDesc.getText();
            Environment env = addComboEnv.getSelectionModel().getSelectedItem();
            Bookmark bookmark = new Bookmark(Bookmark.getNewID(), desc, title, url, LocalDateTime.now(), env, Tag.add(addTxtTags.getText()));
            try {
                Bookmark.add(bookmark);
            } catch (SQLException e) {
                Manager.alertException(resources.getString("error"),
                        resources.getString("error.02"),
                        e.getMessage(), this.dialogStage,
                        e,
                        Level.SEVERE);
            }
            //TODO when the application closes, all tags, bookmarks and environments should be written to the database when the id equals -1
            dialogStage.close();
        } else {
            Manager.alertWarning(resources.getString("add.invalid"),
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
        boolean ok = true;
        if (addTxtUrl.getText().equals("")) ok = false;//url
        if (addTxtTitle.getText().equals("")) ok = false;//title
        if (addTxtTags.getText().equals("")) ok = false;//tags
        if (addTxtDesc.getText().equals("")) ok = false;//desc
        if (addComboEnv.getSelectionModel().getSelectedItem() == null) ok = false;//env
        return ok;
    }

    @FXML
    void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    /**
     * initialize the view. set the actual Date and Time to the added Label.
     */
    @FXML
    void initialize() {
        addLblAdded.setText(LocalDateTime.now().toString());//Now
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

    @FXML
    public void handleAddEnv(ActionEvent actionEvent) {
        try {
            manager.showAddEnvironment(dialogStage);
        } catch (IOException e) {
            Manager.alertException(resources.getString("error"), resources.getString("error.01"), Arrays.toString(e.getStackTrace()), manager.getPrimaryStage(), e, Level.SEVERE);
        }
    }
}
