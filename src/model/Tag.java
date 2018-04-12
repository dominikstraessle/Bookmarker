package model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import manage.Manager;

/**
 * The Datamodel for the Tag.
 * Instance Methodes/Attributes hold the functionality of a single Tag.
 * Class Methods/Attributes are utilitys and lists with all Tags.
 *
 * @author Dominik Strässle
 */
public class Tag {
    /**
     * Contains all Tags at runtime
     */
    private static ObservableList<Tag> tags = FXCollections.observableArrayList();
    /**
     * ID
     */
    private SimpleIntegerProperty id;
    /**
     * Tag. String of the Tag.
     */
    private SimpleStringProperty tagString;


    /**
     * Construcotr with all fields.
     *
     * @param id        ID
     * @param tagString Tag
     */
    public Tag(int id, String tagString) {
        this.id = new SimpleIntegerProperty(id);
        this.tagString = new SimpleStringProperty(tagString);
    }


    /**
     * Used for adding new Tags of a new Bookmark.
     * 1. String with all Tags blank separeted given as #text
     * 2. The String is splitted and converted to lowercase
     * 3. All duplicates are removed
     * 4. Creates new Tag of every String
     * 5. Checks if already a Tag with the same String exists and adds this to the List, else the new one will be added.
     *
     * @param text String with all Tags blank separeted.
     * @return List with all references to the tags.
     */
    public static List<Tag> add(String text) {
        return Arrays
                .stream(text.toLowerCase().split(" "))//make lowercase and split by blank
                .distinct()//delete duplicates
                .map(string -> new Tag(Tag.getNewId(), string))//maps to tags with the given Tagstring !!--->>>> maybe can be shortened with one map, create new tag in the orelse part
                .map(tag -> tags.stream()
                        .filter(tag::equals)//filters for a tag with the same tagString
                        .findAny()//return any
                        .orElse(addAndReturn(tag)))//if optional is not present return the new created tag
                .collect(Collectors.toList());
    }


    /**
     * Adds the given Tag to the {@link #tags} list and returns it.
     *
     * @param tag Tag to add
     * @return same like #tag
     */
    private static Tag addAndReturn(Tag tag) {
        try {
            Manager.getDatabaseController().consumerWrapper(tag, Manager.getDatabaseController()::insert);
        } catch (SQLException e) {
            Manager.log(Level.SEVERE, "Failed to insert a Tag", e);
        }
        tags.add(tag);
        return tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag1 = (Tag) o;
        if (getId() == tag1.getId()) return true;
        return Objects.equals(getTagString(), tag1.getTagString());//compares the strings of the properties
    }

    @Override
    public int hashCode() {

        return Objects.hash(tagString.get());
    }

    /**
     * Returns the next ID to use.
     *
     * @return Next ID.
     */
    public static int getNewId() {
        return tags.stream()
                .mapToInt(Tag::getId)//map to all ID's
                .max()//get the highest ID
                .orElse(0) + 1;//add 1 to the highest id, if there is no ID at all, creates a new one from 0
    }

    @Override
    public String toString() {
        return getTagString();
    }

    public String getTagString() {

        return tagString.get();
    }


    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public static ObservableList<Tag> getTags() {
        return tags;
    }
}
