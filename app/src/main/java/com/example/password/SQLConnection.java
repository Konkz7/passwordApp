package com.example.password;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {

    protected static String _db = "world";
    protected static String _ip = "192.168.0.105"; //192.168.0.103 / 127.0.0.1//192.168.0.105
    protected static String _port = "3306";
    protected static String _username = "root";
    protected static String _password = "AmarayR%d441";

    Connection con;
    public Connection conClass(){

        String ip = "192.168.0.103", port = "1433", db = "USER_AUTH", username = "sa", password = "12345";
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + db;

            con = DriverManager.getConnection(connectURL,username,password);
        } catch (Exception e) {
            Log.e("Error :", e.getMessage());
        }
        return con;


    }

    public Connection CONN() {

        Connection conn = null;

        try {
            // Use the modern MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Corrected connection URL
            String connectURL = "jdbc:mysql://" + _ip + ":" + _port + "/" + _db +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            // Establish the connection
            conn = DriverManager.getConnection(connectURL, _username, _password);

        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return conn;
    }



}
