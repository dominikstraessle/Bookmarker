package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;
import manage.Manager;
import model.Bookmark;
import model.Environment;
import model.Tag;

/**
 * Implementation of @{@link AbstractDatabaseController}.
 * Should be used for every Interaction with the Database.
 *
 * @author Dominik Strässle
 */
public class DatabaseController extends AbstractDatabaseController {

    /**
     * creates a new DatabaseController of the given file.
     *
     * @param filename Name of the database file.
     */
    public DatabaseController(String filename) {
        super(filename);
    }

    /**
     * Inserts a single @{@link Tag} into the database
     *
     * @param tag        Tag to insert
     * @param connection connection to the Database
     * @throws SQLException Insert went wrong
     */
    public void insert(Tag tag, Connection connection) throws SQLException {
        String SQL = "INSERT INTO tag(idtag,tag) VALUES(?,?)";
        PreparedStatement statement = connection.prepareStatement(SQL);
        //setting the parameters
        statement.setInt(1, tag.getId());
        statement.setString(2, tag.getTagString());
        //execute
        statement.executeUpdate();
    }

    /**
     * Inserts a single @{@link Environment} into the database
     *
     * @param environment Environment to insert
     * @param connection  connection to the Databse
     * @throws SQLException Insert went wrong
     */
    public void insert(Environment environment, Connection connection) throws SQLException {
        String SQL = "INSERT INTO environment(idenvironment, name, description, color) VALUES(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(SQL);
        //setting the parameters
        statement.setInt(1, environment.getId());
        statement.setString(2, environment.getName());
        statement.setString(3, environment.getDesc());
        statement.setString(4, environment.getColor().toString());
        //execute
        statement.executeUpdate();
    }

    /**
     * Inserts a single @{@link Bookmark} into the database
     *
     * @param bookmark   Bookmark to insert
     * @param connection connection to Database
     * @throws SQLException Insert went wrong
     */
    public void insert(Bookmark bookmark, Connection connection) throws SQLException {
        String SQL = "INSERT INTO bookmark(idbookmark, title, url, added, description, environment) VALUES(?,?,?,?,?,?);";
        PreparedStatement statement = connection.prepareStatement(SQL);
        //setting the parameters
        statement.setInt(1, bookmark.getId());
        statement.setString(2, bookmark.getTitle());
        statement.setString(3, bookmark.getUrl());
        statement.setDate(4, java.sql.Date.valueOf(bookmark.getAdded().toLocalDate()));
        statement.setString(5, bookmark.getDesc());
        statement.setInt(6, bookmark.getEnvironment().getId());
        //execute
        statement.executeUpdate();

        //the bookmark_has_tag table is an n-to-n cardinality, so they have to be inserted special
        bookmark.getTags().forEach(tag -> insert(bookmark, tag, connection));//insert all idtag/idbookmark references into bookmark_has_tag
    }

    /**
     * Inserts the @{@link Tag} and @{@link Bookmark} in the bookmark_has_tag table.
     * The @{@link SQLException} is catched, because this method is as consumer in foreach
     *
     * @param bookmark   Bookmark
     * @param tag        A Tag that corresponds to the bookmark
     * @param connection Connection
     */
    public void insert(Bookmark bookmark, Tag tag, Connection connection) {
        String SQL = "INSERT  INTO  bookmark_has_tag(idbookmark, idtag) VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            //setting the parameters
            statement.setInt(1, bookmark.getId());
            statement.setInt(2, tag.getId());
            //execute
            statement.executeUpdate();
        } catch (SQLException exception) {
            Manager.log(
                    Level.SEVERE,
                    "Failed to insert a bookmark_has_tag entry",
                    exception);
        }
    }
