package br.alkazuz.clans.storage;

import br.alkazuz.clans.Clans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLCore implements DBCore {
    private Connection connection;

    private String host;

    private String username;

    private String password;

    private String database;

    public MySQLCore(String host, String database, String username, String password) {
        this.database = database;
        this.host = host;
        this.username = username;
        this.password = password;
        initialize();
    }

    private void initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.database, this.username, this.password);
        } catch (ClassNotFoundException e) {
            Clans.debug("ClassNotFoundException! " + e.getMessage());
        } catch (SQLException e) {
            Clans.debug("SQLException! " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed())
                initialize();
        } catch (SQLException e) {
            initialize();
        }
        return this.connection;
    }

    public Boolean checkConnection() {
        return Boolean.valueOf((getConnection() != null));
    }

    public PreparedStatement prepareStatement(String statement) {
        try {
            return this.connection.prepareStatement(statement);
        } catch (SQLException ex) {
            Clans.debug("Error at creating the statement: " + statement + "(" + ex.getMessage() + ")");
            return null;
        }
    }

    public void close() {
        try {
            if (this.connection != null)
                this.connection.close();
        } catch (Exception e) {
            Clans.debug("Failed to close database connection! " + e.getMessage());
        }
    }

    public ResultSet select(String query) {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException ex) {
            Clans.debug("Error at SQL Query: " + ex.getMessage());
            Clans.debug("Query: " + query);
            return null;
        }
    }

    public void insert(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                Clans.debug("Error at SQL INSERT Query: " + ex);
                Clans.debug("Query: " + query);
            }
        }
    }

    public void update(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                Clans.debug("Error at SQL UPDATE Query: " + ex);
                Clans.debug("Query: " + query);
            }
        }
    }

    public void delete(String query) {
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            if (!ex.toString().contains("not return ResultSet")) {
                Clans.debug("Error at SQL DELETE Query: " + ex);
                Clans.debug("Query: " + query);
            }
        }
    }

    public Boolean execute(String query) {
        try {
            getConnection().createStatement().execute(query);
            return Boolean.valueOf(true);
        } catch (SQLException ex) {
            Clans.debug(ex.getMessage());
            Clans.debug("Query: " + query);
            return Boolean.valueOf(false);
        }
    }

    public Boolean existsTable(String table) {
        try {
            ResultSet tables = getConnection().getMetaData().getTables(null, null, table, null);
            return Boolean.valueOf(tables.next());
        } catch (SQLException e) {
            Clans.debug("Failed to check if table '" + table + "' exists: " + e.getMessage());
            return Boolean.valueOf(false);
        }
    }

    public Boolean existsColumn(String tabell, String colum) {
        try {
            ResultSet colums = getConnection().getMetaData().getColumns(null, null, tabell, colum);
            return Boolean.valueOf(colums.next());
        } catch (SQLException e) {
            Clans.debug("Failed to check if colum '" + colum + "' exists: " + e.getMessage());
            return Boolean.valueOf(false);
        }
    }
}
