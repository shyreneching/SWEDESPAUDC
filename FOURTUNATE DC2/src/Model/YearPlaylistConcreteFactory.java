package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class YearPlaylistConcreteFactory extends PlaylistFactory {

    public YearPlaylistConcreteFactory() {
        super.playlistList = new YearPlaylist();
    }

    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> ss) throws SQLException {
        return playlistList.createPlaylist(username, ss);
    }
}
