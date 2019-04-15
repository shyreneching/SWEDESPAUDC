package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;


//The three remaining methods are abstract
//and therefore must be implemented by the subclass

public class JDBCConnectionPool extends ObjectPool<Connection> {

    private final static String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "mysql2018";
    private final static String DATABASE = "MusicPlayer";

    public JDBCConnectionPool() {
        super();
        try {
            Class.forName(DRIVER_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Connection create() {
        try {
            return (DriverManager.getConnection(URL +
                    DATABASE +
                    "?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone="
                    + TimeZone.getDefault().getID(), USERNAME, PASSWORD));
        } catch (SQLException e) {
            e.printStackTrace();
            return (null);
        }
    }

    @Override
    public void expire(Connection o) {
        try {
            ((Connection) o).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Connection o) {
        try {
            return (!((Connection) o).isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }
}

