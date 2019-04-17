package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

//NEW
public class FollowedPlaylistListConcreteFactory extends PlaylistFactory{
    public FollowedPlaylistListConcreteFactory() {
        super.playlistList = new FollowedPlaylistList();
    }

    @Override
    public ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> s) throws SQLException{
        return playlistList.createPlaylist(username, s);
    }
}
