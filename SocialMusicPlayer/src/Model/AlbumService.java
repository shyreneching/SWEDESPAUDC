import Model.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlbumService implements Service {

    public boolean add(Object o) throws SQLException {
        Album a = (Album) o;

        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "INSERT INTO album VALUE (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setInt(1, a.getAlbumID());
            statement.setString(2, a.getAlbumname());
            statement.setString(3, a.getArtist());

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

    public ObservableList<Object> getAll() throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        ObservableList <Object> accounts = FXCollections.observableArrayList();
        String query ="SELECT * FROM album";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Album a = new Album();
                a.setAlbumID(rs.getInt("albumid"));
                a.setAlbumname(rs.getString("albumname"));
                a.setArtist(rs.getString("artist"));
                accounts.add(a);
            }
            return accounts;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    public Album getAlbum(String albumname) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        String query ="SELECT * FROM album WHERE albumname = " + albumname;
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Album a = new Album();
                a.setAlbumID(rs.getInt("albumid"));
                a.setAlbumname(rs.getString("albumname"));
                a.setArtist(rs.getString("artist"));

                return a;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    public boolean delete(String albumname) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "DELETE FROM album WHERE albumname = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            statement.setString(1, albumname);

            boolean deleted  = statement.execute();
            return deleted;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    //pass the username of the account that wants to be change and an account class with COMPLETE information including the updates
    public boolean update(String albumname, Object o) throws SQLException {
        //UPDATE
        // Get a connection:
        Connection connection = pool.checkOut();

        Album a = (Album) o;
        String query = "UPDATE album SET "
                + "albumid = ?, "
                + "albumname = ? "
                + "artist = ? "
                + "WHERE albumname= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            statement.setInt(1, a.getAlbumID());
            statement.setString(2, a.getAlbumname());
            statement.setString(3, a.getArtist());
            statement.setString(4, albumname);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }





}
