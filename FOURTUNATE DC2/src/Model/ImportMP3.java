package Model;

import java.io.File;

public class ImportMP3 implements ImportSong {
    public static SongInterface createSong(String filelocation) {
        AudioParserInterface parser = new AudioParser();
        SongInterface s = parser.getSongDetails(filelocation);
        File songFile = new File(filelocation);
        s.setSongfile(songFile);
        s.setSize(songFile.length());

        return s;
    }
}
