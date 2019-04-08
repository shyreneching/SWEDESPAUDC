package Model;

import java.io.File;

public class ImportM4A implements ImportSong{
    public static SongInterface createSong(String filelocation) {

        AudioParserInterface parser = new AudioParser();
        SongInterface s = parser.getSongDetails(filelocation);
        File songFile = new File(filelocation);
        songFile = new File(songFile.getParent(), (songFile.getName()).replaceAll("\\.m4a$", ".mp3"));
        s.setSongfile(songFile);
        s.setSize(songFile.length());

        return s;
    }
}
