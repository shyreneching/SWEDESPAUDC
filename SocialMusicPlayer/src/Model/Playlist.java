package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist implements PlaylistInterface{
    
    private String playlistid, name;
    private ObservableList<SongInterface> songs;

    public Playlist() {
        songs = FXCollections.observableArrayList();
    }
    
    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ObservableList<SongInterface> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<SongInterface> songs) {
        this.songs = songs;
    }
    
    
}
