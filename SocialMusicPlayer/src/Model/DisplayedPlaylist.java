package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class DisplayedPlaylist implements PlaylistList {
    private PlaylistService playlistService;

    public DisplayedPlaylist() {
        this.playlistService = new PlaylistService();
    }

    //Collects public playlists as well as playlist of artists that they follow
    @Override
    public ObservableList<PlaylistInterface> createPlaylist(String username, ObservableList<SongInterface> s) throws SQLException {
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
//        commented out in favor of more appropriate service method
//        playlists.addAll(playlistService.getFollowedPlaylist(username));
//        playlists.addAll((PlaylistInterface) playlistService.getAll());
//
//        for(PlaylistInterface p : playlists) {
//            if(!p.isDisplay() || p.getUser().equals(username))
//                playlists.remove(p);
//        }

        for(Object ob : playlistService.getFollowedPlaylist(username)) {
            if(((PlaylistInterface)ob).isDisplay()) {
                playlists.add((PlaylistInterface)ob);
            }
        }

        for(Object ob : playlistService.getAll()) {
            if(((PlaylistInterface)ob).getUser().equalsIgnoreCase(username) && ((PlaylistInterface)ob).isDisplay()) {
                playlists.add((PlaylistInterface)ob);
            }
        }
        return playlists;
    }
}
