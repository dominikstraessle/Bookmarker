package search;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Bookmark;

public class SearchController {


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
    private JFXListView<Bookmark> resultList;

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

    /**
     * Constuctor is called before the {@link #initialize()}  method.
     */
    public SearchController() {

    }

    /**
     * Initialize Method is called after the fxml is loaded.
     */
    @FXML
    void initialize() {
        resultList.itemsProperty().bind(Bookmark.resultPropertyProperty());//binds filter results to the listview
        resultList.setCellFactory(this::cellFactoryList);//sets the lookalike of a cell in the listview
        searchTxtKeyWords.textProperty().addListener(Bookmark::filter);//listener for filtering the bookmarks list with the given string
    }

    /**
     * This represents the lookalike of a cell in the {@link #resultList}.
     *
     * @param list List
     * @return the new ListCell
     */
    private ListCell<Bookmark> cellFactoryList(ListView<Bookmark> list) {
        return new ListCell<Bookmark>() {

            @Override
            protected void updateItem(Bookmark bookmark, boolean empty) {
                super.updateItem(bookmark, empty);
                if (bookmark == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    //TODO: https://www.billmann.de/2013/07/03/javafx-custom-listcell/
                    setText(null);
                    VBox vBox = new VBox();
                    Label title = new Label(bookmark.getTitle());
                    title.setStyle("-fx-font-weight: bold");
                    Label desc = new Label(bookmark.getDesc());
                    vBox.getChildren().addAll(title, desc);
                    setGraphic(vBox);
//                    setText(bookmark.getTitle() + ":\n" + bookmark.getDesc());
                    System.out.println("changed");
                }
            }
        };
    }
}
