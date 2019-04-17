package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

//NEW
public class PlaylistUserCanFollowConcreteFactory extends PlaylistFactory {

    public PlaylistUserCanFollowConcreteFactory() {
        super.playlistList = new PlaylistUserCanFollow();
    }

    @Override
    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> s) throws SQLException {
        return playlistList.createPlaylist(username, s);
    }
}
