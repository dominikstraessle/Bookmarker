package search;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import model.Bookmark;

public class SearchController {
    /**
     * Arraylist ist always filled with the Bookmarks matching the search keys.
     */
    private ObservableList<String> foundBookmarks = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchTxtKeyWords;

    @FXML
    private JFXComboBox<?> searchComboEnv;

    @FXML
    private JFXButton addBtnAdd;

    @FXML
    private JFXListView<String> resultList;

    @FXML
    private ImageView detailImageIcon;

    @FXML
    private Label detailLblTitle;

    @FXML
    private MenuButton detailMenuOptions;

    @FXML
    private MenuItem detailMenuDelete;

    @FXML
    private MenuItem detailMenuCitate;

    @FXML
    private MenuItem detailMenuSave;

    @FXML
    private Label detailLblUrl;

    @FXML
    private Label detailLblAdded;

    @FXML
    private Label detailLblEnv;

    @FXML
    private Label detailLblTags;

    @FXML
    private Label detailLblDesc;

    @FXML
    private JFXButton detailBtnOpen;

    @FXML
    private JFXButton detailBtnCopy;

    @FXML
    private Label statusTxtSync;

    @FXML
    private Label statusTxtServer;

    @FXML
    private Label statusTxtModify;

    @FXML
    void handleAdd(ActionEvent event) {

    }

    @FXML
    void handleCitate(ActionEvent event) {

    }

    @FXML
    void handleCopy(ActionEvent event) {

    }

    @FXML
    void handleDelete(ActionEvent event) {

    }

    @FXML
    void handleOpen(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

    @FXML
    void initialize(URL url, ResourceBundle resourceBundle) {
        resultList.setItems(foundBookmarks);//Binding
        searchTxtKeyWords.textProperty().addListener(this::onSearchListener);
    }

    private <T> void onSearchListener(ObservableValue<? extends T> observable, T oldValue, T newValue){

    }
}
