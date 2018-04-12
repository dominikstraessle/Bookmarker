package search;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import manage.Manager;
import model.Bookmark;
import model.Environment;
import model.Tag;

public class SearchController {

    /**
     * Reference to the Manager that initialized the controller
     */
    private Manager manager;

    @FXML
    public MenuItem menuDeleteEnvironment;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchTxtKeyWords;

    @FXML
    private JFXComboBox<Environment> searchComboEnv;

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
     *
     * @param event Add-Button clicked.
     */
    @FXML
    void handleAddBookmark(ActionEvent event) {
        try {
            manager.showAddBookmark();
        } catch (IOException e) {
            Manager.alertException(resources.getString("error"), resources.getString("error.01"), manager.getPrimaryStage(), e);
        }
    }

    /**
     * Add a environment
     *
     * @param event
     */
    @FXML
    void handleAddEnv(ActionEvent event) {
        try {
            manager.showAddEnvironment();
        } catch (IOException e) {
            Manager.alertException(resources.getString("error"), resources.getString("error.01"), manager.getPrimaryStage(), e);
        }
    }

    @FXML
    void handleCitate(ActionEvent event) {

    }

    /**
     * Copy the current displayed url in the systems clipboard
     *
     * @param event button clicked
     */
    @FXML
    void handleCopy(ActionEvent event) {
        if (!detailTxtUrl.getText().equals("")) {//make sure the link is not empty
            ClipboardContent content = new ClipboardContent();
            //put the string into the content
            content.putString(detailTxtUrl.getText());
            //copy the content to clipboard
            Clipboard.getSystemClipboard().setContent(content);
        } else {
            //invalid link
            Manager.alertWarning(resources.getString("error.07"), resources.getString("error.05"), resources.getString("error.06"), manager.getPrimaryStage());
        }
    }

    /**
     * This deletes the currently selected Bookmark from the list and database
     *
     * @param event button clicked
     */
    @FXML
    void handleDelete(ActionEvent event) {
        if (resultList.getSelectionModel().getSelectedItem() != null) {
            try {
                Bookmark.deleteBookmark(resultList.getSelectionModel().getSelectedItem());
            } catch (SQLException exception) {
                //Error while deleteing
                Manager.alertException(resources.getString("error.08"), resources.getString("error.08"), manager.getPrimaryStage(), exception);
            }
        } else {
            //no bookmark selected
            Manager.alertWarning(resources.getString("error.06"), resources.getString("error.06"), resources.getString("error.06"), manager.getPrimaryStage());
        }
    }

    /**
     * Opens the current displayed url in the systems standard browser.
     *
     * @param event button clicked
     */
    @FXML
    void handleOpen(ActionEvent event) {
        try {
            if (!detailTxtUrl.getText().equals("")) {//make sure the link is not empty
                HostServices services = this.manager.getHostServices();//host service to call the browser
                services.showDocument(detailTxtUrl.getText());//open in browser
            } else {
                //invalid link
                Manager.alertWarning(resources.getString("error.03"), resources.getString("error.05"), resources.getString("error.06"), manager.getPrimaryStage());
            }
        } catch (Exception exception) {
            //unable to open the browser
            Manager.alertException(resources.getString("error.03"), resources.getString("error.04"), manager.getPrimaryStage(), exception);
        }
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

        //Binding of the filter string with the @Bookmark.filterStringProperty
        // -> used for filter the the list with the given keywords
        searchTxtKeyWords.textProperty().bindBidirectional(Bookmark.filterStringProperty());

        //shows the detail of the selected item
        resultList.getSelectionModel()
                .selectedItemProperty()//get the selected item
                .addListener((observable, oldValue, newValue) -> showBookmarkDetails(newValue));//show the details of the selected item.
        //binds the environments to the searchComboBox
        searchComboEnv.itemsProperty().bind(Environment.environmentsPropertyProperty());
        //When a new Environment is selected, then the search list will only be such elements, where the environment equals the selected environment
        Bookmark.currentEnvironmentProperty().bind(searchComboEnv.getSelectionModel().selectedItemProperty());
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
                    //https://www.billmann.de/2013/07/03/javafx-custom-listcell/
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

    @FXML
    public void handleReset(ActionEvent actionEvent) {
        searchComboEnv.getSelectionModel().clearSelection();
    }

    /**
     * Delete the currently selected environment from the list and database
     *
     * @param actionEvent button clicked
     */
    @FXML
    public void handleDeleteEnvironment(ActionEvent actionEvent) {
        if (searchComboEnv.getSelectionModel().selectedItemProperty().get() != null) {//check if one is selected
            try {
                Environment.delete();//deletes the currently selected environment
            } catch (SQLException exception) {
                //Error while deleteing
                Manager.alertException(resources.getString("error.08"), resources.getString("error.08"), manager.getPrimaryStage(), exception);
            }
        } else {
            //no environment selected
            Manager.alertWarning(resources.getString("error.06"), resources.getString("error.06"), resources.getString("error.06"), manager.getPrimaryStage());
        }
    }
}
