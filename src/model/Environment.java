package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class Environment {

    /**
     * The list contains all Environments at runtime. Used for filtering.
     */
    private static ObservableList<Environment> environments = FXCollections.observableArrayList();
    /**
     * ID
     */
    private SimpleIntegerProperty id;
    /**
     * Name
     */
    private SimpleStringProperty name;
    /**
     * Description
     */
    private SimpleStringProperty desc;
    /**
     * Color
     */
    private ObjectProperty<Color> color;

//    /**
//     * Constructor with all required fields
//     *
//     * @param name Name
//     * @param desc Description
//     */
//    public Environment(String name, String desc) {
//        this.name = new SimpleStringProperty(name);
//        this.desc = new SimpleStringProperty(desc);
//    }

    /**
     * Constructor with all fields.
     *
     * @param id    ID
     * @param name  Name
     * @param desc  Description
     * @param color Color
     */
    public Environment(int id, String name, String desc, Color color) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.desc = new SimpleStringProperty(desc);
        this.color = new SimpleObjectProperty<>(color);
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public static ObservableList<Environment> getEnvironments() {
        return environments;
    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "id=" + id +
                ", name=" + name +
                ", desc=" + desc +
                ", color=" + color +
                '}';
    }
}
