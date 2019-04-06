package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AccountService implements Service{
    private JDBCConnectionPool pool;
    private PlaylistService ps;

    public AccountService() {
         pool = new JDBCConnectionPool();
    }

    //adds account to the database. Must be COMPLETE information
    public boolean add(Object o) throws SQLException {
        AccountInterface a = (AccountInterface) o;

        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "INSERT INTO accounts VALUE (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, a.getUsername());
            statement.setString(2, a.getPassword());
            statement.setString(3, a.getName());

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

    //gets all the accounts in the parameter in an arraylist
    public ObservableList<Object> getAll() throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        ObservableList <Object> accounts = FXCollections.observableArrayList();
        String query ="SELECT * FROM accounts";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                AccountInterface a = new Account();
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setName(rs.getString("name"));
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

    //gets one specific account with the username as a parameter
    public AccountInterface getAccount(String username) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        String query ="SELECT * FROM accounts WHERE username = " + username;
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                AccountInterface a = new Account();
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setName(rs.getString("name"));

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

    //pass the username of the account that you want to view the playlist
    public ObservableList<PlaylistInterface> getUserPlaylist(String username) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlist = FXCollections.observableArrayList();

        String query ="SELECT * FROM playlist WHERE username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                playlist.add(ps.getPlaylist(rs.getString("idplaylist"), username));
            }
            return playlist;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return playlist;
    }

    //pass the username to delete an account
    public boolean delete(String username) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "DELETE FROM accounts WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            statement.setString(1, username);

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
    public boolean update(String username, Object o) throws SQLException {
        //UPDATE
        // Get a connection:
        Connection connection = pool.checkOut();

        AccountInterface a = (AccountInterface) o;
        String query = "UPDATE accounts SET "
                + "name = ?, "
                + "password = ? "
                + "WHERE username= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            statement.setString(1, a.getName());
            statement.setString(2, a.getPassword());
            statement.setString(3, username);

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
