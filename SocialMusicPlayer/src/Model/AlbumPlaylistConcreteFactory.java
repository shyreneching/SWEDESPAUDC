package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class AlbumPlaylistConcreteFactory extends PlaylistFactory {


    public AlbumPlaylistConcreteFactory() {
        super.playlistList = new AlbumPlaylist();
    }

    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> s) throws SQLException {
        return playlistList.createPlaylist(username, s);
    }
}
