package model;

import java.sql.SQLException;
import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import manage.Manager;

/**
 * The Datamodel for the Environment.
 * Instance Methodes/Attributes hold the functionality of a single Environment.
 * Class Methods/Attributes are utilitys and lists with all Environments.
 *
 * @author Dominik Strässle
 */
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
        //TODO the add can be called here and a constructor without an id is needed, the nextID method can also called here.
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

    /**
     * Removes the given environment from the database and the list.
     * Also removes the corresponding bookmarks
     *
     * @throws SQLException Delete went wrong
     */
    public static void delete() throws SQLException {
        if (Bookmark.currentEnvironmentProperty().get() == null) return;//check if the env is null

        Manager.getDatabaseController().consumerWrapper(Bookmark.getSelectedEnvironment(), Manager.getDatabaseController()::delete);//delete in the database
        environments.remove(Bookmark.getSelectedEnvironment());//remove in-memory

        refreshEnvironmentsResultsProperty();//everything will refresh, because the current environment is deleted
        Bookmark.refreshBookmarksResultsProperty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Environment)) return false;
        Environment that = (Environment) o;
        if (getId() == that.getId()) return true;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDesc(), that.getDesc()) &&
                Objects.equals(getColor(), that.getColor());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, desc, color);
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getId() {
        return id.get();
    }

    public static ObservableList<Environment> getEnvironmentsProperty() {
        return environmentsProperty.get();
    }

    public static SimpleListProperty<Environment> environmentsPropertyProperty() {
        return environmentsProperty;
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

    public void setColor(Color color) {
        this.color.set(color);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    /**
     * Handles the edit of a Environment
     *
     * @param oldEnvironment the environment without changes
     * @param newEnvironment the edited environment
     */
    public static void edit(Environment oldEnvironment, Environment newEnvironment) throws SQLException {
        //TODO modify in db
        //TODO modify in list
        Manager.getDatabaseController().consumerWrapper(//TODO method (reference)));
                //do the in memory changes
                oldEnvironment.setName(newEnvironment.getName());
        oldEnvironment.setDesc(newEnvironment.getDesc());
        oldEnvironment.setColor(newEnvironment.getColor());
    }
}
