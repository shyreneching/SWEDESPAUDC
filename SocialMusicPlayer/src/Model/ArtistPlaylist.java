package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ArtistPlaylist implements PlaylistList {
    public SongService songService;

    public ArtistPlaylist() {
        this.songService = new SongService();
    }

    public ObservableList<PlaylistInterface> createPlaylist(String username, ObservableList<SongInterface> ss) throws SQLException {
        ObservableList<SongInterface> songs;
        if(ss == null)
            songs = songService.getUserSong(username);
        else
            songs = ss;
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        boolean added = false;

        for(SongInterface s: songs){
            for(PlaylistInterface play: playlists){
                if(play.getName().equals(s.getArtist())){
                    ObservableList<SongInterface> temp = play.getSongs();
                    temp.add(s);
                    play.setSongs(temp);
                    added = true;
                }
            }
            if(!added){
                ObservableList<SongInterface> temp = FXCollections.observableArrayList();
                PlaylistInterface p = new Playlist();
                p.setName(s.getArtist());
                temp.add(s);
                p.setSongs(temp);
                playlists.add(p);
            }
        }
        return playlists;
    }
}
