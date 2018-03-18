package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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
     * List with all filter strings.
     */
    private static ArrayList<String> filterCriteria;

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
     * Date and Time when the bookmark was added
     */
    private SimpleObjectProperty<LocalDateTime> added;
    /**
     * The correspondig environment
     */
    private SimpleObjectProperty<Environment> environment;
    /**
     * All corresponding tags
     */
    private ObservableList<Tag> tags = FXCollections.observableArrayList();

    /**
     * Constuctor with all fields
     *
     * @param id          ID
     * @param desc        Description
     * @param title       Title
     * @param url         Url
     * @param added       Date and time when the bookmark was added
     * @param environment corresponding environment
     * @param tags        All corresponding tags
     */
    public Bookmark(int id, String desc, String title, String url, LocalDateTime added, Environment environment, Collection<? extends Tag> tags) {
        this.id = new SimpleIntegerProperty(id);
        this.desc = new SimpleStringProperty(desc);
        this.title = new SimpleStringProperty(title);
        this.url = new SimpleStringProperty(url);
        this.added = new SimpleObjectProperty<>(added);
        this.environment = new SimpleObjectProperty<>(environment);
        this.tags.addAll(tags);
    }

    /**
     * Constuctor with all fields
     *
     * @param id          ID
     * @param desc        Description
     * @param title       Title
     * @param url         Url
     * @param added       Date and time when the bookmark was added
     * @param environment corresponding environment
     */
    public Bookmark(int id, String desc, String title, String url, LocalDateTime added, Environment environment) {
        this.id = new SimpleIntegerProperty(id);
        this.desc = new SimpleStringProperty(desc);
        this.title = new SimpleStringProperty(title);
        this.url = new SimpleStringProperty(url);
        this.added = new SimpleObjectProperty<>(added);
        this.environment = new SimpleObjectProperty<>(environment);
    }


    /**
     * Filters the {@link #bookmarks} List with the search keywords.
     * Collects the results to {@link #results} and removes all entrys with no filter match
     *
     * @param newValue Filter String
     */
    public static void filter(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.length() > 100) return;//TODO: do i require this?
        //only lowercase
        String searchString = newValue.toLowerCase();
        //Creates a ArrayList with all search keywords -> they are splitted by a blank.
        Bookmark.filterCriteria = new ArrayList<>(Arrays.asList(searchString.split(" ")));
        //adds all bookmarks with the corresponding number of matches with the filter strings to a Map | bookmarks with no matches already removed.
        Bookmark.results = FXCollections.observableArrayList(Bookmark.bookmarks.stream()//stream of all bookmarks
                .collect(toMap(Bookmark::getMe, Bookmark::filterBookmark))//Maps the values to a key<Bookmark> with the value of matches<Integer>
                .entrySet().stream()//new Stream of the entry set
                .filter(bookmarkIntegerEntry -> bookmarkIntegerEntry.getValue() != 0)//removes all Values with no matches
                .sorted(Comparator.<Map.Entry<Bookmark, Integer>>comparingInt(Map.Entry::getValue).reversed())//sorts the map, the most matches will be at the top(reversed)
                .map(Map.Entry::getKey)//maps the Map to an list
                .collect(Collectors.toList()));//Collecting to a list
//        System.out.println(results.toString());
        resultProperty.set(results);//refresh the resultproperty
    }

    /**
     * Returns the value of matches of the {@link #filterCriteria} Strings with the Object attributs
     *
     * @param bookmark Object to filter
     * @return Value of matches
     */
    public static Integer filterBookmark(Bookmark bookmark) {
        int count = 0;

        //count the matches with every filter keyword
        //TODO: Problem with NullPointer...?
        for (String filter : Bookmark.filterCriteria) {
            if (bookmark.getTitle().toLowerCase().contains(filter)) count += 1;
            if (bookmark.getDesc().toLowerCase().contains(filter)) count += 1;
            if (bookmark.getEnvironment().getDesc().toLowerCase().contains(filter)) count += 1;
            if (bookmark.getEnvironment().getName().toLowerCase().contains(filter)) count += 1;
//            for (Tag tag : bookmark.getTags()) {
//                if (tag.getTag().toLowerCase().contains(filter)) count += 1;
//            }//Replaced by the construct down here...
            count += bookmark.getTags().stream()
                    .filter(tag -> tag.getTag().toLowerCase().contains(filter))
                    .count();
        }

        return count;
    }

    /**
     * Adds a list of Tags.
     *
     * @param tags List of Tags
     */
    public void addTags(Collection<? extends Tag> tags) {
        this.tags.addAll(tags);
    }


    /**
     * returns refernce of the object.
     *
     * @return this
     */
    private Bookmark getMe() {
        return this;
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

    public void setTags(ObservableList<Tag> tags) {
        this.tags = tags;
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


    public LocalDateTime getAdded() {
        return added.get();
    }

    public SimpleObjectProperty<LocalDateTime> addedProperty() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added.set(added);
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

    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", desc=" + desc +
                ", title=" + title +
                ", url=" + url +
                ", added=" + added +
                ", environment=" + environment +
                ", tags=" + tags +
                '}';
    }

    public static ObservableList<Bookmark> getResultProperty() {
        return resultProperty.get();
    }

    public static SimpleListProperty<Bookmark> resultPropertyProperty() {
        return resultProperty;
    }


    /**
     * Sets the {@link #bookmarks} list as the list of the {@link #resultProperty}.
     * Method is used after loading all Bookmarks from the Database, to show all Bookmarks in the @{@link search.SearchController}s Listview.
     */
    public static void showAllBookmarks() {
        Bookmark.resultProperty.set(bookmarks);//after all Bookmarks are loaded, the property is updated the first time
    }
}
