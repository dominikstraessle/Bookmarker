package model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import manage.Manager;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * The Datamodel for the Bookmark.
 * Instance Methodes/Attributes hold the functionality of a singel Bookmark.
 * Class Methods/Attributes are utilitys and lists containing filter results and all current bookmarks.
 *
 * @author Dominik Strässle
 */
public class Bookmark {

    /**
     * List sorted by value of matches with the filter string
     */
    private static ObservableList<Bookmark> results = FXCollections.observableArrayList();
    /**
     * Property of {@link #results}
     */
    private static SimpleListProperty<Bookmark> resultProperty = new SimpleListProperty<>();

    /**
     * This List contains all bookmarks at the runtime, it is used for filtering.
     */
    private static ObservableList<Bookmark> bookmarks = FXCollections.observableArrayList();
    /**
     * This is everytime the actual filterString.
     */
    private static SimpleStringProperty filterString = new SimpleStringProperty("");
    /**
     * This is the actually selected Environment
     */
    private static SimpleObjectProperty<Environment> selectedEnvironment = new SimpleObjectProperty<>();
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
    private SimpleStringProperty url;
    /**
     * Date and Time when the bookmark was modified
     */
    private SimpleObjectProperty<LocalDateTime> modified;
    /**
     * The correspondig environment
     */
    private SimpleObjectProperty<Environment> environment;
    /**
     * All corresponding tags
     */
//    private ObservableList<Tag> tags = FXCollections.observableArrayList();

    private SimpleListProperty<Tag> tags = new SimpleListProperty<>();

    /**
     * Constuctor with all fields
     *
     * @param id          ID
     * @param desc        Description
     * @param title       Title
     * @param url         Url
     * @param modified    Date and time when the bookmark was modified
     * @param environment corresponding environment
     * @param tags        All corresponding tags
     */
    public Bookmark(int id, String desc, String title, String url, LocalDateTime modified, Environment environment, Collection<? extends Tag> tags) {
        this.id = new SimpleIntegerProperty(id);
        this.desc = new SimpleStringProperty(desc);
        this.title = new SimpleStringProperty(title);
        this.url = new SimpleStringProperty(url);
        this.modified = new SimpleObjectProperty<>(modified);
        this.environment = new SimpleObjectProperty<>(environment);
//        this.tags.addAll(tags);
        this.tags = new SimpleListProperty<>(FXCollections.observableArrayList(tags));
    }

    /**
     * Constuctor with all fields
     *
     * @param id          ID
     * @param desc        Description
     * @param title       Title
     * @param url         Url
     * @param modified    Date and time when the bookmark was modified
     * @param environment corresponding environment
     */
    public Bookmark(int id, String desc, String title, String url, LocalDateTime modified, Environment environment) {
        this.id = new SimpleIntegerProperty(id);
        this.desc = new SimpleStringProperty(desc);
        this.title = new SimpleStringProperty(title);
        this.url = new SimpleStringProperty(url);
        this.modified = new SimpleObjectProperty<>(modified);
        this.environment = new SimpleObjectProperty<>(environment);
    }

    /**
     * Adds a list of Tags.
     *
     * @param tags List of Tags
     */
    public void addTags(Collection<? extends Tag> tags) {
        //if the list is null create a new one -> lazy instantiation
        if (this.tags.get() == null) this.tags.set(FXCollections.observableArrayList());
        this.tags.addAll(tags);
    }


    /**
     * Filters the {@link #bookmarks} List with the search keywords.
     * Collects the results to {@link #results} and removes all entrys with no filter match
     *
     * @param observable observable
     */
    public static void filter(Observable observable) {

        //Creates a ArrayList with all search keywords -> they are splitted by a blank.
        //converts the string to lowercase before
        ArrayList<String> filterCriteria = new ArrayList<>(Arrays
                .asList(getFilterString()//the new filter string
                        .toLowerCase()//make everything lowercase
                        .split(" ")));//splits by blank

        //adds all bookmarks with the corresponding number of matches with the filter strings to a Map | bookmarks with no matches already removed.
        Bookmark.results = FXCollections.observableArrayList(Bookmark.bookmarks.stream()//stream of all bookmarks
                .collect(toMap(Bookmark::getMe, bookmark -> filterCriteria.stream()//Maps the values to a key<Bookmark> with the value of matches<Integer>
                        .mapToInt(bookmark::countMatches)//counts the matches for every filter String
                        .sum()))//summarize the value of all matches
                .entrySet().stream()//new Stream of the entry set
                .filter(bookmarkIntegerEntry -> bookmarkIntegerEntry.getValue() != 0)//removes all Values with no matches
                .sorted(Comparator.<Map.Entry<Bookmark, Integer>>comparingInt(Map.Entry::getValue).reversed())//sorts the map, the most matches will be at the top(reversed)
                .map(Map.Entry::getKey)//maps the Map to an list containing the key
                .collect(toList()));//Collecting to a list

        //update the resultProperty with the new results
        Bookmark.resultProperty.set(Bookmark.results);//refresh the resultproperty
    }

    /**
     * Counts the matches with a given filter String
     *
     * @param filter filter String
     * @return count of the matches with the filter String.
     */
    public int countMatches(String filter) {
        int count = 0;

        if (getTitle().toLowerCase().contains(filter)) count += 1;//title
        if (getDesc().toLowerCase().contains(filter)) count += 1;//description
        if (getEnvironment().getDesc().toLowerCase().contains(filter)) count += 1;//env description
        if (getEnvironment().getName().toLowerCase().contains(filter)) count += 1;//env name

        //count the number of matching tags
        count += getTags().stream()//tags
                .filter(tag -> tag.getTagString().toLowerCase().contains(filter))//only keeps the tags with a match
                .count();//counts the number of matches

        return count;
    }


