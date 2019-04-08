package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public abstract class PlaylistFactory {
    protected PlaylistList playlistList;

    public abstract ObservableList<PlaylistInterface> playlistFactoryMethod(String username, ObservableList<SongInterface> s) throws SQLException;
}
