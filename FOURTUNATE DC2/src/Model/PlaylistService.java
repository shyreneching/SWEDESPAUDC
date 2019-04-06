package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistService implements Service{
    private JDBCConnectionPool pool;

    public PlaylistService() {
        pool = new JDBCConnectionPool();
    }

    public boolean add(Object o) throws SQLException{return false;}

    //adds playlist class and the account that owns the playlist. Songs in the playlist must already be imported to the database beforehand
    public boolean add(PlaylistInterface p, AccountInterface a) throws SQLException {
        Connection connection = pool.checkOut();
        String query = "INSERT INTO playlist VALUE (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 = "INSERT INTO songcollection VALUE (?, ?)";
        PreparedStatement statement2 = connection.prepareStatement(query2);

        ObservableList<SongInterface> songs = p.getSongs();
        try {
            statement.setString(1, p.getPlaylistid());
            statement.setString(2, p.getName());
            statement.setString(3, a.getUsername());

            for(SongInterface s: songs) {
                statement2.setString(1, p.getPlaylistid());
                statement2.setString(2, s.getSongid());

                statement2.execute();
            }
            boolean added = statement.execute();
            return added;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    //adds an arraylist of songs to the playlist
    public boolean addSongsPlaylist(ObservableList<SongInterface> songs, String playlistid) throws SQLException {
        Connection connection = pool.checkOut();
        Boolean added = false;
        String query2 = "INSERT INTO songcollection VALUE (?, ?)";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            for(SongInterface s: songs) {
                statement2.setString(1, playlistid);
                statement2.setString(2, s.getSongid());

                added = statement2.execute();
            }

            return added;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    public boolean addSongPlaylist(SongInterface s, String playlistid) throws SQLException {
        Connection connection = pool.checkOut();
        String query = "INSERT INTO songcollection VALUE (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, playlistid);
            statement.setString(2, s.getSongid());
            Boolean added = statement.execute();

            return added;
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }


    //gets all the playlist in the parameter in an arraylist
    public ObservableList<Object> getAll() throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<Object> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query ="SELECT * FROM playlist";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 ="SELECT * FROM songcollection INNER JOIN song ON songcollection.idsong = song.idsong";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setSongs(FXCollections.observableArrayList());
                playlists.add(p);
            }

            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()){
                SongInterface s = new Song();
                String playlistid = rs2.getString("idplaylist");
                s.setSongid(rs2.getString("idsong"));
                s.setName(rs2.getString("songname"));
                s.setGenre(rs2.getString("genre"));
                s.setArtist(rs2.getString("artist"));
                s.setAlbum(rs2.getString("album"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");
                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());

                for(Object p : playlists){
                    if(((Playlist)p).getPlaylistid().compareTo(playlistid) == 0) {
                        songs = ((Playlist)p).getSongs();
                        songs.add(s);
                        ((Playlist)p).setSongs(FXCollections.observableArrayList());
                    }
                }
            }
            return playlists;
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

    public ObservableList<PlaylistInterface> getUserPlaylist(String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query ="SELECT * FROM playlist WHERE username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 ="SELECT * FROM songcollection INNER JOIN song ON songcollection.idsong = song.idsong";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setSongs(FXCollections.observableArrayList());
                playlists.add(p);
            }

            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()){
                SongInterface s = new Song();
                String playlistid = rs2.getString("idplaylist");
                s.setSongid(rs2.getString("idsong"));
                s.setName(rs2.getString("songname"));
                s.setGenre(rs2.getString("genre"));
                s.setArtist(rs2.getString("artist"));
                s.setAlbum(rs2.getString("album"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");
                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                for(PlaylistInterface p : playlists){
                    if(p.getPlaylistid().compareTo(playlistid) == 0) {
                        songs = p.getSongs();
                        songs.add(s);
                        p.setSongs(songs);
                    }
                }
            }
            return playlists;
        } catch (SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //gets one specific playlist with the playlistid
    public PlaylistInterface getPlaylist(String playlistid, String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<SongInterface> songs;
        PlaylistInterface p = new Playlist();

        String query ="SELECT * FROM playlist WHERE idplaylist = '" + playlistid + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 ="SELECT * FROM songcollection NATURAL JOIN song " +
                "NATURAL JOIN usersong " +
                "WHERE idplaylist = '" + playlistid + "' AND username = '" + username +"'";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setSongs(FXCollections.observableArrayList());
            }

            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()) {
                SongInterface s = new Song();
                s.setSongid(rs2.getString("idsong"));
                s.setName(rs2.getString("songname"));
                s.setGenre(rs2.getString("genre"));
                s.setArtist(rs2.getString("artist"));
                s.setAlbum(rs2.getString("album"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-" + s.getName() + ".mp3");
                //gets the song from the database and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setTimesplayed(rs2.getInt("timesplayed"));
                s.setUser(rs2.getString("username"));
                songs = p.getSongs();
                songs.add(s);
                p.setSongs(songs);
            }
            return p;
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

    //get playlist with the same name
    public ObservableList<PlaylistInterface> getPlaylistName(String playlistname, String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query ="SELECT * FROM playlist WHERE playlistname = '" + playlistname + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 ="SELECT * FROM songcollection NATURAL JOIN song " +
                "NATURAL JOIN usersong " +
                "WHERE playlistname = '" + playlistname + "' AND username = '" + username +"'";
        PreparedStatement statement2 = connection.prepareStatement(query2);

        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setSongs(FXCollections.observableArrayList());
                playlists.add(p);
            }

            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()){
                SongInterface s = new Song();
                String playlistid = rs2.getString("idplaylist");
                s.setSongid(rs2.getString("idsong"));
                s.setName(rs2.getString("songname"));
                s.setGenre(rs2.getString("genre"));
                s.setArtist(rs2.getString("artist"));
                s.setAlbum(rs2.getString("album"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");
                //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());
                s.setTimesplayed(rs2.getInt("timesplayed"));
                s.setUser(rs2.getString("username"));

                for(PlaylistInterface p : playlists){
                    if(p.getPlaylistid().compareTo(playlistid) == 0) {
                        songs = p.getSongs();
                        songs.add(s);
                        p.setSongs(songs);
                    }
                }
            }
            return playlists;
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

    //pass the playlist id to delete the specific song
    public boolean delete(String playlistid) throws SQLException {
        Connection connection = pool.checkOut();
        String query = "DELETE FROM playlist WHERE idplaylist = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 = "DELETE FROM songcollection WHERE idplaylist = ?";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            statement.setString(1, playlistid);
            statement2.setString(1, playlistid);
            boolean deleted  = statement2.execute() && statement.execute();
            return deleted;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        return false;
    }

    //deletes all the songs in the playlist, pass the playlistid as parameter
    public boolean deleteSongInPlaylist(String songid, String playlistid) throws SQLException {
        Connection connection = pool.checkOut();
        String query2 = "DELETE FROM songcollection WHERE idplaylist = ? AND idsong = ?";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            statement2.setString(1, playlistid);
            statement2.setString(2, songid);
            boolean deleted  = statement2.execute();
            return deleted;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }


    //deletes all the songs in the playlist, pass the playlistid as parameter
    public boolean deleteAllSongsInPlaylist(String playlistid) throws SQLException {
        Connection connection = pool.checkOut();
        String query2 = "DELETE FROM songcollection WHERE idplaylist = ?";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            statement2.setString(1, playlistid);
            boolean deleted  = statement2.execute();
            return deleted;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    //pass the playlistid of the playlist that wants to be change and playlist class with COMPLETE information including the updates
    public boolean update(String playlistid, Object o) throws SQLException {
        Connection connection = pool.checkOut();

        PlaylistInterface p = (Playlist) o;
        String query = "UPDATE playlist, SET "
                + "playlistname = ?,"
                + " WHERE playlist= ?";

        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, p.getName());
            statement.setString(2, p.getPlaylistid());
            statement.executeUpdate();
            updatePlaylistSongs(playlistid, p);
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

    public boolean updatePlaylistSongs(String playlistid, PlaylistInterface p) throws SQLException {
        deleteAllSongsInPlaylist(playlistid);
        addSongsPlaylist(p.getSongs(), playlistid);
        return false;
    }

}
