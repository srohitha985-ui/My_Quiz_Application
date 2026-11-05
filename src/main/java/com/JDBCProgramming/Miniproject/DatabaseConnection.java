package com.JDBCProgramming.Miniproject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.JDBCCredentials.Credentials;

public class DatabaseConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    Credentials.url,
                    Credentials.user,
                    Credentials.pwd
            );

            System.out.println("Database Connected Successfully");

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Database Connection Failed");

            e.printStackTrace();
        }

        return con;
    }
}