package Model;

import Mp3agic.InvalidDataException;
import Mp3agic.NotSupportedException;
import Mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

public interface AudioParserInterface {
    public SongInterface getSongDetails(String location);

    // Returns the album art of the song in File Format
    // Need to pass song with album art
    // Returns null if no album art
    public File getSongImage(SongInterface s) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException;

    public SongInterface setSongImage(SongInterface s, File image) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException;

    public SongInterface editSongDetails(SongInterface original, SongInterface changed) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException;
}
