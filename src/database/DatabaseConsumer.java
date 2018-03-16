package database;


import java.sql.SQLException;

/**
 * Database Consumer for Alter, Create, Insert and Delete
 *
 * @param <S> SQL String
 * @param <T> Connection
 */
@FunctionalInterface
public interface DatabaseConsumer<S, T> {
    /**
     * This will execute the given Function
     *
     * @param s Argmuent should be a SQL String
     * @param t Argument should be a Connection Object
     */
    void execute(S s, T t) throws SQLException;
}
