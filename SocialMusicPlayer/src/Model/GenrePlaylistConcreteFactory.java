package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class GenrePlaylistConcreteFactory extends PlaylistFactory {

    public GenrePlaylistConcreteFactory()  {
        super.playlistList = new GenrePlaylist();
    }

    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> ss) throws SQLException {
        return playlistList.createPlaylist(username, ss);
    }
}
