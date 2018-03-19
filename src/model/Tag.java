package model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public String getTagString() {

        return tagString.get();
    }

    public SimpleStringProperty tagStringProperty() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString.set(tagString);
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

    public static ObservableList<Tag> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tagString=" + tagString +
                '}';
    }


    //TODO javadoc
    public static List<Tag> add(String text) {

        return Arrays
                .stream(text.toLowerCase().split(" "))//make lowercase and split by blank
                .distinct()//delete duplicates
                .map(string -> new Tag(-1, string))//maps to tags with the given Tagstring//TODO maybe can be shortened with one map, create new tag in the orelse part
                .map(tag -> tags.stream()
                        .filter(tag::equals)//filters for a tag with the same tagString
                        .findAny()//return any
                        .orElse(addAndReturn(tag)))//if optional is not present return the new created tag
                .collect(Collectors.toList());
    }

    //TODO javadoc
    private static Tag addAndReturn(Tag tag) {
        tags.add(tag);
        return tag;
    }


    @Override//TODO
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(tagString.get(), tag1.tagString.get());
    }

    @Override//TODO
    public int hashCode() {

        return Objects.hash(tagString.get());
    }
}
