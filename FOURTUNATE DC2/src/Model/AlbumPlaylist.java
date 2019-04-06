package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class AlbumPlaylist implements PlaylistList{
    public SongService songService;

    public AlbumPlaylist() {
        this.songService = new SongService();
    }

    public ObservableList<PlaylistInterface> createPlaylist(String username, ObservableList<SongInterface> ss) throws SQLException {
        System.out.println("soend");
        ObservableList<SongInterface> songs;
        if(ss == null)
            songs = songService.getUserSong(username);
        else
            songs = ss;
        ObservableList<PlaylistInterface> playlists = FXCollections.observableArrayList();
        boolean added = false; 
        
        for(SongInterface s: songs){
            if (playlists == null){
                ObservableList<SongInterface> temp = FXCollections.observableArrayList();
                PlaylistInterface p = new Playlist();
                p.setName(s.getAlbum());
                temp.add(s);
                p.setSongs(temp);
                playlists.add(p);
            }
            for(PlaylistInterface play: playlists){
                if(play.getName().equals(s.getAlbum())){
                    ObservableList<SongInterface> temp = play.getSongs();
                    temp.add(s);
                    play.setSongs(temp);
                    added = true;
                }
            }
            if(!added){
                ObservableList<SongInterface> temp = FXCollections.observableArrayList();
                PlaylistInterface p = new Playlist();
                p.setName(s.getAlbum());
                temp.add(s);
                p.setSongs(temp);
                playlists.add(p);
            }
        }
        return playlists;
    }
}
