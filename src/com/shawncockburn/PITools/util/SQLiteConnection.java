package com.shawncockburn.PITools.util;

import com.shawncockburn.PITools.data.Data;
import com.shawncockburn.PITools.data.ImageData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLiteConnection {

    // SQLite database name string
    private static final String databaseName = SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.DATABASE_FILE_NAME);
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

    public List<ImageData> executeSelectAllQuery() throws SQLException {
        String sqlSelectAll = "SELECT * FROM " + SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME);
        List<ImageData> imageDataList = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet resultSet    = stmt.executeQuery(sqlSelectAll)){
            if (resultSet != null){
                while (resultSet.next()){
                    Integer id = resultSet.getInt(SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_ID_HEADING));
                    String productCode = resultSet.getString(SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING));
                    String productWebCode = resultSet.getString(SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING));
                    String productName = resultSet.getString(SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING));
                    imageDataList.add(new ImageData(id, productCode, productWebCode, productName));
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return imageDataList;
    }

    public Integer executeUpdateQuery(String sql) throws SQLException {
        Integer updatedRows = 0;
        Connection conn = null;
        try {conn = this.connect();
             Statement stmt  = conn.createStatement();
             updatedRows = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
        return updatedRows;
    }

    public Connection getSqlConnection(){
        return connect();
    }

//setup database with table if not exists
    public void createDefaultTable() throws SQLException {
        if (!Files.exists(databaseLocation)) {
            String sql = "CREATE TABLE IF NOT EXISTS "+ SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME) +" (\n"
                    + "    "+ SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_ID_HEADING) +" integer PRIMARY KEY,\n"
                    + "    "+ SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING) +" text NOT NULL,\n"
                    + "    "+ SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING) +" text NOT NULL,\n"
                    + "    "+ SQLiteUtil.getSQLConstant(SQLiteUtil.SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING) +" text NOT NULL\n"
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
