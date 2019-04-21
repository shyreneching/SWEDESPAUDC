package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class PlaylistService implements Service{
    private JDBCConnectionPool pool;
    long millis = System.currentTimeMillis();
    Date date = new Date(millis);

    Timestamp timestamp = new Timestamp(date.getTime());

    public PlaylistService() {
        pool = new JDBCConnectionPool();
    }

    public boolean add(Object o) throws SQLException{return false;}

    //adds playlist class and the account that owns the playlist. Songs in the playlist must already be imported to the database beforehand
    public boolean add(PlaylistInterface p, AccountInterface a) throws SQLException {
        Connection connection = pool.checkOut();
        String query = "INSERT INTO playlist VALUE (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 = "INSERT INTO songcollection VALUE (?, ?)";
        PreparedStatement statement2 = connection.prepareStatement(query2);

        ObservableList<SongInterface> songs = p.getSongs();
        try {
            statement.setString(1, p.getPlaylistid());
            statement.setString(2, p.getName());
            statement.setString(3, a.getUsername());
            statement.setString(4, "private");
            statement.setBoolean(5, false);
            statement.setTimestamp(6, timestamp);

            for(SongInterface s: songs) {
                statement2.setString(1, p.getPlaylistid());
                statement2.setString(2, s.getSongid());

                statement2.execute();
            }
            /*boolean added = statement.execute();
            return added;*/
            statement.execute();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
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
        String query2 ="SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "NATURAL JOIN album";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");
                /*//gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());*/

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
        }/* catch (IOException e) {
            e.printStackTrace();
        }*/ finally {
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

        String query = "SELECT * FROM playlist WHERE username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 = "SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "INNER JOIN album ON song.albumid = album.albumid";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setDate(rs2.getTimestamp("dateuploaded"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");
               /* //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());*/
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
        } /*catch (IOException e) {
            e.printStackTrace();
        } */finally {
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
                "NATURAL JOIN album " +
                "WHERE idplaylist = '" + playlistid + "' " +
                "AND username = '" + username +"'";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
            }

            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()) {
                SongInterface s = new Song();
                s.setSongid(rs2.getString("idsong"));
                s.setName(rs2.getString("songname"));
                s.setGenre(rs2.getString("genre"));
                s.setArtist(rs2.getString("artist"));
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-" + s.getName() + ".mp3");
                //gets the song from the database and make put it in a File datatype
                /* //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());*/
                s.setTimesplayed(rs2.getInt("timesplayed"));
                s.setUser(rs2.getString("username"));
                songs = p.getSongs();
                songs.add(s);
                p.setSongs(songs);
            }
            return p;
        } catch (SQLException e){
            e.printStackTrace();
        } /*catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */finally {
            if(statement != null) statement.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //get playlist with the same name
    /*public ObservableList<PlaylistInterface> getPlaylistName(String playlistname, String username) throws SQLException {*/
    public PlaylistInterface getPlaylistName(String playlistname, String username) throws SQLException {
        Connection connection = pool.checkOut();
        /*ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();*/
        ObservableList <SongInterface> songs;

        String query ="SELECT * FROM playlist WHERE playlistname = '" + playlistname.trim() +
                "' AND playlist.username = '" + username.trim() + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        /*String query2 ="SELECT * FROM songcollection NATURAL JOIN song " +
                "NATURAL JOIN usersong " +
                "NATURAL JOIN album " +
                "WHERE playlistname = '" + playlistname + "' AND username = '" + username +"'";*/
        String query2 = "SELECT * FROM songcollection INNER JOIN song ON songcollection.idsong = song.idsong " +
                "INNER JOIN usersong ON song.idsong = usersong.idsong " +
                "INNER JOIN playlist ON songcollection.idplaylist = playlist.idplaylist " +
                "INNER JOIN album ON song.albumid = album.albumid " +
                "WHERE playlist.playlistname = '" + playlistname.trim() + "' AND playlist.username = '" + username.trim() + "'";
        PreparedStatement statement2 = connection.prepareStatement(query2);

        PlaylistInterface p = new Playlist();
        try {
            ResultSet rs = statement.executeQuery();
            /*while (rs.next()){*/
            if(rs.next()) {
                /*PlaylistInterface p = new Playlist();*/
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
                /*playlists.add(p);*/
            }

            ResultSet rs2 = statement2.executeQuery();
            while (rs2.next()){
                SongInterface s = new Song();
                String playlistid = rs2.getString("idplaylist");
                s.setSongid(rs2.getString("idsong"));
                s.setName(rs2.getString("songname"));
                s.setGenre(rs2.getString("genre"));
                s.setArtist(rs2.getString("artist"));
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setDate(rs2.getTimestamp("dateuploaded"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");
               /* //gets the song from the databse and make put it in a File datatype
                File theFile = new File(s.getName());
                OutputStream out = new FileOutputStream(theFile);
                InputStream input = rs2.getBinaryStream("songfile");
                byte[] buffer = new byte[4096];  // how much of the file to read/write at a time
                while (input.read(buffer) > 0) {
                    out.write(buffer);
                }
                s.setSongfile(theFile);
                //takes the exact location of the song
                s.setFilelocation(theFile.getAbsolutePath());*/
                s.setTimesplayed(rs2.getInt("timesplayed"));
                s.setUser(rs2.getString("username"));

                /*for(PlaylistInterface p : playlists){*/
                    if(p.getPlaylistid().compareTo(playlistid) == 0) {
                        songs = p.getSongs();
                        songs.add(s);
                        p.setSongs(songs);
                    }
                /*}*/
            }
            /*return playlists;*/
            return p;
        } catch (SQLException e){
            e.printStackTrace();
        } /*catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */finally {
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
            /*boolean deleted  = statement2.execute() && statement.execute();*/
            statement2.execute();
            statement.execute();
            /*return deleted;*/
            return true;
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
        String query = "UPDATE playlist SET "
                + "playlistname = ?," +
                "status = ?," +
                "display = ?"
                + " WHERE idplaylist = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, p.getName());
            statement.setString(2, p.getStatus());
            statement.setBoolean(3, p.isDisplay());
            statement.setString(4, p.getPlaylistid());
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

    public boolean updatePlaylistName(String name, String playlistid) throws SQLException {
        Connection connection = pool.checkOut();
        /*UPDATE `musicplayer`.`playlist` SET `playlistname` = 'new plasylist' WHERE (`idplaylist` = 'P01');*/
        String query = "UPDATE playlist SET " +
                "playlistname = '" + name + "'" +
                "WHERE idplaylist = '" + playlistid + "'";
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

    public boolean updatePlaylistSongs(String playlistid, PlaylistInterface p) throws SQLException {
        deleteAllSongsInPlaylist(playlistid);
        addSongsPlaylist(p.getSongs(), playlistid);
        return false;
    }


    //add
    public boolean updateFollowedPlaylist(String playlistid, PlaylistInterface p) throws SQLException {
        Connection connection = pool.checkOut();

        String query = "UPDATE followedplaylist SET " +
                "status = ?," +
                "display = ?"
                + " WHERE idplaylist= ?";

        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setString(1, p.getStatus());
            statement.setBoolean(2, p.isDisplay());
            statement.setString(3, p.getPlaylistid());
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


    //new
    public ObservableList<PlaylistInterface> getFollowedPlaylist(String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query ="SELECT * FROM playlist INNER JOIN followedplaylist " +
                "ON playlist.idplaylist = followedplaylist.idplaylist " +
                "WHERE followedplaylist.username = '" + username + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        String query2 ="SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "NATURAL JOIN album ";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new FollowedPlaylist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setDate(rs2.getTimestamp("dateuploaded"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");

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
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //add
    public ObservableList<PlaylistInterface> getUserPublicPlaylist(String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query = "SELECT * FROM playlist INNER JOIN followedplaylist " +
                "ON playlist.idplaylist = followedplaylist.idplaylist " +
                "WHERE followedplaylist.username = '" + username + "' " +
                "AND followedplaylist.status = 'public'";
        String query3 = "SELECT * FROM playlist " +
                "WHERE username = '" + username + "'" +
                "AND status = 'public'";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statement3 = connection.prepareStatement(query3);
        String query2 ="SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "NATURAL JOIN album";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new FollowedPlaylist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("playlist.username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                playlists.add(p);
            }

            ResultSet rs3 = statement3.executeQuery();
            while (rs3.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");

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
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //add
    public ObservableList<PlaylistInterface> getUserPrivatePlaylist(String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query = "SELECT * FROM playlist INNER JOIN followedplaylist " +
                "ON playlist.idplaylist = followedplaylist.idplaylist " +
                "WHERE followedplaylist.username = '" + username + "' " +
                "AND followedplaylist.status = 'private'";
        String query3 = "SELECT * FROM playlist " +
                "WHERE username = '" + username + "'" +
                "AND status = 'private'";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statement3 = connection.prepareStatement(query3);
        String query2 ="SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "NATURAL JOIN album";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new FollowedPlaylist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("playlist.username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                playlists.add(p);
            }

            ResultSet rs3 = statement3.executeQuery();
            while (rs3.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");

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
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //add
    public ObservableList<PlaylistInterface> getPublicPlaylist() throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query = "SELECT * FROM playlist INNER JOIN followedplaylist " +
                "ON playlist.idplaylist = followedplaylist.idplaylist " +
                "WHERE followedplaylist.status = 'public'";
        String query3 = "SELECT * FROM playlist " +
                "WHERE status = 'public'";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statement3 = connection.prepareStatement(query3);
        String query2 ="SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "NATURAL JOIN album";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new FollowedPlaylist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("playlist.username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                playlists.add(p);
            }

            ResultSet rs3 = statement3.executeQuery();
            while (rs3.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setSize(rs2.getFloat("size"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");

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
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //add
    public ObservableList<PlaylistInterface> getUserDisplayedPlaylist(String username) throws SQLException {
        Connection connection = pool.checkOut();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        ObservableList <SongInterface> songs;

        String query = "SELECT * FROM playlist INNER JOIN followedplaylist " +
                "ON playlist.idplaylist = followedplaylist.idplaylist " +
                "WHERE followedplaylist.username = '" + username + "' " +
                "AND followedplaylist.display = true";
        String query3 = "SELECT * FROM playlist " +
                "WHERE username = '" + username + "'" +
                "AND display = true";
        PreparedStatement statement = connection.prepareStatement(query);
        PreparedStatement statement3 = connection.prepareStatement(query3);
        String query2 ="SELECT * FROM songcollection " +
                "INNER JOIN song ON songcollection.idsong = song.idsong " +
                "NATURAL JOIN album";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                PlaylistInterface p = new FollowedPlaylist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("playlist.username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                playlists.add(p);
            }

            ResultSet rs3 = statement3.executeQuery();
            while (rs3.next()){
                PlaylistInterface p = new Playlist();
                p.setPlaylistid(rs.getString("idplaylist"));
                p.setName(rs.getString("playlistname"));
                p.setUser(rs.getString("username"));
                p.setSongs(FXCollections.observableArrayList());
                p.setStatus(rs.getString("status"));
                p.setDisplay(rs.getBoolean("display"));
                p.setDate(rs.getTimestamp("datecreated"));
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
                s.setAlbum(rs2.getString("albumname"));
                s.setYear(rs2.getInt("year"));
                s.setTrackNumber(rs2.getInt("trackNumber"));
                s.setLength(rs2.getInt("length"));
                s.setSize(rs2.getFloat("size"));
                s.setDate(rs2.getTimestamp("dateuploaded"));
                // sets the name to "Artist-title"
                s.setFilename(s.getArtist() + "-"+ s.getName()+ ".mp3");

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
        } finally {
            if(statement != null) statement.close();
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return null;
    }

    //add
    public boolean followPlaylist(String idplaylist, String username) throws SQLException {
        Connection connection = pool.checkOut();
        String query = "INSERT INTO followedplaylist VALUE (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);

        try {
            statement.setString(1, idplaylist);
            statement.setString(2, username);
            statement.setString(3, "private");
            statement.setBoolean(4, false);

            /*boolean added = statement.execute();*/
            statement.execute();
            /*return added;*/
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

    //add
    public boolean unfollowPlaylist(String idplaylist, String username) throws SQLException {
        Connection connection = pool.checkOut();
        String query2 = "DELETE FROM followedplaylist WHERE idplaylist = ? AND username = ?";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        try {
            statement2.setString(1, idplaylist);
            statement2.setString(2, username);
            /*boolean deleted  = statement2.execute();*/
            statement2.execute();
            /*return deleted;*/
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(statement2 != null) statement2.close();
            if(connection != null)  connection.close();
        }
        pool.checkIn(connection);
        return false;
    }

    //add
    //gets all the accounts in the parameter in an arraylist
    public ObservableList<AccountInterface> getPlaylistFollower(String playlistid) throws SQLException {
        // Get a connection:
        Connection connection = pool.checkOut();

        ObservableList <AccountInterface> accounts = FXCollections.observableArrayList();
        String query ="SELECT * FROM followedplaylist INNER JOIN accounts " +
                "ON followedplaylist.username = accounts.username " +
                "WHERE idplaylist = '" + playlistid + "'";
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

}