    /**
     * Sets the {@link #bookmarks} list as the list of the {@link #resultProperty}.
     * Method is used after loading all Bookmarks from the Database, to show all Bookmarks in the @{@link search.SearchController}s Listview.
     */
    public static void refreshBookmarksResultsProperty() {
        Bookmark.resultProperty.set(bookmarks);//after all Bookmarks are loaded, the property is updated the first time
        filterStringProperty().set("");
    }

    /**
     * Used for adding a new Bookmark to the list of Bookmarks
     *
     * @param bookmark bookmark to add
     */
    public static void add(Bookmark bookmark) throws SQLException {
        Manager.getDatabaseController().consumerWrapper(bookmark, Manager.getDatabaseController()::insert);
        bookmarks.add(bookmark);
    }

    /**
     * Returns the next ID to use.
     *
     * @return Next ID
     */
    public static int getNewID() {
        return bookmarks.stream()
                .mapToInt(Bookmark::getId)//map to all ID's
                .max()//get the highest ID
                .orElse(0) + 1;//add 1 to the highest id, if there is no ID at all, creates a new one from 0
    }

    /**
     * When the Environment changes, all corresponding Bookmarks will be loaded into the {@link #bookmarks} list.
     *
     * @param observable observable
     * @param oldValue   old value
     * @param newValue   new Selected Environment
     */
    public static void changeEnvironment(ObservableValue<? extends Environment> observable, Environment oldValue, Environment newValue) {
        try {
            if (newValue == null) {
                Manager.getDatabaseController().consumerWrapper(Manager.getDatabaseController()::readBookmarks);//read all Bookmarks
            } else {
                Manager.getDatabaseController().consumerWrapper(newValue, Manager.getDatabaseController()::readBookmarks);//read only the corresponding bookmarks
            }
        } catch (SQLException e) {
            Manager.log(Level.SEVERE, "Failed to load Bookmarks from database", e);
        }
        //refresh the bookmarks
        refreshBookmarksResultsProperty();
    }


    /**
     * Delete the given Bookmark from the list and database
     */
    public static void deleteBookmark(Bookmark bookmark) throws SQLException {
        bookmarks.remove(bookmark);//delete from the list
        Manager.getDatabaseController().consumerWrapper(bookmark, Manager.getDatabaseController()::delete);//delete from the database
        refreshBookmarksResultsProperty();
    }


    public void setUrl(String url) {
        this.url.set(url);
    }

    /**
     * Edit a given bookmark with the new values
     *
     * @param oldBookmark the one to edit
     * @param newBookmark the one with the new values
     * @throws SQLException Update went wrong
     */
    public static void edit(Bookmark oldBookmark, Bookmark newBookmark) throws SQLException {
        //on the db side
        Manager.getDatabaseController().consumerWrapper(newBookmark, Manager.getDatabaseController()::edit);

        //set the new values
        oldBookmark.setTitle(newBookmark.getTitle());
        oldBookmark.setDesc(newBookmark.getDesc());
        oldBookmark.setUrl(newBookmark.getUrl());
        oldBookmark.setModified(newBookmark.getModified());
        oldBookmark.setTags(newBookmark.getTags());
    }

    /**
     * returns refernce of the object.
     *
     * @return this
     */
    private Bookmark getMe() {
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bookmark)) return false;
        Bookmark bookmark = (Bookmark) o;
        if (getId() == bookmark.getId()) return true;
        return Objects.equals(getDesc(), bookmark.getDesc()) &&
                Objects.equals(getTitle(), bookmark.getTitle()) &&
                Objects.equals(getUrl(), bookmark.getUrl()) &&
                Objects.equals(getModified(), bookmark.getModified()) &&
                Objects.equals(getEnvironment(), bookmark.getEnvironment()) &&
                Objects.equals(getTags(), bookmark.getTags());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, desc, title, url, modified, environment, tags);
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", desc=" + desc +
                ", title=" + title +
                ", url=" + url +
                ", modified=" + modified +
                ", environment=" + environment +
                ", tags=" + tags +
                '}';
    }

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public static ObservableList<Bookmark> getResults() {
        return results;
    }

    public ObservableList<Tag> getTags() {
        return tags;
    }

    public static String getFilterString() {
        return filterString.get();
    }

    public static SimpleStringProperty filterStringProperty() {
        return filterString;
    }

    public static Environment getSelectedEnvironment() {
        return selectedEnvironment.get();
    }

    public static SimpleObjectProperty<Environment> currentEnvironmentProperty() {
        return selectedEnvironment;
    }

    public SimpleListProperty<Tag> tagsProperty() {
        return tags;
    }

    public void setTags(ObservableList<Tag> tags) {
        this.tags.set(tags);
    }

    public static ObservableList<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public static void setBookmarks(ObservableList<Bookmark> bookmarks) {
        Bookmark.bookmarks = bookmarks;
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


    public LocalDateTime getModified() {
        return modified.get();
    }

    public SimpleObjectProperty<LocalDateTime> modifiedProperty() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified.set(modified);
    }

    public Environment getEnvironment() {
        return environment.get();
    }

    public SimpleObjectProperty<Environment> environmentProperty() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment.set(environment);
    }

    public static ObservableList<Bookmark> getResultProperty() {
        return resultProperty.get();
    }

    public static SimpleListProperty<Bookmark> resultPropertyProperty() {
        return resultProperty;
    }

    static {
        //listener for filtering the bookmarks list with the given string
        filterStringProperty().addListener(Bookmark::filter);
        //When a new Environment is selected, then the search list will only be such elements, where the environment equals the selected environment
        currentEnvironmentProperty().addListener(Bookmark::changeEnvironment);
    }


}
