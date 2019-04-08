package Model;

public class MP3ConcreteFactory extends SongFactory {

    @Override
    public SongInterface SongFactoryMethod(String filelocation) {
        return ImportMP3.createSong(filelocation);
    }
}
