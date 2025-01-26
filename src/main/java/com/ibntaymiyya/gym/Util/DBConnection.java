package com.ibntaymiyya.gym.Util;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class DBConnection {



    private static final String ADMIN_DB_USER = "root";
    private static final String ADMIN_DB_PASSWORD = "root";
    private static final String DB_NAME = "gym";



    public static Connection getCon() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/" + DB_NAME;
        return DriverManager.getConnection(dbUrl, ADMIN_DB_USER, ADMIN_DB_PASSWORD);
    }




}






