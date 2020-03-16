package com.shawncockburn.PITools.util;

import com.shawncockburn.PITools.data.ImageData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLiteUtil {
    private static String DATABASE_FILE_NAME = "productData.db";
    private static String IMAGE_DATA_TABLE_NAME = "productData";
    private static String IMAGE_DATA_ID_HEADING = "id";
    private static String IMAGE_DATA_PRODUCT_CODE_HEADING = "productCode";
    private static String IMAGE_DATA_PRODUCT_WEB_CODE_HEADING = "productWebCode";
    private static String IMAGE_DATA_PRODUCT_NAME_HEADING = "productName";

    public enum SQL_CONSTANTS {
        DATABASE_FILE_NAME,
        IMAGE_DATA_TABLE_NAME,
        IMAGE_DATA_ID_HEADING,
        IMAGE_DATA_PRODUCT_CODE_HEADING,
        IMAGE_DATA_PRODUCT_WEB_CODE_HEADING,
        IMAGE_DATA_PRODUCT_NAME_HEADING
    }

    public enum SQL_UPDATE_TYPE {
        INSERT,
        UPDATE,
        DELETE
    }

    public static String getSQLConstant(SQL_CONSTANTS sqlConstant) {
        switch (sqlConstant) {
            case DATABASE_FILE_NAME:
                return DATABASE_FILE_NAME;
            case IMAGE_DATA_TABLE_NAME:
                return IMAGE_DATA_TABLE_NAME;
            case IMAGE_DATA_ID_HEADING:
                return IMAGE_DATA_ID_HEADING;
            case IMAGE_DATA_PRODUCT_CODE_HEADING:
                return IMAGE_DATA_PRODUCT_CODE_HEADING;
            case IMAGE_DATA_PRODUCT_WEB_CODE_HEADING:
                return IMAGE_DATA_PRODUCT_WEB_CODE_HEADING;
            case IMAGE_DATA_PRODUCT_NAME_HEADING:
                return IMAGE_DATA_PRODUCT_NAME_HEADING;
            default:
                return null;
        }
    }

    public List<ImageData> getAllData() throws SQLException {
        SQLiteConnection sqLiteConnection = new SQLiteConnection();
        return sqLiteConnection.executeSelectAllQuery();
    }

    public List<ImageData> getData(ImageData imageData) throws SQLException {
        List<ImageData> foundResults = new ArrayList<>();
        SQLiteConnection sqLiteConnection = new SQLiteConnection();
        String sql = "SELECT * FROM " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME) + " WHERE "
                + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING) + " = ?";
        try (PreparedStatement stmt = sqLiteConnection.getSqlConnection().prepareStatement(sql)) {
            stmt.setString(1, imageData.getProductCode());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                foundResults.add(new ImageData(resultSet.getInt(getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_ID_HEADING)),
                        resultSet.getString(getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING)),
                        resultSet.getString(getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING)),
                        resultSet.getString(getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING))));
            }
        } catch (SQLException e) {
            throw e;
        }
        return foundResults;
    }

    public static List<ImageData> updateSQLDatabase(Map<ImageData, SQL_UPDATE_TYPE> imageDataMap) {
        List<ImageData> imageDataList = new ArrayList<>(imageDataMap.keySet());
        List<ImageData> errors = new ArrayList<>();

        for (ImageData imageData : imageDataList) {

            SQLiteConnection sqLiteConnection = new SQLiteConnection();
            Connection connection = sqLiteConnection.getSqlConnection();
            String sql = "";

            switch (imageDataMap.get(imageData)) {
                case INSERT:
                    sql = "INSERT INTO " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME)
                            + " (" + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING)
                            + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING)
                            + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING)
                            + ") VALUES(?,?,?)";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, imageData.getProductCode());
                        stmt.setString(2, imageData.getProductWebCode());
                        stmt.setString(3, imageData.getProductName());
                        stmt.executeUpdate();
                    } catch (SQLException e) {
                        errors.add(imageData);
                    }
                    break;
                case UPDATE:
                    sql = "UPDATE " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME)
                            + " SET " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING) + " = ?"
                            + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING) + " = ?"
                            + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING) + " = ?"
                            + " WHERE " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_ID_HEADING) + " = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, imageData.getProductCode());
                        stmt.setString(2, imageData.getProductWebCode());
                        stmt.setString(3, imageData.getProductName());
                        stmt.setInt(4, imageData.getId());
                        stmt.executeUpdate();
                    } catch (SQLException e) {
                        errors.add(imageData);
                    }
                    break;
                case DELETE:
                    sql = "DELETE FROM " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME) + " WHERE "
                            + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_ID_HEADING) + " = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setInt(1, imageData.getId());
                        stmt.executeUpdate();
                    } catch (SQLException e) {
                        errors.add(imageData);
                    }
                    break;
                default:
                    errors.add(imageData);
                    break;
            }
        }
        return errors;
    }

    public static void updateSingleItemSQLDatabase(ImageData imageData, SQL_UPDATE_TYPE sqlUpdateType) throws Exception {

        SQLiteConnection sqLiteConnection = new SQLiteConnection();
        Connection connection = sqLiteConnection.getSqlConnection();
        String sql = "";

        switch (sqlUpdateType) {
            case INSERT:
                sql = "INSERT INTO " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME)
                        + " (" + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING)
                        + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING)
                        + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING)
                        + ") VALUES(?,?,?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, imageData.getProductCode());
                    stmt.setString(2, imageData.getProductWebCode());
                    stmt.setString(3, imageData.getProductName());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw e;
                }
                break;
            case UPDATE:
                sql = "UPDATE " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME)
                        + " SET " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_CODE_HEADING) + " = ?"
                        + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_WEB_CODE_HEADING) + " = ?"
                        + ", " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_PRODUCT_NAME_HEADING) + " = ?"
                        + " WHERE " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_ID_HEADING) + " = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, imageData.getProductCode());
                    stmt.setString(2, imageData.getProductWebCode());
                    stmt.setString(3, imageData.getProductName());
                    stmt.setInt(4, imageData.getId());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw e;
                }
                break;
            case DELETE:
                sql = "DELETE FROM " + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_TABLE_NAME) + " WHERE "
                        + getSQLConstant(SQL_CONSTANTS.IMAGE_DATA_ID_HEADING) + " = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, imageData.getId());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw e;
                }
                break;
            default:
                throw new Exception("Unknown Error updating database");
        }
    }

}
