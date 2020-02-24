package com.shawncockburn.PITools.data;

import com.shawncockburn.PITools.util.SQLiteConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Data {

        private static final Path USER_HOME_DIR = Paths.get(System.getProperty("user.home"));
        private static final Path WORKING_DIR = Paths.get(USER_HOME_DIR.toString(), "PI-Tools");
        private static final Path SQL_DATABASE_DIR = Paths.get(WORKING_DIR.toString(), "database");
        private static final Path DEFAULT_OUTPUT_DIR = Paths.get(USER_HOME_DIR.toString(), "Desktop");


    public enum STATIC_DIRS {
        USER_HOME_DIR,
        WORKING_DIR,
        SQL_DATABASE_DIR,
        DEFAULT_OUTPUT_DIR
    }

    public static Path getStaticDir(STATIC_DIRS staticDirs){
        //todo: add these dirs
        switch (staticDirs){
            case USER_HOME_DIR: return USER_HOME_DIR;
            case WORKING_DIR: return WORKING_DIR;
            case SQL_DATABASE_DIR: return SQL_DATABASE_DIR;
            case DEFAULT_OUTPUT_DIR: return DEFAULT_OUTPUT_DIR;
            default: return null;
        }
    }

    public static void checkDataExists() throws Exception {
        //check all directories exist + create them if they don't
        if (!Files.exists(USER_HOME_DIR)){
            throw new IOException("Cannot find user's home directory");
        }

        if (!Files.exists(DEFAULT_OUTPUT_DIR)){
            throw new IOException("Cannot find user's Desktop directory");
        }

        if (!Files.exists(WORKING_DIR)){
            Files.createDirectory(WORKING_DIR);
            if (!Files.exists(WORKING_DIR)){
                throw new IOException("Cannot create directory: " + WORKING_DIR.toString());
            }
        }

        if (!Files.exists(SQL_DATABASE_DIR)){
            Files.createDirectory(SQL_DATABASE_DIR);
            if (!Files.exists(SQL_DATABASE_DIR)){
                throw new IOException("Cannot create directory: " + SQL_DATABASE_DIR.toString());
            }
        }

        //Check sql database is working
        SQLiteConnection sqLiteConnection = new SQLiteConnection();
        sqLiteConnection.createDefaultTable();
    }
}