//    /**
//     * Inserts a List of Environments into the database
//     *
//     * @param environments Environments to insert
//     * @param connection   connection to Database
//     * @throws SQLException Insert went wrong
//     */
//    public void insertEnvironments(List<Environment> environments, Connection connection) throws SQLException {
//        StringBuilder insertString = new StringBuilder();
//        String SQL = "INSERT INTO environment(name, description, color) VALUES(%s,%s,%s);\n";
//        environments.forEach(environment -> {
//            insertString.append(String.format(SQL,
//                    environment.getName(),
//                    environment.getDesc(),
//                    environment.getColor()));
//        });
//        connection.createStatement().executeUpdate(insertString.toString());

//    }

//    /**
//     * Insert a list of Bookmarks into the database
//     *
//     * @param bookmarks  List of Bookmarks
//     * @param connection connection to Database
//     * @throws SQLException Insert went wrong
//     */
//    public void insertBookmarks(List<Bookmark> bookmarks, Connection connection) throws SQLException {
//        StringBuilder insertString = new StringBuilder();
//        String SQL = "INSERT INTO bookmark(title, url, added, description, environment) VALUES(%s,%s,%s,%s,%s);\n";
//        bookmarks.forEach(bookmark -> {
//            insertString.append(String.format(SQL,
//                    bookmark.getTitle(),
//                    bookmark.getUrl(),
//                    Date.from(bookmark.getAdded().atZone(ZoneId.systemDefault()).toInstant()).toString(),
//                    bookmark.getDesc(),
//                    bookmark.getEnvironment()));
//        });
//        connection.createStatement().executeUpdate(insertString.toString());
//    }

//    /**
//     * Insert a list of Tags into the database.
//     *
//     * @param tags       List of Tags
//     * @param connection Connection to the Database
//     * @throws SQLException Insert went wrong
//     */
//    public void insertTags(List<Tag> tags, Connection connection) throws SQLException {
//        StringBuilder insertString = new StringBuilder();
//        String SQL = "INSERT INTO tag(tag) VALUES(?);\n";
//        tags.forEach(tag -> insertString.append(SQL.replace("?", tag.getTagString())));
//        connection.createStatement().executeUpdate(insertString.toString());
//        //alternative would be: but then i need to handle a exception inside the lambda, own implementation is too much effort
//        //tags.forEach(tag -> insert(tag, connection ));
//    }

