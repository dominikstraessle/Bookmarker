package model;

import java.net.URL;
import java.time.LocalDateTime;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Bookmark {

    /**
     * This List contains all bookmarks at the runtime, it is used for filtering.
     */
    private static ObservableList<Bookmark> bookmarks = FXCollections.observableArrayList();

    /**
     * ID
     */
    private SimpleIntegerProperty id;
    /**
     * Description
     */
    private SimpleStringProperty desc;
    /**
     * Title
     */
    private SimpleStringProperty title;
    /**
     * URL
     */
    //TODO: ObjectProperty<URL> ...
    private URL url;
    /**
     * Date and Time when the bookmark was added
     */
    private LocalDateTime added;
    /**
     * The correspondig environment
     */
    private Environment environment;

    /**
     * Creates a Bookmark with all required fields
     *
     * @param id    ID
     * @param title Title
     * @param url   URL
     */
    public Bookmark(SimpleIntegerProperty id, SimpleStringProperty title, URL url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    /**
     * Creates a Bookmark with all fields
     *
     * @param id          ID
     * @param desc        Description
     * @param title       Title
     * @param url         URL
     * @param added       Date and time when added
     * @param environment corresponding environment
     */
    public Bookmark(SimpleIntegerProperty id, SimpleStringProperty desc, SimpleStringProperty title, URL url, LocalDateTime added, Environment environment) {
        this.id = id;
        this.desc = desc;
        this.title = title;
        this.url = url;
        this.added = added;
        this.environment = environment;
    }

    /**
     * Adds a bookmark to the bookmarks list.
     *
     * @param bookmark Bookmark
     */
    public static void addBookmark(Bookmark bookmark) {
        Bookmark.bookmarks.add(bookmark);
    }

    /**
     * Adds a list of bookmarks to the bookmarks list.
     *
     * @param bookmarks List of bookmarks
     */
    public static void addAllBookmarks(Bookmark... bookmarks) {
        Bookmark.bookmarks.addAll(bookmarks);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDesc() {
        return desc.get();
    }

    public SimpleStringProperty descProperty() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
