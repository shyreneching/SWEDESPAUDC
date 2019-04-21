package Controller;

import Model.*;
import Mp3agic.InvalidDataException;
import Mp3agic.NotSupportedException;
import Mp3agic.UnsupportedTagException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
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

    public boolean updateSongName(String name, SongInterface song) {
        try {
            if(model.updateSongName(name, song)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAlbumArt(String albumid, File file) {
        if(model.uploadALbumArt(albumid, file)) {
            return true;
        }
        return false;
    }

    public boolean updateSongAlbum(String album, SongInterface song) {
        for(AlbumInterface a : model.getUserAlbum(model.getUser().getUsername())) {
            if(a.getAlbumname().contains("- Single")) {
                if(a.getAlbumname().trim().substring(0, a.getAlbumname().length()-8).trim().equalsIgnoreCase(album)) {
                    if(model.updateAlbumInSong(a.getAlbumID().trim(), song.getSongid().trim())) {
                        return true;
                    }
                }
            } else {
                if(a.getAlbumname().trim().equalsIgnoreCase(album)) {
                    if(model.updateAlbumInSong(a.getAlbumID().trim(), song.getSongid().trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean updateSongGenre(String genre, SongInterface song) {
        try {
            if(model.updateSongGenre(genre, song)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSongYear(int year, SongInterface song) {
        try {
            if(model.updateSongYear(year, song)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSong(SongInterface song) {
        try {
            if(model.deleteSong(song.getSongid())) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean likeSong(SongInterface song) {
        if(model.likeSong(song)) {
            return true;
        }
        return false;
    }

    public boolean unlikeSong(SongInterface song) {
        if(model.unlikeSong(song.getSongid())) {
            return true;
        }
        return false;
    }

    public boolean deleteAlbum(String name) {
        AlbumInterface album = null;
        try {
            album = model.getAlbumFromUser(name, model.getUser().getUsername());
            if(model.deleteAlbum(album)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePlaylist(String name) {
        PlaylistInterface p = null;
        try {
            p = model.getPlaylistFromUser(name, model.getUser().getUsername());
            if(model.deletePlaylist(p.getPlaylistid())) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean followPlaylist(String name, String artist) {
        PlaylistInterface p = null;
        try {
            p = model.getPlaylistFromUser(name, artist);
            if(model.followPlaylist(p)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unfollowPlaylist(String name, String artist) {
        PlaylistInterface p = null;
        try {
            p = model.getPlaylistFromUser(name, artist);
            if(model.unfollowPlaylist(p)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean inFollowedPlaylist(String name, String artist) {
        ObservableList<PlaylistInterface> followed = FXCollections.observableArrayList();
        try {
            followed = model.getFollowedPlaylistList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(PlaylistInterface p : followed) {
            if(p.getName().trim().equalsIgnoreCase(name) && p.getUser().trim().equalsIgnoreCase(artist)) {
                return true;
            }
        }
        return false;
    }

    public boolean updateAlbumName(String album, String oldAlbum) {
        AlbumInterface aa = null;
        try {
            aa = model.getAlbumFromUser(oldAlbum, model.getUser().getUsername());
            if(model.updateAlbumName(aa, album)) {
                System.out.println("true");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePlaylistName(String name, PlaylistInterface playlist) {
        if(model.updatePlaylistName(name, playlist.getPlaylistid().trim())) {
            return true;
        }
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
                ObservableList<PlaylistInterface> list = model.getUserPlaylist(acc.getUsername());
                for(PlaylistInterface p : list) {
                    if(inFollowedPlaylist(p.getName(), acc.getUsername())) {
                        unfollowPlaylist(p.getName(), acc.getUsername());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean highlightPlaylist(PlaylistInterface playlist) {
        try {
            if(model.highlightPlaylist(playlist)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean highlightFollowedPlaylist(PlaylistInterface playlist) {
        try {
            if(model.highlightFollowedPlaylist(playlist)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unhighlightPlaylist(PlaylistInterface playlist) {
        try {
            if(model.unhighlightPlaylist(playlist)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unhighlightFollowedPlaylist(PlaylistInterface playlist) {
        try {
            if(model.unhighlightFollowedPlaylist(playlist)) {
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

    public boolean inDisplayedPlaylist(PlaylistInterface playlist) {
        ObservableList<PlaylistInterface> list = FXCollections.observableArrayList();
        try {
            list = model.getDisplayedPlaylist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(PlaylistInterface p : list) {
            if(p.getPlaylistid().equalsIgnoreCase(playlist.getPlaylistid())) {
                return true;
            }
        }

        return false;
    }

    public boolean validAlbum(String name) {
        ObservableList<AlbumInterface> list = model.getUserAlbum(model.getUser().getUsername());
        for(AlbumInterface a : list) {
            if(a.getAlbumname().equals(name)) {
                return true;
            } else if(a.getAlbumname().contains("- Single")) {
                if(a.getAlbumname().substring(0, a.getAlbumname().length()-8).trim().equalsIgnoreCase(name)) {
                    if(model.getAlbumSize(a.getAlbumname()) == 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean validPlaylist(String name) {
        ObservableList<PlaylistInterface> list = FXCollections.observableArrayList();
        try {
            list = model.getUserPlaylist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(PlaylistInterface p : list) {
            if(p.getName().trim().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<PlaylistInterface> searchPlaylist(String key) {
        ObservableList<PlaylistInterface> temp = model.searchPlaylist(key);
        ObservableList<PlaylistInterface> filter = FXCollections.observableArrayList();
        for (PlaylistInterface p : temp) {
            if (isInFollowing(p.getUser()) || p.getUser().trim().equalsIgnoreCase(model.getUser().getUsername().trim())) {
                filter.add(p);
            }
        }
        return filter;
    }

    public boolean inLikedSong(SongInterface song) {
        ObservableList<SongInterface> list = model.getlikedSongs();
        for(SongInterface s : list) {
            if(s.getSongid().equalsIgnoreCase(song.getSongid())) {
                return true;
            }
        }
        return false;
    }
}
