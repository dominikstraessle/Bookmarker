package search;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import model.Tag;

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
     * Shows the detail of the given Bookmark in the detail-view if the bookmark is present.
     * Should be called when the user clicks on a result in the results list view
     *
     * @param bookmark Clicked Bookmark to show the details
     */
    private void showBookmarkDetails(Bookmark bookmark) {
        if (bookmark != null) {
            detailLblTitle.setText(bookmark.getTitle());
            detailLblUrl.setText(bookmark.getUrl());
            detailLblAdded.setText(bookmark.getAdded().toString());
            detailLblDesc.setText(bookmark.getDesc());
            detailLblEnv.setText(bookmark.getEnvironment().getName());
            detailLblTags.setText(bookmark.getTags().stream()//get all tags of a bookmark as stream
                    .map(Tag::getTag)//only the text of the tags in the list
                    .collect(Collectors.joining(" ")));//collect to a string delimited by a blank
        } else {//the bookmark is a null reference, so set all detail labels to blank
            detailLblTitle.setText("");
            detailLblUrl.setText("");
            detailLblAdded.setText("");
            detailLblDesc.setText("");
            detailLblEnv.setText("");
            detailLblTags.setText("");
        }
    }

    /**
     * Initialize Method is called after the fxml is loaded.
     */
    @FXML
    void initialize() {
        resultList.itemsProperty().bind(Bookmark.resultPropertyProperty());//binds filter results to the listview
        resultList.setCellFactory(this::cellFactoryList);//sets the lookalike of a cell in the listview
        searchTxtKeyWords.textProperty().addListener(Bookmark::filter);//listener for filtering the bookmarks list with the given string
        resultList.getSelectionModel()
                .selectedItemProperty()//get the selected item
                .addListener((observable, oldValue, newValue) -> showBookmarkDetails(newValue));//show the details of the selected item.
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
                    String bookmarkTitle = bookmark.getTitle();
                    Label title = new Label(bookmarkTitle);
                    //Make the title bold
                    title.getStyleClass().add("bold_text");
                    //Description String
                    String bookmarkDesc = bookmark.getDesc();
                    //if the String is longer than 70 then it will be shortened...
                    //just an appereance nicety
                    if (bookmarkDesc.length() > 70)
                        bookmarkDesc = bookmarkDesc.substring(0, 70) + "...";
                    Label desc = new Label(bookmarkDesc);

                    vBox.getChildren().addAll(title, desc);
                    setGraphic(vBox);
                }
            }
        };
    }
}
