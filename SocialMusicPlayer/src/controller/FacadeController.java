package Controller;

import Model.AccountInterface;
import Model.FacadeModel;
import Model.SongInterface;

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
}
