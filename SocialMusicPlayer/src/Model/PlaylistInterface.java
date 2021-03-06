package Model;

import javafx.collections.ObservableList;

import java.sql.Timestamp;

public interface PlaylistInterface {

    public String getPlaylistid();

    public void setPlaylistid(String playlistid);

    public String getName();

    public void setName(String name);

    public ObservableList<SongInterface> getSongs();

    public void setSongs(ObservableList<SongInterface> songs);

    public String getUser();

    public void setUser(String user);

    public String getStatus();

    public void setStatus(String status);

    public boolean isDisplay();

    public void setDisplay(boolean display);

    public Timestamp getDate();

    public void setDate(Timestamp date);
}
