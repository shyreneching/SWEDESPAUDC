package Model;

import javafx.collections.ObservableList;

public interface AccountInterface {

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);

    public String getName();

    public void setName(String name);

    public ObservableList<PlaylistInterface> getPlaylists();

    public void setPlaylists(ObservableList<PlaylistInterface> playlists);

    public ObservableList<SongInterface> getSongs();

    public void setSongs(ObservableList<SongInterface> songs);
}