//    /**
//     * Selects all entries of a table
//     *
//     * @param table      Tablename
//     * @param connection Connection to the Database
//     * @return Results as ResultSet
//     * @throws SQLException Select went wrong
//     */
//    public ResultSet selectAll(String table, Connection connection) throws SQLException {
//        String SQL = "SELECT * FROM ?".replace("?", table);
//        return connection.createStatement().executeQuery(SQL);
//    }
//
//    /**
//     * Executes select from a given sql.
//     *
//     * @param SQL        SQL with select
//     * @param connection Connection to the Database
//     * @return Results as ResultSet
//     * @throws SQLException Select went wrong
//     */
//    public ResultSet selectOwnStatement(String SQL, Connection connection) throws SQLException {
//        return connection.createStatement().executeQuery(SQL);
//    }

    /**
     * Selects all @{@link Tag}s from the database into the @{@link Tag#tags} list.
     *
     * @throws SQLException Select went wrong
     */
    public void readTags(Connection connection) throws SQLException {
        //selects all from the table tag
        String SQL = "SELECT * FROM tag";
        ResultSet set = connection.createStatement().executeQuery(SQL);
        //clear all tags before inserting the loaded ones
        Tag.getTags().clear();

        //reads every row from the result set and adds a new Tag to the list
        while (set.next()) {
            int id = set.getInt("idtag");
            String tagSring = set.getString("tag");
            Tag.getTags().add(new Tag(id, tagSring));
        }
    }

    /**
     * Selects all @{@link Environment}s from the database into the @{@link Environment} environments list.
     *
     * @throws SQLException Select went wrong
     */
    public void readEnvironemnts(Connection connection) throws SQLException {
        //selects all from the table environment
        String SQL = "SELECT * FROM environment";
        ResultSet set = connection.createStatement().executeQuery(SQL);
        //clear all environments before inserting the loaded ones
        Environment.getEnvironments().clear();

        //reads every row from the result set and adds a new Environment to the list
        while (set.next()) {
            int id = set.getInt("idenvironment");
            String name = set.getString("name");
            String desc = set.getString("description");
            Color color = Color.valueOf(set.getString("color"));//Color is saved as hex
            Environment.getEnvironments().add(new Environment(id, name, desc, color));
        }
    }

    /**
     * Returns the Environment with the given id
     *
     * @param id id of the environment
     * @return Environment
     */
    private Environment getEnvironment(int id) {
        return Environment.getEnvironments().stream()
                .filter(environment -> environment.getId() == id)//only keep the one with the correct ID
                .findAny()//get the one
                .orElse(new Environment(0, "", "", Color.WHITE));//if the optional is empty, return a new Environment -> should not happen
    }

    /**
     * Returns a List of Tags that are in the bookmark_has_tag table with the given id
     *
     * @param id id of the Bookmark
     * @return List of tags
     */
    private List<Tag> getTags(int id, Connection connection) {
        String SQL = "SELECT idtag FROM bookmark_has_tag WHERE bookmark_has_tag.idbookmark = ?";//SQL
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            //set parameters
            statement.setInt(1, id);
            //execute
            ResultSet setTags = statement.executeQuery();//execute
            ArrayList<Integer> tagIDs = new ArrayList<>();
            while (setTags.next()) {
                tagIDs.add(setTags.getInt("idtag"));
            }
            return tagIDs.stream()//stream of all ID's
                    .map(integer -> Tag.getTags().stream()//map to the Tag Object with the same ID
                            .filter(tag -> tag.getId() == integer)
                            .findFirst().orElse(new Tag(0, "")))
                    .collect(Collectors.toList());//collect to a List
        } catch (SQLException e) {
            Manager.log(Level.SEVERE, "Failed to load the Tags of an Bookmark", e);
        }
        return new ArrayList<>();//returns empty list when the select went wrong
    }

    /**
     * Reads all bookmarks and initializes all bookmarks in th @{@link Bookmark} list.
     *
     * @throws SQLException Select went wrong
     */
    public void readBookmarks(Connection connection) throws SQLException {
        //Selects all from the table bookmark
        String SQL = "SELECT * FROM bookmark";
        ResultSet set = connection.createStatement().executeQuery(SQL);

        //reads every row to initialize all bookmarks
        readAndSetBookmarks(SQL, connection);
    }

    /**
     * Reads all bookmarks and initializes all bookmarks in th @{@link Bookmark} list.
     * But only the Bookmarks of a specific Environment
     *
     * @param environment Selected Environment
     * @param connection  connection to database
     * @throws SQLException Select went wrong
     */
    public void readBookmarks(Environment environment, Connection connection) throws SQLException {
        //Selects all from the table bookmark
        String SQL = "SELECT * FROM bookmark where environment = " + environment.getId();

        //reads every row to initialize all bookmarks
        readAndSetBookmarks(SQL, connection);
    }


    private void readAndSetBookmarks(String SQL, Connection connection) throws SQLException {
        ResultSet set = connection.createStatement().executeQuery(SQL);
        Bookmark.getBookmarks().clear();
        while (set.next()) {
            int id = set.getInt("idbookmark");
            String title = set.getString("title");
            String url = set.getString("url");//URL is saved as string
            String desc = set.getString("description");
            LocalDateTime added = set.getDate("added").toLocalDate().atStartOfDay();//Creates a LocalDateTime from a Date.
            //adds a new Bookmark to the list
            Bookmark.getBookmarks().add(new Bookmark(id, desc, title, url, added, getEnvironment(set.getInt("environment"))));
        }
        //Adds all correasponding Tags
        // -> this cant be done above in the constuctor, because only one ResultSet can be open at time
        Bookmark.getBookmarks().
                forEach(bookmark -> bookmark.addTags(getTags(bookmark.getId(), connection)));
    }


}



