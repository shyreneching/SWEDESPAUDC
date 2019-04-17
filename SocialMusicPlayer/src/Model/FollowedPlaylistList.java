package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

//NEW
public class FollowedPlaylistList implements PlaylistList{
    private PlaylistService playlistService;

    public FollowedPlaylistList() {
        playlistService = new PlaylistService();
    }

    @Override
    public ObservableList<PlaylistInterface> createPlaylist(String username, ObservableList<SongInterface> s) throws SQLException {
        ObservableList<PlaylistInterface> playlists = playlistService.getFollowedPlaylist(username);
        return playlists;
    }
}
