package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

//NEW
public class PlaylistUserCanFollow implements PlaylistList {
    private PlaylistService playlistService;

    public PlaylistUserCanFollow() {
        this.playlistService = new PlaylistService();
    }

    @Override
    public ObservableList<PlaylistInterface> createPlaylist(String username, ObservableList<SongInterface> s) throws SQLException{
        ObservableList<PlaylistInterface> playlists = playlistService.getPublicPlaylist();
        return playlists;
    }
}
