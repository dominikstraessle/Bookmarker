package search;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import manage.Manager;
import model.Bookmark;
import model.Tag;

public class SearchController {

    /**
     * Reference to the Manager that initialized the controller
     */
    private Manager manager;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchTxtKeyWords;

    @FXML
    private JFXComboBox<?> searchComboEnv;

    @FXML
    private JFXButton addBtnAddBookmark;

    @FXML
    private JFXButton addBtnAddEnv;

    @FXML
    private JFXListView<Bookmark> resultList;

    @FXML
    private ImageView detailImageIcon;

    @FXML
    private JFXTextField detailTxtTitle;

    @FXML
    private MenuButton detailMenuOptions;

    @FXML
    private MenuItem detailMenuDelete;

    @FXML
    private MenuItem detailMenuCitate;

    @FXML
    private MenuItem detailMenuSave;

    @FXML
    private JFXTextField detailTxtUrl;

    @FXML
    private JFXTextField detailTxtAdded;

    @FXML
    private JFXTextField detailTxtEnv;

    @FXML
    private JFXTextField detailTxtTags;

    @FXML
    private JFXTextArea detailTxtDesc;

    @FXML
    private WebView detailWebImage;

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

    /**
     * Open the Dialog to add a Bookmark.
     * @param event Add-Button clicked.
     */
    @FXML
    void handleAddBookmark(ActionEvent event) {
        try {
            manager.showAddBookmark();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("error"));
            alert.setHeaderText(resources.getString("error.01"));
            alert.setContentText(e.getMessage());
            alert.initOwner(manager.getPrimaryStage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleAddEnv(ActionEvent event) {

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
            detailTxtTitle.setText(bookmark.getTitle());
            detailTxtUrl.setText(bookmark.getUrl());
            detailTxtAdded.setText(bookmark.getAdded().toString());
            detailTxtDesc.setText(bookmark.getDesc());
            detailTxtEnv.setText(bookmark.getEnvironment().getName());
            detailTxtTags.setText(bookmark.getTags().stream()//get all tags of a bookmark as stream
                    .map(Tag::getTagString)//only the text of the tags in the list
                    .collect(Collectors.joining(" ")));//collect to a string delimited by a blank
            String googleFavIcon = "http://www.google.com/s2/favicons?domain_url=";//google api for loading favicon TODO: show favicon of url in the image/web view

        } else {//the bookmark is a null reference, so set all detail labels to blank
            detailTxtTitle.setText("");
            detailTxtUrl.setText("");
            detailTxtAdded.setText("");
            detailTxtDesc.setText("");
            detailTxtEnv.setText("");
            detailTxtTags.setText("");
        }
    }

    /**
     * Initialize Method is called after the fxml is loaded.
     */
    @FXML
    void initialize() {
        //binds filter results to the listview
        resultList.itemsProperty().bind(Bookmark.resultPropertyProperty());

        //sets the lookalike of a cell in the listview
        resultList.setCellFactory(this::cellFactoryList);

        //listener for filtering the bookmarks list with the given string
        searchTxtKeyWords.textProperty().addListener(Bookmark::filter);

        //shows the detail of the selected item
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

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
