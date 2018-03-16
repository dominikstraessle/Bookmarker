package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;
import model.Bookmark;
import model.Environment;
import model.Tag;

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
     * Inserts a single tag into the database
     *
     * @param tag        Tag to insert
     * @param connection connection to the Database
     * @throws SQLException Insert went wrong
     */
    public void insert(Tag tag, Connection connection) throws SQLException {
        String SQL = "INSERT INTO tag(tag) VALUES(?)";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, tag.getTag());
        statement.executeUpdate();
    }

    /**
     * Insert a list of Tags into the database.
     *
     * @param tags       List of Tags
     * @param connection Connection to the Database
     * @throws SQLException Insert went wrong
     */
    public void insert(List<Tag> tags, Connection connection) throws SQLException {
        StringBuilder insertString = new StringBuilder();
        String SQL = "INSERT INTO tag(tag) VALUES(?)";
        tags.forEach(tag -> insertString.append(SQL.replace("?", tag.getTag())));
        connection.createStatement().executeUpdate(insertString.toString());
        //alternative would be: but then i need to handle a exception inside the lambda, own implementation is too much effort
        //tags.forEach(tag -> insert(tag, connection ));
    }

    /**
     * Selects all entries of a table
     *
     * @param table      Tablename
     * @param connection Connection to the Database
     * @return Results as ResultSet
     * @throws SQLException Select went wrong
     */
    public ResultSet selectAll(String table, Connection connection) throws SQLException {
        String SQL = "SELECT * FROM ?".replace("?", table);
        return connection.createStatement().executeQuery(SQL);
    }

    /**
     * Executes select from a given sql.
     *
     * @param SQL        SQL with select
     * @param connection Connection to the Database
     * @return Results as ResultSet
     * @throws SQLException Select went wrong
     */
    public ResultSet selectOwnStatement(String SQL, Connection connection) throws SQLException {
        return connection.createStatement().executeQuery(SQL);
    }

    /**
     * Selects all tags from the database into the @{@link Tag} tags list.
     *
     * @throws SQLException Select went wrong
     */
    public void readTags(Connection connection) throws SQLException {
        //selects all from the table tag
        String SQL = "SELECT * FROM tag";
        ResultSet set = connection.createStatement().executeQuery(SQL);
        //reads every row from the result set and adds a new Tag to the list
        while (set.next()) {
            int id = set.getInt("idtag");
            String tagSring = set.getString("tag");
            Tag.getTags().add(new Tag(id, tagSring));
        }
    }

    /**
     * Selects all environments from the database into the @{@link Environment} environments list.
     *
     * @throws SQLException Select went wrong
     */
    public void readEnvironemnts(Connection connection) throws SQLException {
        //selects all from the table environment
        String SQL = "SELECT * FROM environment";
        ResultSet set = connection.createStatement().executeQuery(SQL);

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
     * Reads all bookmarks and initializes all bookmarks in th @{@link Bookmark} list.
     *
     * @throws SQLException Select went wrong
     */
    public void readBookmarks(Connection connection) throws SQLException {
        //Selects all from the table bookmark
        String SQL = "SELECT * FROM bookmark";
        ResultSet set = connection.createStatement().executeQuery(SQL);

        //reads every row to initialize all bookmarks
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
                forEach(bookmark ->
                {
                    try {
                        bookmark.addTags(getTags(bookmark.getId(), connection));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Returns the Environment with the given id
     *
     * @param id id of the environment
     * @return Environment
     */
    private Environment getEnvironment(int id) {
        return Environment.getEnvironments().stream()
                .filter(environment -> environment.getId() == id).findFirst()
                .orElse(new Environment(0, "", "", Color.WHITE));
    }

    /**
     * Returns a List of Tags that are in the bookmark_has_tag table with the given id
     *
     * @param id id of the Bookmark
     * @return List of tags
     * @throws SQLException Select went wrong
     */
    private List<Tag> getTags(int id, Connection connection) throws SQLException {
        String SQL = "SELECT idtag FROM bookmark_has_tag WHERE bookmark_has_tag.idbookmark = ?".replace("?", String.valueOf(id));
        ResultSet setTags = connection.createStatement().executeQuery(SQL);
        ArrayList<Integer> tagIDs = new ArrayList<>();
        while (setTags.next()) {
            tagIDs.add(setTags.getInt("idtag"));
        }

        return tagIDs.stream()
                .map(integer -> Tag.getTags().stream()
                        .filter(tag -> tag.getId() == integer)
                        .findFirst().orElse(new Tag(0, "")))
                .collect(Collectors.toList());
    }
}



