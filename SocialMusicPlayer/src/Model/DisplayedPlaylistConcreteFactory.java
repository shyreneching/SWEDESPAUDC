package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class DisplayedPlaylistConcreteFactory extends PlaylistFactory {
    public DisplayedPlaylistConcreteFactory() {
        super.playlistList = new DisplayedPlaylist();
    }

    @Override
    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> s) throws SQLException {
        return playlistList.createPlaylist(username, s);
    }
}
