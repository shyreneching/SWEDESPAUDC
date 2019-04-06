package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;

public class SongService implements Service{
    private JDBCConnectionPool pool;
    long millis = System.currentTimeMillis();
    Date date = new Date(millis);

    Timestamp timestamp = new Timestamp(date.getTime());

    public SongService() {
        pool = new JDBCConnectionPool();
    }

    //adds song to the database. Must be COMPLETE information
    public boolean add(Object o) throws SQLException {
        SongInterface s = (Song) o;
        Connection connection = pool.checkOut();
        String query = "INSERT INTO song VALUE (?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 = "INSERT INTO usersong VALUE (?, ?, ?)";
        PreparedStatement statement2 = connection.prepareStatement(query2);

        try {
            statement.setString(1, s.getSongid());
            statement.setString(2, s.getName());
            statement.setString(3, s.getGenre());
            statement.setString(4, s.getArtist());
            statement.setString(5, s.getAlbum());
            statement.setInt(6, s.getYear());
            statement.setInt(7, s.getTrackNumber());
            statement.setInt(8, s.getLength());
            statement.setDouble(9, s.getSize());
            // Write the file into the database
            FileInputStream songfile = new FileInputStream(s.getSongfile());
            statement.setBinaryStream(10, songfile);
            statement.setTimestamp(11, timestamp);

            statement2.setString(1, s.getSongid());
            statement2.setString(2, s.getUser());
            statement2.setInt(3, s.getTimesplayed());

            statement.execute();
            statement2.execute();
            
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    //adds existing song to user
    public boolean addSongtoUser(String songid, String username) throws SQLException, SQLIntegrityConstraintViolationException {
        Connection connection = pool.checkOut();

        String query = "INSERT INTO usersong VALUE (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        try {
            statement.setString(1, songid);
            statement.setString(2, username);
            statement.setInt(3, 0);

            boolean added = statement.execute();

            return added;
        } catch (SQLException e){
            
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    //gets all the songs in the parameter in an arraylist
    public ObservableList<Object> getAll() throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList <Object> songs = FXCollections.observableArrayList();
        
        String query ="SELECT * FROM song";
        PreparedStatement statement = connection.prepareStatement(query);

        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                SongInterface s = new Song();
                s.setSongid(rs.getString("idsong"));
                s.setName(rs.getString("songname"));
                s.setGenre(rs.getString("genre"));
                s.setArtist(rs.getString("artist"));
                s.setAlbum(rs.getString("album"));
                s.setYear(rs.getInt("year"));
                s.setTrackNumber(rs.getInt("trackNumber"));
                s.setLength(rs.getInt("length"));
                s.setSize(rs.getFloat("size"));
                s.setSize(rs.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename("src/Music/" + s.getArtist() + "-"+ s.getName()+ ".mp3");

                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setDate(rs.getTimestamp("dateuploaded"));

                songs.add(s);
            }
            return songs;
        } catch (SQLException e){
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

    // gets song of a specific user
    public ObservableList<SongInterface> getUserSong(String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<SongInterface> songs = FXCollections.observableArrayList();

        String query ="SELECT * FROM song INNER JOIN usersong ON song.idsong = usersong.idsong " +
                "WHERE username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                SongInterface s = new Song();
                s.setSongid(rs.getString("idsong"));
                s.setName(rs.getString("songname"));
                s.setGenre(rs.getString("genre"));
                s.setArtist(rs.getString("artist"));
                s.setAlbum(rs.getString("album"));
                s.setYear(rs.getInt("year"));
                s.setTrackNumber(rs.getInt("trackNumber"));
                s.setLength(rs.getInt("length"));
                s.setSize(rs.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename("src/Music/" + s.getArtist() + "-"+ s.getName()+ ".mp3");

                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setUser(rs.getString("username"));
                s.setTimesplayed(rs.getInt("timesplayed"));
                s.setDate(rs.getTimestamp("dateuploaded"));
                songs.add(s);
            }
            return songs;
        } catch (SQLException e){
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

    //gets one specific song with the id of the song
    public SongInterface getSong(String songid, String username) throws SQLException {
        Connection connection = pool.checkOut();

        String query ="SELECT * FROM song NATURAL JOIN usersong WHERE idsong = '" + songid +
                "' AND username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Song s = new Song();
                s.setSongid(rs.getString("idsong"));
                s.setName(rs.getString("songname"));
                s.setGenre(rs.getString("genre"));
                s.setArtist(rs.getString("artist"));
                s.setAlbum(rs.getString("album"));
                s.setYear(rs.getInt("year"));
                s.setTrackNumber(rs.getInt("trackNumber"));
                s.setLength(rs.getInt("length"));
                s.setSize(rs.getFloat("size"));

                // sets the name to "Artist-title"
                s.setFilename("src/Music/" + s.getArtist() + "-"+ s.getName()+ ".mp3");

                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }

                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setUser(rs.getString("username"));
                s.setTimesplayed(rs.getInt("timesplayed"));
                s.setDate(rs.getTimestamp("dateuploaded"));
                return s;
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

    //get songs with the same name
    public SongInterface getSongName(String songname, String album, String artist) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<SongInterface> songs = FXCollections.observableArrayList();

        String query ="SELECT * FROM song WHERE songname = '" + songname +
                "' AND album = '" + album + "' AND artist = '" + artist + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Song s = new Song();
                s.setSongid(rs.getString("idsong"));
                s.setName(rs.getString("songname"));
                s.setGenre(rs.getString("genre"));
                s.setArtist(rs.getString("artist"));
                s.setAlbum(rs.getString("album"));
                s.setYear(rs.getInt("year"));
                s.setTrackNumber(rs.getInt("trackNumber"));
                s.setLength(rs.getInt("length"));
                s.setSize(rs.getFloat("size"));

                // sets the name to "Artist-title"
                s.setFilename("src/Music/" + s.getArtist() + "-" + s.getName() + ".mp3");

                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }

                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setUser(rs.getString("username"));
                s.setTimesplayed(rs.getInt("timesplayed"));
                s.setDate(rs.getTimestamp("dateuploaded"));
                return s;
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


    //get songs with the same name
    public ObservableList<SongInterface> getSongName(String songname, String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<SongInterface> songs = FXCollections.observableArrayList();

        String query ="SELECT * FROM song NATURAL JOIN usersong WHERE songname = '" + songname +
                "' AND '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        try {

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                SongInterface s = new Song();
                s.setSongid(rs.getString("idsong"));
                s.setName(rs.getString("songname"));
                s.setGenre(rs.getString("genre"));
                s.setArtist(rs.getString("artist"));
                s.setAlbum(rs.getString("album"));
                s.setYear(rs.getInt("year"));
                s.setTrackNumber(rs.getInt("trackNumber"));
                s.setLength(rs.getInt("length"));
                s.setSize(rs.getFloat("size"));

                // sets the name to "Artist-title"
                s.setFilename("src/Music/" + s.getArtist() + "-"+ s.getName()+ ".mp3");

                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getFilename());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setUser(rs.getString("username"));
                s.setTimesplayed(rs.getInt("timesplayed"));
                s.setDate(rs.getTimestamp("dateuploaded"));
                songs.add(s);
            }
            return songs;
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

    public boolean delete(String s){return false;}

    //pass the song id to delete the specific song
    public boolean delete(String songid, AccountInterface a) throws SQLException {
        Connection connection = pool.checkOut();
        String query = "DELETE FROM usersong " +
                "WHERE idsong = ? AND username = ?";
        String query2 = "DELETE FROM songcollection " +
                "WHERE idsong = ? AND playlistid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            statement.setString(1, songid);
            statement.setString(2, a.getUsername());
            if(a.getPlaylists() != null) {
                for (PlaylistInterface p: a.getPlaylists()) {
                statement2.setString(1, songid);
                statement2.setString(2, p.getPlaylistid());
                statement2.execute();
            }
            }
            boolean deleted  = statement.execute();
            return deleted;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        return false;
    }

    public boolean update(String s, Object o){return false;}

    //pass the songid of the song that wants to be change and song class with COMPLETE information including the updates
    public boolean update(String songid, SongInterface s, String username) throws SQLException {
        Connection connection = pool.checkOut();
        AudioParserInterface ap = new AudioParser();

        String query = "UPDATE song SET "
                + "songname = ?, "
                + "genre = ?, "
                + "artist = ?, "
                + "album = ?, "
                + "year = ?, "
                + "trackNumber = ?, "
                + "length = ?, "
                + "size = ?, "
                + "songfile = ? "
                + " WHERE idsong = ?";

        String query2 = "UPDATE usersong SET "
                + "timesplayed = ? "
                + " WHERE username= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            statement.setString(1, s.getName());
            statement.setString(2, s.getGenre());
            statement.setString(3, s.getArtist());
            statement.setString(4, s.getAlbum());
            statement.setInt(5, s.getYear());
            statement.setInt(6, s.getTrackNumber());
            statement.setInt(7, s.getLength());
            statement.setDouble(8, s.getSize());
            // Write the file into the database
            FileInputStream songfile = new FileInputStream(s.getSongfile());
            statement.setBinaryStream(9, songfile);
            statement.setString(10, songid)
            ;
            statement2.setInt(1, s.getTimesplayed());
            statement2.setString(2, username);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }
}
