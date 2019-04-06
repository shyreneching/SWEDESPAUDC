package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account implements AccountInterface {
    
    private String username, password, name;
    private ObservableList<PlaylistInterface> playlists;
    private ObservableList<SongInterface> songs;

    public Account(){

    }

    public Account(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        playlists = FXCollections.observableArrayList();
        songs = FXCollections.observableArrayList();
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name + " ";
    }

    public ObservableList<PlaylistInterface> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ObservableList<PlaylistInterface> playlists) {
        this.playlists = playlists;
    }

    public ObservableList<SongInterface> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<SongInterface> songs) {
        this.songs = songs;
    }
    
    
}
