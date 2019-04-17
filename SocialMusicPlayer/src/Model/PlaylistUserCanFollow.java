package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

//NEW
public class PlaylistUserCanFollow implements PlaylistList {
    private PlaylistService playlistService;
    private AccountService accountService;

    public PlaylistUserCanFollow() {
        this.playlistService = new PlaylistService();
    }

    //Collects public playlists as well as playlist of artists that they follow
    @Override
    public ObservableList<PlaylistInterface> createPlaylist(String username, ObservableList<SongInterface> s) throws SQLException{
        accountService = new AccountService();
        ObservableList<Object> followedArtists = FXCollections.observableArrayList();
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();

        //fills list of artists that user follows
        for (Object user: accountService.getFollowed(username)){
            if (user instanceof Artist){
                followedArtists.add(user);
            }
        }

        playlists.addAll(playlistService.getPublicPlaylist());//add all public playlists
        String artistName;
        //for each artist, add all the playlist that the artist has made; this is presuming every playlist of artist is open to the user
        for (Object artist: followedArtists){
            artistName = ((Artist)artist).getUsername();
            playlists.addAll(playlistService.getUserPlaylist(artistName));
        }

        return playlists;
    }
}
