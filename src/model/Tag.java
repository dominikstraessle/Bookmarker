package model;

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
    private SimpleStringProperty tag;
//
//    /**
//     * Constuctor with all required fields.
//     *
//     * @param tag Tag
//     */
//    public Tag(String tag) {
//
//        this.tag = new SimpleStringProperty(tag);
//    }

    /**
     * Construcotr with all fields.
     *
     * @param id  ID
     * @param tag Tag
     */
    public Tag(int id, String tag) {
        this.id = new SimpleIntegerProperty(id);
        this.tag = new SimpleStringProperty(tag);
    }

    public String getTag() {

        return tag.get();
    }

    public SimpleStringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
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
                ", tag=" + tag +
                '}';
    }
}
