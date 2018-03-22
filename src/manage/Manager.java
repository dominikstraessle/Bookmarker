package manage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import add.AddBookmarkController;
import database.DatabaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bookmark;
import search.SearchController;

public class Manager extends Application {

    /**
     * DatabaseController should be used by every component.
     */
    private DatabaseController databaseController = new DatabaseController("res/data/data.sqlite");

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        showSearch();
    }

    //TODO: javadoc
    private void showSearch() throws IOException, SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("strings/lang");//for internationalization
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../search/search.fxml"), resourceBundle);
        Parent root = loader.load();//load fxml
        root.getStylesheets().add("stylesheets/style.css");//add stylesheet
        SearchController searchController = loader.getController();//init controller
        searchController.setManager(this);//reference manager
        loadData();//load everything from the database into the model
        this.primaryStage.getIcons().add(new Image("images/brand.png"));//add icon
        this.primaryStage.setTitle(resourceBundle.getString("bookmarker"));
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.show();
    }

    /**
     * Loads all data from the database into the ArrayLists.
     * Shows all Bookmarks in the @{@link SearchController}s Listview
     *
     * @throws SQLException Failed to load data
     */
    private void loadData() throws SQLException {
        databaseController.consumerWrapper(databaseController::readTags);
        databaseController.consumerWrapper(databaseController::readEnvironemnts);
        databaseController.consumerWrapper(databaseController::readBookmarks);
        Bookmark.showAllBookmarks();
        //TODO: should i move this method calls into the static constructor of Bookmark class?
    }

    //TODO javadoc
    private void writeData() throws SQLException {
        databaseController.consumerWrapper(databaseController::writeAll);
    }

    public static void main(String[] args) {
        launch(args);
    }


    public DatabaseController getDatabaseController() {
        return databaseController;
    }

    /**
     * Shows the Dialog to add a bookmark.
     */
    public void showAddBookmark() throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("strings/lang");//for internationalization
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../add/addBookmark.fxml"), resourceBundle);
        Parent parent = loader.load();
        parent.getStylesheets().add("stylesheets/style.css");
        AddBookmarkController controller = loader.getController();
        controller.setManager(this);
        Stage dialog = new Stage();
        controller.setDialogStage(dialog);
        dialog.setTitle(resourceBundle.getString("add"));
        dialog.getIcons().add(new Image("images/brand.png"));
        dialog.setScene(new Scene(parent));
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(this.primaryStage);
        dialog.showAndWait();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
