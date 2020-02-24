package com.shawncockburn.PITools.util;

import com.shawncockburn.PITools.data.Data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class SQLiteConnection {

    // SQLite database name string
    private static final String databaseName = "productData.db";
    private static final Path databaseLocation = Paths.get(Data.getStaticDir(Data.STATIC_DIRS.SQL_DATABASE_DIR).toString(), databaseName);

    private Connection connect() {

        String databaseURL = "jdbc:sqlite:" + databaseLocation;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ResultSet executeSelectQuery(String sql) throws SQLException {
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            return rs;
        } catch (SQLException e) {
            throw e;
        }
    }

//setup database with table if not exists
    public void createDefaultTable() throws SQLException {
        if (!Files.exists(databaseLocation)) {
            String sql = "CREATE TABLE IF NOT EXISTS productData (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    productCode text NOT NULL,\n"
                    + "    productWebCode text NOT NULL\n"
                    + ");";
            Connection conn = null;
            try {
                conn = this.connect();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                throw e;
            } finally {
                conn.close();
            }
        }
    }

}
