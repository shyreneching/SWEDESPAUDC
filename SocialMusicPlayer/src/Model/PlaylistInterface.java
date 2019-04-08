package Model;

import javafx.collections.ObservableList;

public interface PlaylistInterface {

    public String getPlaylistid();

    public void setPlaylistid(String playlistid);

    public String getName();

    public void setName(String name);

    public ObservableList<SongInterface> getSongs();

    public void setSongs(ObservableList<SongInterface> songs);
}
