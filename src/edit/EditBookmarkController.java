package edit;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import manage.Manager;
import model.Bookmark;
import model.Environment;
import model.Tag;

/**
 * Controller of the editBookmark.fxml
 *
 * @author Dominik Strässle
 */
public class EditBookmarkController {

    /**
     * Reference to the Manager that initialized the controller
     */
    private Manager manager;
    /**
     * The actual Dialog Stage, the ok and cancel button must have the ability to interact with it.
     */
    private Stage dialogStage;

    /**
     * the old Bookmark without changes
     */
    private Bookmark oldBookmark;

    @FXML
    private ResourceBundle resources;

    @FXML
    private JFXTextField editTxtUrl;

    @FXML
    private JFXTextField editTxtTitle;

    @FXML
    private JFXTextField editTxtTags;

    @FXML
    private JFXComboBox<Environment> editComboEnv;

    @FXML
    private JFXTextArea editTxtDesc;

    @FXML
    private Label addLblAdded;

    /**
     * When the Button is clicked, it checks if the information is valid.
     * This will call the methods to set the changes to the edited bookmark
     *
     * @param event Button clicked event.
     */
    @FXML
    void handleEdit(ActionEvent event) {
        //all fields are valid
        if (checkFields()) {

            //create new Bookmark with the existing id
            Bookmark newBookmark = new Bookmark(
                    oldBookmark.getId(),
                    editTxtDesc.getText(),
                    editTxtTitle.getText(),
                    editTxtUrl.getText(),
                    LocalDateTime.now(),
                    editComboEnv.getSelectionModel().getSelectedItem(),
                    Tag.add(editTxtTags.getText()));

//            //if there are no changes return
//            if (newBookmark.equals(oldBookmark)) {
//                dialogStage.close();
//                return;
//            } else {
//                //the Bookmarks gets the actual date, because it's modified
//                newBookmark.setModified(LocalDateTime.now());
//            }

            try {
                //try to edit the oldBookmark, the IO with the database can fail
                Bookmark.edit(oldBookmark, newBookmark);
            } catch (SQLException exception) {
                //Show alert and Log
                Manager.alertException(
                        resources.getString("error"),
                        resources.getString("error.11"),
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
        if (editTxtUrl.getText().equals("")) return false;//url
        if (editTxtTitle.getText().equals("")) return false;//title
        if (editTxtTags.getText().equals("")) return false;//tags
        if (editTxtDesc.getText().equals("")) return false;//desc
        if (editComboEnv.getSelectionModel().getSelectedItem() == null) return false;//env
        if (oldBookmark == null) return false;//old environment is null
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
        editComboEnv.itemsProperty().bind(Environment.environmentsPropertyProperty());
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

    /**
     * Set the selected bookmark which should be edited and fills all fields with the given values
     *
     * @param oldBookmark bookmark to edit
     */
    public void setBookmark(Bookmark oldBookmark) {
        this.oldBookmark = oldBookmark;

        //set the old values
        editTxtTitle.setText(oldBookmark.getTitle());
        editTxtDesc.setText(oldBookmark.getDesc());
        editComboEnv.getSelectionModel().select(oldBookmark.getEnvironment());
        editTxtTags.setText(oldBookmark.getTags().stream()//get all tags of a bookmark as stream
                .map(Tag::getTagString)//only the text of the tags in the list
                .collect(Collectors.joining(" ")));//collect to a string delimited by a blank
        editTxtUrl.setText(oldBookmark.getUrl());
    }
}
