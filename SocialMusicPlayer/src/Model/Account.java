package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account implements AccountInterface {
    
    protected String username, password, name;
    protected ObservableList<PlaylistInterface> playlists;
    protected ObservableList<SongInterface> songs;
    /*Add*/
    protected ObservableList<FollowedPlaylist> followedPlaylist;
    protected ObservableList<AccountInterface> followedPeople;

    public Account(){

    }

    public Account(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        playlists = FXCollections.observableArrayList();
        songs = FXCollections.observableArrayList();
        //add
        followedPlaylist = FXCollections.observableArrayList();
        followedPeople = FXCollections.observableArrayList();
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

    //Add
    public ObservableList<FollowedPlaylist> getFollowedPlaylist() {
        return followedPlaylist;
    }

    //Add
    public void setFollowedPlaylist(ObservableList<FollowedPlaylist> followedPlaylist) {
        this.followedPlaylist = followedPlaylist;
    }

    //Add
    public ObservableList<AccountInterface> getFollowedPeople() {
        return followedPeople;
    }

    //Add
    public void setFollowedPeople(ObservableList<AccountInterface> followedPeople) {
        this.followedPeople = followedPeople;
    }
}
