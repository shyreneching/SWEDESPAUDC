package Controller;

import Model.*;
import Mp3agic.InvalidDataException;
import Mp3agic.NotSupportedException;
import Mp3agic.UnsupportedTagException;
import javafx.collections.ObservableList;

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

    public boolean followPlaylist() {

        return false;
    }

    public boolean unfollowPlaylist() {

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
}
