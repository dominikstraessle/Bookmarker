package model;

import java.sql.SQLException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import manage.Manager;

public class Environment {

    /**
     * The list contains all Environments at runtime. Used for filtering.
     */
    private static ObservableList<Environment> environments = FXCollections.observableArrayList();
    /**
     * Property of {@link #environments}
     */
    private static SimpleListProperty<Environment> environmentsProperty = new SimpleListProperty<>();
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

//    /**
//     * Constructor with all required fields
//     *
//     * @param name  Name
//     * @param desc  Description
//     * @param color Color
//     */
//    public Environment(String name, String desc, Color color) {
//        this.name = new SimpleStringProperty(name);
//        this.desc = new SimpleStringProperty(desc);
//        this.color = new SimpleObjectProperty<>(color);
//    }

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
        return getName();
    }

    /**
     * Returns the next ID to use.
     *
     * @return Next ID
     */
    public static int getNewID() {
        return environments.stream()
                .mapToInt(Environment::getId)//map to all ID's
                .max()//get the highest ID
                .orElse(0) + 1;//add 1 to the highest id, if there is no ID at all, creates a new one from 0
    }

    /**
     * Sets the {@link #environments} list as the list of the {@link #environmentsProperty}.
     * Method is used after loading all Environments from the Database, to show all Environments in the @{@link search.SearchController}s ComboBox.
     */
    public static void refreshEnvironmentsResultsProperty() {
        Environment.environmentsProperty.set(Environment.environments);//after all Bookmarks are loaded, the property is updated the first time
    }

    public static ObservableList<Environment> getEnvironmentsProperty() {
        return environmentsProperty.get();
    }

    public static SimpleListProperty<Environment> environmentsPropertyProperty() {
        return environmentsProperty;
    }

    /**
     * Add a new Environment
     *
     * @param environment Environment to add
     */
    public static void add(Environment environment) throws SQLException {
        Manager.getDatabaseController().consumerWrapper(environment, Manager.getDatabaseController()::insert);
        environments.add(environment);
        environmentsProperty.set(environments);
    }
}
