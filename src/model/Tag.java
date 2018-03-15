package model;

import javafx.beans.property.SimpleStringProperty;

public class Tag {

    private SimpleStringProperty tag;


    public Tag(SimpleStringProperty tag) {

        this.tag = tag;
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
}
