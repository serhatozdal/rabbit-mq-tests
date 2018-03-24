package com.serhatozdal.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author serhatozdal
 */
public class SqlConnection {

    java.sql.Connection sqlConnection = null;

    public Connection getConnection() {
        if (sqlConnection != null) {
            connect();
        }
        return sqlConnection;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            sqlConnection = DriverManager.getConnection("jdbc:postgresql://host:5432/db",
                    "user", "pass");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (sqlConnection != null) {
            try {
                sqlConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
