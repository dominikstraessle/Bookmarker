package database;


import java.sql.SQLException;

/**
 * Database Consumer for Alter, Create, Insert and Delete
 *
 * @param <S> Connection
 */
@FunctionalInterface
public interface DatabaseSpecifiedConsumer<S> {
    /**
     * This will execute the given Function
     *
     * @param t Argument should be a Connection Object
     */
    void execute(S t) throws SQLException;
}
