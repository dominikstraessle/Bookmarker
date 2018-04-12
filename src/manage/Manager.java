package manage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import add.AddBookmarkController;
import add.AddEnvironmentController;
import database.DatabaseController;
import edit.EditBookmarkController;
import edit.EditEnvironmentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bookmark;
import model.Environment;
import search.SearchController;
/*TODO Tasks
-X- tidy up every class
-X- Add Environment Functionality in Search and Add-View
-X- Platform.runLater(Runnable r); -> for the load data thread
-X- Support all Buttons in the search view and environment delete etc
- Color Env support
- Beautify the alertException
- LocalDateFormat... and change Added to -> Modified
- Icons support
- Threads to load / asynchronous loading/writing
- Tidy up Controllers (to much id's)
 */

/**
 * Manager stands for Managing everything that has to do with FXML, Database and the Application.
 *
 * @author Dominik Strässle
 */
public class Manager extends Application {

    /**
     * This DatabaseController should be used by every component.
     */
    private static DatabaseController databaseController = new DatabaseController("res/data/data.sqlite");
    /**
     * Ressource for internationalization.
     */
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("strings/lang");

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger("Bookmarker");

    /**
     * Primary Stage of the Application
     */
    private Stage primaryStage;

    /**
     * The start method is executed at the start of the application and loads the data from the database and the fxml files.
     *
     * @param primaryStage the primary stage
     * @throws Exception Error
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        //this will load the data from the database in a new Thread
//        new Thread(Manager::loadData).start();
        Platform.runLater(Manager::loadData);
        //load and show the search view
        showSearch();
    }

    /**
     * shows the standard screen for searching the bookmarks and view there details
     *
     * @throws IOException Loading of the fxml file went wrong
     */
    private void showSearch() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../search/search.fxml"), resourceBundle);
        Parent root = loader.load();//load fxml
        root.getStylesheets().add("stylesheets/style.css");//add stylesheet
        SearchController searchController = loader.getController();//init controller
        searchController.setManager(this);//reference manager
        this.primaryStage.getIcons().add(new Image("images/brand.png"));//add icon
        this.primaryStage.setTitle(resourceBundle.getString("bookmarker"));
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.show();
    }

    /**
     * Loads all data from the database into the ArrayLists.
     * Shows all Bookmarks in the @{@link SearchController}s Listview and Environemnts in the @{@link SearchController}s ComboBox
     */
    private static void loadData() {
        try {
            databaseController.consumerWrapper(databaseController::readTags);
            databaseController.consumerWrapper(databaseController::readEnvironemnts);
            databaseController.consumerWrapper(databaseController::readBookmarks);
        } catch (SQLException e) {
            Manager.log(Level.SEVERE, "Failed to load data", e);
        }
        Environment.refreshEnvironmentsResultsProperty();
        Bookmark.refreshBookmarksResultsProperty();
    }

    /**
     * Main Method
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates an Alert for Exceptions
     *
     * @param title     Title
     * @param header    Header
     * @param stage     Owner Stage
     * @param exception Exception
     */
    public static void alertException(String title, String header, Stage stage, Exception exception) {
        log(Level.SEVERE, header, exception);//logging the Exception
        String content = Arrays.toString(exception.getStackTrace()).replace(", ", "\n\t");
        alert(title, header, content, stage, Alert.AlertType.ERROR);
    }

    /**
     * Creates and shows an Alert
     *
     * @param title      Title
     * @param header     Header
     * @param content    Content
     * @param ownerStage Owner Stage
     * @param type       Alerttype of the Alert
     */
    private static void alert(String title, String header, String content, Stage ownerStage, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(ownerStage);
        alert.showAndWait();
    }

    /**
     * Creates an Alert for Warnings
     *
     * @param title   Title
     * @param header  Header
     * @param content Content
     * @param stage   Owner Stage
     */
    public static void alertWarning(String title, String header, String content, Stage stage) {
        log(Level.WARNING, header, content);
        alert(title, header, content, stage, Alert.AlertType.WARNING);
    }

    /**
     * Returns the Database controller
     *
     * @return The DatabaseController
     */
    public static DatabaseController getDatabaseController() {
        return databaseController;
    }

    /**
     * Shows the Dialog to add a bookmark.
     */
    public void showAddBookmark() throws IOException {
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

    /**
     * Returns the PrimaryStage
     *
     * @return the Application Stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Log a message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     *
     * @param level     One of the message level identifiers, e.g., SEVERE
     * @param title     The string message (or a key in the message catalog)
     * @param exception parameter to the message
     */
    public static void log(Level level, String title, Exception exception) {
        String stackTrace = Arrays.toString(exception.getStackTrace()).replace(", ", "\n\t");
        String logMessage = title + "\n\t" + stackTrace;
        LOGGER.log(level, logMessage, exception);
    }

    /**
     * Log a message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     *
     * @param level   One of the message level identifiers, e.g., SEVERE
     * @param title   The string message (or a key in the message catalog)
     * @param message parameter to the message
     */
    public static void log(Level level, String title, String message) {
        String logMessage = title + "\n\t" + message;
        LOGGER.log(level, logMessage, message);
    }

    /**
     * Shows the Dialog to add a environment from the primary stage.
     */
    public void showAddEnvironment() throws IOException {
        showAddEnvironment(primaryStage);
    }

    /**
     * Shows the Dialog to add a environment with a custom stage
     */
    public void showAddEnvironment(Stage customStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../add/addEnv.fxml"), resourceBundle);
        Parent parent = loader.load();
        parent.getStylesheets().add("stylesheets/style.css");
        AddEnvironmentController controller = loader.getController();
        Stage dialog = new Stage();
        controller.setDialogStage(dialog);
        dialog.setTitle(resourceBundle.getString("add"));
        dialog.getIcons().add(new Image("images/brand.png"));
        dialog.setScene(new Scene(parent));
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(customStage);
        dialog.showAndWait();
    }


    /**
     * Shows the Dialog to edit a environment from the primary stage.
     */
    public void showEditEnvironment(Environment oldEnvironment) throws IOException {
        showEditEnvironment(primaryStage, oldEnvironment);
    }

    /**
     * Shows the Dialog to edit a environment with a custom stage
     */
    public void showEditEnvironment(Stage customStage, Environment oldEnvironment) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../edit/editEnv.fxml"), resourceBundle);
        Parent parent = loader.load();
        parent.getStylesheets().add("stylesheets/style.css");
        EditEnvironmentController controller = loader.getController();
        Stage dialog = new Stage();
        controller.setDialogStage(dialog);
        controller.setEnvironment(oldEnvironment);
        dialog.setTitle(resourceBundle.getString("edit"));
        dialog.getIcons().add(new Image("images/brand.png"));
        dialog.setScene(new Scene(parent));
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(customStage);
        dialog.showAndWait();
    }

    /*
    Static constuctor for initializing the LOGGER.
    */
    static {
        try {//Initialize the Logger to write into log files instead of the console
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            FileHandler fileHandler = new FileHandler("C:\\Users\\stra5\\IdeaProjects\\Bookmarker\\res\\log\\logging_" + date + ".log", true);//handler for log file
//            FileHandler fileHandler = new FileHandler("C:\\Users\\domin\\OneDrive - SBL\\4 Semester\\120  Benutzerschnittstellen implementieren\\Bookmarker\\res\\log\\logging_" + date + ".log", true);//handler for log file
//            FileHandler fileHandler = new FileHandler("../res/logging_" + date + ".log", true);//handler for log file
            LOGGER.addHandler(fileHandler);//add handler
            fileHandler.setFormatter(new SimpleFormatter());//set Formatter
            LOGGER.setUseParentHandlers(false);//no console output anymore
            LOGGER.info("Logger initialized");//first message
        } catch (IOException e) {
            log(Level.SEVERE, "Error initializing the Filehandler for Logging", e);
        }
    }

    /**
     * Show the editBookmark dialog as dialog on the primary stage
     *
     * @param oldBookmark selected bookmark
     * @throws IOException cant load the fxml
     */
    public void showEditBookmark(Bookmark oldBookmark) throws IOException {
        showEditBookmark(primaryStage, oldBookmark);
    }

    /**
     * Show the editBookmark dialog on a given stage
     *
     * @param primaryStage Stage to use as owner
     * @param oldBookmark  selected bookmark
     * @throws IOException cant load the fxml
     */
    private void showEditBookmark(Stage primaryStage, Bookmark oldBookmark) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../edit/editBookmark.fxml"), resourceBundle);
        Parent parent = loader.load();
        parent.getStylesheets().add("stylesheets/style.css");
        EditBookmarkController controller = loader.getController();
        Stage dialog = new Stage();
        controller.setDialogStage(dialog);
        controller.setManager(this);
        controller.setBookmark(oldBookmark);
        dialog.setTitle(resourceBundle.getString("edit"));
        dialog.getIcons().add(new Image("images/brand.png"));
        dialog.setScene(new Scene(parent));
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(this.primaryStage);
        dialog.showAndWait();
    }
}
