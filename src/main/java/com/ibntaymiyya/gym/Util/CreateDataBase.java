package com.ibntaymiyya.gym.Util;

import java.sql.*;

public class CreateDataBase {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DATABASE_NAME = "gym";

    public static void main(String[] args) {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected to MySQL server!");


            if (!databaseExists(connection)) {
                createDatabase(connection);
                System.out.println("Database '" + DATABASE_NAME + "' created successfully!");
            }


            try (Connection gymConnection = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, USERNAME, PASSWORD)) {
                System.out.println("Connected to database '" + DATABASE_NAME + "'!");


                createTables(gymConnection);


                createStoredProcedures(gymConnection);

                System.out.println("Database initialization completed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean databaseExists(Connection connection) throws SQLException {
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + DATABASE_NAME + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next();
        }
    }

    private static void createDatabase(Connection connection) throws SQLException {
        String sql = "CREATE DATABASE " + DATABASE_NAME;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        createMembersTable(connection);
        createDietPlansTable(connection);
        createMembershipTypesTable(connection);
        createPermissionsTable(connection);
        createSerialNumbersTable(connection);
        createUsersTable(connection);
        createUserPermissionsTable(connection);
    }

    private static void createMembersTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS members (" +
                "member_id INT NOT NULL DEFAULT '1', " +
                "name VARCHAR(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'N/A', " +
                "phone_number VARCHAR(15) COLLATE utf8mb4_general_ci DEFAULT NULL, " +
                "membership_start_date DATETIME DEFAULT NULL, " +
                "membership_end_date DATETIME DEFAULT NULL, " +
                "age INT DEFAULT '0', " +
                "weight INT DEFAULT '0', " +
                "hight INT DEFAULT '0', " +
                "sub_pay DECIMAL(60,2) DEFAULT '0.00', " +
                "gender VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'N/A', " +
                "days_left INT NOT NULL DEFAULT '0', " +
                "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "totalpayed DECIMAL(60,2) NOT NULL DEFAULT '0.00', " +
                "holdDays INT DEFAULT '0', " +
                "isOnHold VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'no', " +
                "PRIMARY KEY (member_id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'members' created successfully!");
        }
    }

    private static void createDietPlansTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS dietplans (" +
                "diet_plan_id INT NOT NULL AUTO_INCREMENT, " +
                "diet_name VARCHAR(225) NOT NULL, " +
                "description VARCHAR(500) NOT NULL, " +
                "member_id INT NOT NULL, " +
                "PRIMARY KEY (diet_plan_id), " +
                "KEY member_id_idx (member_id), " +
                "CONSTRAINT member_id FOREIGN KEY (member_id) REFERENCES members (member_id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'dietplans' created successfully!");
        }
    }

    private static void createMembershipTypesTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS membershiptypes (" +
                "membership_type_id INT NOT NULL, " +
                "type_name VARCHAR(225) DEFAULT NULL, " +
                "PRIMARY KEY (membership_type_id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='next Version 1.2';";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'membershiptypes' created successfully!");
        }
    }

    private static void createPermissionsTable(Connection connection) throws SQLException {

        String createTableSQL = "CREATE TABLE IF NOT EXISTS permissions (" +
                "permission_id INT NOT NULL AUTO_INCREMENT, " +
                "permission_name VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (permission_id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table 'permissions' created successfully!");


            String insertDataSQL = "INSERT IGNORE INTO permissions (permission_id, permission_name) VALUES " +
                    "(1, 'ChangePriceAndMonth'), " +
                    "(2, 'EditCurrntUserInfo'), " +
                    "(3, 'AddNewUser'), " +
                    "(4, 'DeletAllTables'), " +
                    "(5, 'RemoveUsers');";

            statement.executeUpdate(insertDataSQL);
            System.out.println("Data inserted into 'permissions' table successfully!");
        }
    }

    private static void createSerialNumbersTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS serialnumbers (" +
                "serial_number_id INT NOT NULL, " +
                "serial_code VARCHAR(225) NOT NULL, " +
                "hwid VARCHAR(225) NOT NULL, " +
                "account_limit INT DEFAULT NULL, " +
                "activation_time VARCHAR(225) NOT NULL, " +
                "expiration_date DATETIME DEFAULT NULL, " +
                "PRIMARY KEY (serial_number_id), " +
                "UNIQUE KEY serial_code_UNIQUE (serial_code)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'serialnumbers' created successfully!");
        }
    }

    private static void createUsersTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT NOT NULL AUTO_INCREMENT, " +
                "username VARCHAR(225) NOT NULL, " +
                "password VARCHAR(225) NOT NULL, " +
                "account_created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "serial_number_id INT DEFAULT NULL, " +
                "isAdmin VARCHAR(45) NOT NULL DEFAULT 'no', " +
                "PRIMARY KEY (user_id), " +
                "UNIQUE KEY username_UNIQUE (username), " +
                "KEY serial_number_id_idx (serial_number_id), " +
                "CONSTRAINT serial_number_id FOREIGN KEY (serial_number_id) REFERENCES serialnumbers (serial_number_id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='For Users (Admin , Coatches , etc.)';";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'users' created successfully!");
        }
    }

    private static void createUserPermissionsTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS userpermissions (" +
                "user_permission_id INT NOT NULL AUTO_INCREMENT, " +
                "user_id INT DEFAULT NULL, " +
                "permission_id INT DEFAULT NULL, " +
                "PRIMARY KEY (user_permission_id), " +
                "KEY permission_id_idx (permission_id), " +
                "KEY user_id_idx (user_id), " +
                "CONSTRAINT permission_id FOREIGN KEY (permission_id) REFERENCES permissions (permission_id), " +
                "CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES users (user_id)" +
                ") ENGINE=InnoDB AUTO_INCREMENT=284 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'userpermissions' created successfully!");
        }
    }

    private static void createStoredProcedures(Connection connection) throws SQLException {
        createGetHoldSubsProcedure(connection);
        createGetMembersExpiringDaysProcedure(connection);
        createGetSubsDaysLeftProcedure(connection);
        createSetAdminStatusProcedure(connection);
    }

    private static void createGetHoldSubsProcedure(Connection connection) throws SQLException {
        String sql = "CREATE PROCEDURE IF NOT EXISTS GetHoldSubs() " +
                "BEGIN " +
                "   SELECT " +
                "       member_id, " +
                "       name, " +
                "       phone_number, " +
                "       membership_start_date, " +
                "       membership_end_date, " +
                "       age, " +
                "       weight, " +
                "       hight, " +
                "       sub_pay, " +
                "       gender, " +
                "       totalpayed, " +
                "       holdDays, " +
                "       isOnHold, " +
                "       CASE " +
                "           WHEN DATEDIFF(membership_end_date, CURDATE()) <= 0 THEN 0 " +
                "           ELSE DATEDIFF(membership_end_date, CURDATE()) " +
                "       END AS days_left " +
                "   FROM " +
                "       members " +
                "   WHERE " +
                "       isOnHold = 'yes'; " +
                "END;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Stored Procedure 'GetHoldSubs' created successfully!");
        }
    }

    private static void createGetMembersExpiringDaysProcedure(Connection connection) throws SQLException {
        String sql = "CREATE PROCEDURE IF NOT EXISTS GetMembersExpiringDays(IN daysLeft INT) " +
                "BEGIN " +
                "   SELECT " +
                "       member_id, " +
                "       name, " +
                "       phone_number, " +
                "       membership_start_date, " +
                "       membership_end_date, " +
                "       age, " +
                "       weight, " +
                "       hight, " +
                "       sub_pay, " +
                "       gender, " +
                "       totalpayed, " +
                "       holdDays, " +
                "       isOnHold, " +
                "       DATEDIFF(membership_end_date, CURDATE()) AS days_left " +
                "   FROM " +
                "       members " +
                "   WHERE " +
                "       DATEDIFF(membership_end_date, CURDATE()) <= daysLeft " +
                "       AND DATEDIFF(membership_end_date, CURDATE()) >= -14 " +
                "       AND isOnHold = 'no' " +
                "   ORDER BY " +
                "       days_left; " +
                "END;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Stored Procedure 'GetMembersExpiringDays' created successfully!");
        }
    }

    private static void createGetSubsDaysLeftProcedure(Connection connection) throws SQLException {
        String sql = "CREATE PROCEDURE IF NOT EXISTS GetSubsDaysLeft() " +
                "BEGIN " +
                "   SELECT " +
                "       member_id, " +
                "       name, " +
                "       phone_number, " +
                "       membership_start_date, " +
                "       membership_end_date, " +
                "       age, " +
                "       weight, " +
                "       hight, " +
                "       sub_pay, " +
                "       gender, " +
                "       totalpayed, " +
                "       holdDays, " +
                "       isOnHold, " +
                "       GREATEST(DATEDIFF(membership_end_date, CURDATE()), 0) AS days_left " +
                "   FROM " +
                "       members " +
                "   ORDER BY " +
                "       days_left; " +
                "END;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Stored Procedure 'GetSubsDaysLeft' created successfully!");
        }
    }

    private static void createSetAdminStatusProcedure(Connection connection) throws SQLException {
        String sql = "CREATE PROCEDURE IF NOT EXISTS SetAdminStatus(IN userId INT) " +
                "BEGIN " +
                "   DECLARE permissionCount INT; " +
                "   SELECT COUNT(*) INTO permissionCount " +
                "   FROM userpermissions " +
                "   WHERE user_id = userId " +
                "     AND permission_id IN (1, 2, 3, 4, 5); " +
                "   IF permissionCount = 5 THEN " +
                "       UPDATE users SET isAdmin = 'yes' WHERE user_id = userId; " +
                "   ELSE " +
                "       UPDATE users SET isAdmin = 'no' WHERE user_id = userId; " +
                "   END IF; " +
                "END;";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Stored Procedure 'SetAdminStatus' created successfully!");
        }
    }

}
