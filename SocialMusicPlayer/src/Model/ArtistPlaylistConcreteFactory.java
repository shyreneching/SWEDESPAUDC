package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ArtistPlaylistConcreteFactory extends PlaylistFactory {

    public ArtistPlaylistConcreteFactory() {
        super.playlistList = new ArtistPlaylist();
    }

    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> ss) throws SQLException {
        return playlistList.createPlaylist(username, ss);
    }
}
