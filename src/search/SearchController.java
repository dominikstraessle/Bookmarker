package search;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import manage.Manager;
import model.Bookmark;
import model.Environment;

public class SearchController {

    /**
     * Reference to the Manager that initialized the controller
     */
    private Manager manager;

    @FXML
    private ResourceBundle resources;


    @FXML
    private JFXTextField searchTxtKeyWords;

    @FXML
    private JFXComboBox<Environment> searchComboEnv;

    @FXML
    private JFXListView<Bookmark> resultList;

    @FXML
    private Region detailRegionIcon;

    @FXML
    private JFXTextField detailTxtTitle;

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
     * Is called to edit the selected Bookmark.
     *
     * @param event
     */
    @FXML
    void handleEditBookmark(ActionEvent event) {
        //nothing selected
        if (null == resultList.getSelectionModel().getSelectedItem()) {
            //show a warning
            Manager.alertWarning(resources.getString("error"), resources.getString("error.06"), resources.getString("error.06"), manager.getPrimaryStage());
            return;
        }
        try {
            //show the dialog
            manager.showEditBookmark(resultList.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            //failed to open the dialog
            Manager.alertException(resources.getString("error"), resources.getString("error.09"), manager.getPrimaryStage(), e);
        } finally {
            showBookmarkDetails(resultList.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void handleEditEnvironment(ActionEvent event) {
        if (null == searchComboEnv.getSelectionModel().getSelectedItem()) {
            //show a warning
            Manager.alertWarning(resources.getString("error"), resources.getString("error.11"), resources.getString("error.11"), manager.getPrimaryStage());
            return;
        }
        try {
            manager.showEditEnvironment(searchComboEnv.getSelectionModel().getSelectedItem());
        } catch (
                IOException e) {
            Manager.alertException(resources.getString("error"), resources.getString("error.09"), manager.getPrimaryStage(), e);
        } finally {
            showBookmarkDetails(resultList.getSelectionModel().getSelectedItem());
        }

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
            detailTxtTitle.textProperty().bind(bookmark.titleProperty());
            detailTxtUrl.textProperty().bind(bookmark.urlProperty());
            detailTxtAdded.textProperty().bind(bookmark.modifiedProperty().asString());
            detailTxtDesc.textProperty().bind(bookmark.descProperty());
            detailTxtEnv.textProperty().bind(bookmark.getEnvironment().nameProperty());
            detailTxtTags.textProperty().bind(bookmark.tagsAsStringProperty());
//            String googleFavIcon = "http://www.google.com/s2/favicons?domain_url=";//google api for loading favicon TODO: show favicon of url in the image/web view

            //A Property that contains the actual color of the environment.
            //bind the colorProperty and set the color
            SimpleObjectProperty<Color> color = new SimpleObjectProperty<>();
            color.bind(bookmark.environmentProperty().get().colorProperty());
            color.addListener((observable, oldValue, newValue) -> {
                detailRegionIcon.setBackground(new Background(
                        new BackgroundFill(
                                Paint.valueOf(newValue.toString()),
                                new CornerRadii(3),
                                new Insets(0))));
            });
            //set the color on the first show
            detailRegionIcon.setBackground(new Background(
                    new BackgroundFill(
                            Paint.valueOf(bookmark.getEnvironment().getColor().toString()),
                            new CornerRadii(3),
                            new Insets(0))));

        } else {//the bookmark is a null reference, so set all detail labels to blank
            detailTxtTitle.textProperty().unbind();
            detailTxtTitle.setText("");

            detailTxtUrl.textProperty().unbind();
            detailTxtUrl.setText("");

            detailTxtAdded.textProperty().unbind();
            detailTxtAdded.setText("");

            detailTxtDesc.textProperty().unbind();
            detailTxtDesc.setText("");

            detailTxtEnv.textProperty().unbind();
            detailTxtEnv.setText("");

            detailTxtTags.textProperty().unbind();
            detailTxtTags.setText("");

            //set background to nothing
            detailRegionIcon.setBackground(new Background(
                    new BackgroundFill(
                            Paint.valueOf(Color.TRANSPARENT.toString()),
                            new CornerRadii(3),
                            new Insets(0))));
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
                    //create new CellCard
                    setGraphic(new CellCard(bookmark, list));
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
        if (searchComboEnv.getSelectionModel().getSelectedItem() != null) {//check if one is selected
            try {
                Environment.delete();//deletes the currently selected environment
            } catch (SQLException exception) {
                //Error while deleteing
                Manager.alertException(resources.getString("error.08"), resources.getString("error.08"), manager.getPrimaryStage(), exception);
            }
        } else {
            //no environment selected
            Manager.alertWarning(resources.getString("error"), resources.getString("error.11"), resources.getString("error.11"), manager.getPrimaryStage());
        }
    }
}
