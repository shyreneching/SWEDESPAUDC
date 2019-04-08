package Model;

public class M4AConcreteFactory extends SongFactory {
    @Override
    public SongInterface SongFactoryMethod(String filelocation) {
        return ImportM4A.createSong(filelocation);
    }
}
