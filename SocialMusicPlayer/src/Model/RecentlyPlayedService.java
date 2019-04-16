package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class RecentlyPlayedService implements Service {

    private JDBCConnectionPool pool;
    long millis = System.currentTimeMillis();
    Date date = new Date(millis);

    Timestamp timestamp = new Timestamp(date.getTime());

    public RecentlyPlayedService() {
        pool = new JDBCConnectionPool();
    }

    public ObservableList<Object> getAll() throws SQLException {
        Connection connection = pool.checkOut();

        ObservableList <Object> recentlyplayed = FXCollections.observableArrayList();
        String query ="SELECT * FROM recentlyplayed";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                RecentlyPlayedInterface a = new RecentlyPlayed();
                a.setRecentlyplayedid(rs.getInt("idrecentlyplayed"));
                a.setIdsong(rs.getString("idsong"));
                a.setUsername(rs.getString("username"));
                a.setTimeplayed(rs.getTimestamp("timeplayed"));
                recentlyplayed.add(a);
            }
            return recentlyplayed;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    public boolean add(Object o) throws SQLException {
        RecentlyPlayedInterface a = (RecentlyPlayed) o;

        ObservableList<Object> r = getAll();

        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "INSERT INTO recentlyplayed VALUE (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setInt(1, r.size()+1);
            statement.setString(2, a.getIdsong());
            statement.setString(3, a.getUsername());
            statement.setTimestamp(4, timestamp);

            boolean added = statement.execute();

            return added;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        // Return the connection
        pool.checkIn(connection);
        return false;
    }


    public boolean delete(String s) throws SQLException {
        return false;
    }


    public boolean update(String s, Object o) throws SQLException {
        return false;
    }
}
