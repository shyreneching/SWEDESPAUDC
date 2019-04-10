package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist implements PlaylistInterface{
    
    private String playlistid, name;
    //add
    private String user, status;
    private ObservableList<SongInterface> songs;
    //add
    private boolean display;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
