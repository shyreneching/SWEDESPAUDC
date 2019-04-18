package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;

public class AlbumService implements Service {
    private JDBCConnectionPool pool;
    long millis = System.currentTimeMillis();
    Date date = new Date(millis);

    Timestamp timestamp = new Timestamp(date.getTime());

    public AlbumService() {
        this.pool = new JDBCConnectionPool();
    }

    public boolean add(Object o) throws SQLException {
        AlbumInterface a = (Album) o;

        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "INSERT INTO album VALUE (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, a.getAlbumID());
            statement.setString(2, a.getAlbumname());
            statement.setString(3, a.getArtist());
            statement.setTimestamp(4, timestamp);
            statement.setTimestamp(5, null);

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

        ObservableList <Object> album = FXCollections.observableArrayList();
        String query ="SELECT * FROM album";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                //Add
                AlbumInterface a;
                String s = rs.getString("albumname");
                if(s.contains("-Single")){
                    a = new Single();
                }
                else {
                    a = new Album();
                }

                a.setAlbumID(rs.getString("albumid"));
                a.setAlbumname(rs.getString("albumname"));
                a.setArtist(rs.getString("artist"));
                a.setDate(rs.getTimestamp("dateCreated"));
                album.add(a);
            }
            return album;
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
    // gets the album image
    public File getAlbumArt(String albumid) throws SQLException {
        Connection connection = pool.checkOut();

        String query ="SELECT * FROM album WHERE albumid = '" + albumid + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                //gets the song from the databse and make put it in a File datatype
                File theFile = new File("src/Music/" + rs.getString("artist") + "-"+ rs.getString("albumname")+ ".jpg");
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("albumart");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                return theFile;
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //add
    //upload album image
    public boolean uploadALbumArt(String albumid, File albumart) throws SQLException {
        Connection connection = pool.checkOut();

        String query = "UPDATE album SET "
                + "albumart = ? "
                + "WHERE albumid= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            //FileInputStream albumImage = new FileInputStream(albumart);
            //statement.setString(1, albumImage);
            statement.setString(1, albumart.toString());
            statement.setString(2, albumid);

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

    public AlbumInterface getAlbum(String albumid) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        String query ="SELECT * FROM album WHERE albumid = '" + albumid + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                AlbumInterface a = new Album();
                a.setAlbumID(rs.getString("albumid"));
                a.setAlbumname(rs.getString("albumname"));
                a.setArtist(rs.getString("artist"));
                a.setDate(rs.getTimestamp("dateCreated"));
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

    public AlbumInterface getAlbumName(String username, String albumname) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        String query ="SELECT * FROM album WHERE albumname = '" + albumname + "' AND artist =  '" + username +"'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                AlbumInterface a = new Album();
                a.setAlbumID(rs.getString("albumid"));
                a.setAlbumname(rs.getString("albumname"));
                a.setArtist(rs.getString("artist"));
                a.setDate(rs.getTimestamp("dateCreated"));
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


    public boolean delete(String albumid) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        String query = "DELETE FROM album WHERE albumid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, albumid);
            statement.execute();
            /*boolean deleted  = statement.execute();
            return deleted;*/
            return true;
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
    public boolean update(String albumid, Object o) throws SQLException {
        //UPDATE
        // Get a connection:
        Connection connection = pool.checkOut();

        AlbumInterface a = (Album) o;
        String query = "UPDATE album SET "
                + "albumid = ?, "
                + "albumname = ?, "
                + "artist = ? "
                + "WHERE albumid= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            statement.setString(1, a.getAlbumID());
            statement.setString(2, a.getAlbumname());
            statement.setString(3, a.getArtist());
            statement.setString(4, albumid);

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

    public boolean updateAlbumName(String albumid, String albumname) throws SQLException {
        Connection connection = pool.checkOut();
        /*UPDATE `musicplayer`.`album` SET `albumname` = 'Jumbo' WHERE (`albumid` = 'A01');*/
        String query = "UPDATE album SET "
                + "albumname = '" + albumname + "'"
                + "WHERE albumid = '" + albumid + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
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

    public boolean updateAlbumInSong(String albumid, String songid) throws SQLException {
        Connection connection = pool.checkOut();

        /*UPDATE `musicplayer`.`song` SET `albumid` = 'A02' WHERE (`idsong` = 'S02');*/
        String query = "UPDATE song SET "
                + "albumid = '" + albumid + "' "
                + "WHERE idsong = '" + songid + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
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

    public ObservableList<AlbumInterface> getUserAlbum(String username) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();
        ObservableList<AlbumInterface> albums = FXCollections.observableArrayList();

        String query = "SELECT * FROM album WHERE artist = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                AlbumInterface a = new Album();
                a.setAlbumID(rs.getString("albumid"));
                a.setAlbumname(rs.getString("albumname"));
                a.setArtist(rs.getString("artist"));
                a.setDate(rs.getTimestamp("dateCreated"));
                albums.add(a);
            }
            return albums;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    public ObservableList<AlbumInterface> searchAlbum(String search) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<AlbumInterface> albums = FXCollections.observableArrayList();

        String query = "SELECT *  FROM  album" +
                " WHERE  albumname LIKE '%" + search + "%' ";

        PreparedStatement statement = connection.prepareStatement(query);

        try {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                AlbumInterface s = new Album();
                s.setAlbumID(rs.getString("albumid"));
                s.setAlbumname(rs.getString("albumname"));
                s.setArtist(rs.getString("artist"));
                s.setDate(rs.getTimestamp("dateCreated"));

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

                albums.add(s);
            }
            return albums;
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