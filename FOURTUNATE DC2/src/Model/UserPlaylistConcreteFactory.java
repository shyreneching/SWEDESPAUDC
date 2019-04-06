package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UserPlaylistConcreteFactory extends PlaylistFactory {

    public UserPlaylistConcreteFactory()  {
        super.playlistList = new UserPlaylist();
    }

    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> s) throws SQLException {
        return playlistList.createPlaylist(username, s);
    }

}
