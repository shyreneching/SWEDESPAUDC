package Controller;

import Model.*;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class FacadeController {

    private FacadeModel model;

    public FacadeController(FacadeModel model) {
        this.model = model;
    }

    public boolean addSong(String location, String albumID) {
        try {
            if(model.addSong(location, albumID)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSong(SongInterface song) {

        return false;
    }

    public boolean deleteSong(SongInterface song) {

        return false;
    }

    public boolean followPlaylist() {

        return false;
    }

    public boolean unfollowPlaylist() {

        return false;
    }

    public boolean addSongToPlaylist(SongInterface song, String playlist) {
        PlaylistInterface p = null;
        for(PlaylistInterface pp : model.getUser().getPlaylists()) {
            if(pp.getName().equalsIgnoreCase(playlist)) {
                p = pp;
                break;
            }
        }
        try {
            if(model.addSongToPlaylist(song, p)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFromPlaylist(SongInterface song, String playlist) {
        PlaylistInterface p = null;
        for(PlaylistInterface pp : model.getUser().getPlaylists()) {
            if(pp.getName().equalsIgnoreCase(playlist)) {
                p = pp;
                break;
            }
        }
        try {
            if(model.deleteSongToPlaylist(song, p)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createPlaylist(String name) {
        PlaylistInterface p = new Playlist();
        p.setName(name);
        try {
            if(model.addPlaylist(p)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAlbum(String name) {
        try {
            if(model.createAlbum(model.getUser().getUsername(), name)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean followUser(AccountInterface acc) {
        try {
            if(model.followUser(acc)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unfollowUser(AccountInterface acc) {
        try {
            if(model.unfollowUser(acc)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isInFollowing(String username) {
        try {
            for(AccountInterface acc : model.getFollowed()) {
                if(acc.getUsername().equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isInFollowers(String username) {

        return false;
    }

    public boolean validAlbum(String name) {
        ObservableList<AlbumInterface> list = model.getUserAlbum(model.getUser().getUsername());
        for(AlbumInterface a : list) {
            if(a.getAlbumname().equals(name)) {
                if(a.getAlbumname().contains("- Single")) {
                    if(model.getAlbumSize(a.getAlbumname()) == 1) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
