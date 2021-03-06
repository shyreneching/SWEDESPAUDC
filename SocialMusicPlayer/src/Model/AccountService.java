package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

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
        //String query = "INSERT INTO accounts VALUE (?, ?, ?)";
        String query = "INSERT INTO accounts VALUE (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, a.getUsername());
            statement.setString(2, a.getPassword());
            statement.setString(3, a.getName());

            //add
            if(o instanceof Listener){
                statement.setString(4, "Listener");
            }
            else if (o instanceof Artist){
                statement.setString(4, "Artists");
            }

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
                //Add
                AccountInterface a;
                String s = rs.getString("accountType");
                if(s.equalsIgnoreCase("Listener")){
                    a = new Listener();
                }
                else{
                    a = new Artist();
                }

                //AccountInterface a = new Account();
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

        String query ="SELECT * FROM accounts WHERE username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                //Add
                AccountInterface a;
                String s = rs.getString("accountType");
                if(s.equalsIgnoreCase("Listener")){
                    a = new Listener();
                }
                else {
                    a = new Artist();
                }

                //AccountInterface a = new Account();
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

    public ObservableList<Object> getFollowers(String username) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        ObservableList <Object> accounts = FXCollections.observableArrayList();
        String query ="SELECT * FROM accounts INNER JOIN followpeople" +
                " ON accounts.username = followpeople.follower" +
                " WHERE followpeople.followed = '" + username.trim() + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                //Add
                AccountInterface a;
                String s = rs.getString("accountType");
                if(s.equalsIgnoreCase("Listener")){
                    a = new Listener();
                }
                else{
                    a = new Artist();
                }

                //AccountInterface a = new Account();
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

    public ObservableList<Object> getFollowed(String username) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        ObservableList <Object> accounts = FXCollections.observableArrayList();
        String query ="SELECT * FROM accounts INNER JOIN followpeople" +
                " ON accounts.username = followpeople.followed" +
                " WHERE followpeople.follower = '" + username.trim() + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                //Add
                AccountInterface a;
                String s = rs.getString("accountType");
                if(s.equalsIgnoreCase("Listener")){
                    a = new Listener();
                }
                else{
                    a = new Artist();
                }

                //AccountInterface a = new Account();
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

    //add
    public boolean followPeople(AccountInterface follower, AccountInterface followed) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "INSERT INTO followpeople VALUE (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, follower.getUsername());
            statement.setString(2, followed.getUsername());

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

    //add
    public boolean unfollowPeople(AccountInterface follower, AccountInterface followed) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "DELETE FROM followpeople WHERE follower = ? AND followed = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            statement.setString(1, follower.getUsername());
            statement.setString(2, followed.getUsername());

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

    public ObservableList<AccountInterface> searchArtist(String search) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<AccountInterface> artists = FXCollections.observableArrayList();

        String query = "SELECT *  FROM  accounts" +
                " WHERE  username LIKE '%" + search + "%' AND accountType = 'Artists'";

        PreparedStatement statement = connection.prepareStatement(query);

        try {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                AccountInterface a = new Account();
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setName(rs.getString("name"));
                artists.add(a);

                /*//gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());*/


            }
            return artists;
        } catch (SQLException e) {
            e.printStackTrace();
        } /*catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */ finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        pool.checkIn(connection);
        return null;

    }

    public ObservableList<AccountInterface> searchListener(String search) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<AccountInterface> listener = FXCollections.observableArrayList();

        String query = "SELECT *  FROM  accounts" +
                " WHERE  username LIKE '%" + search + "%' AND accountType = 'Listener'";

        PreparedStatement statement = connection.prepareStatement(query);

        try {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                AccountInterface a = new Account();
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setName(rs.getString("name"));
                listener.add(a);

                /*//gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());*/


            }
            return listener;
        } catch (SQLException e) {
            e.printStackTrace();
        } /*catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */ finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        pool.checkIn(connection);
        return null;

    }
}