package Model;

public class CreateSongFromLocal {
    public static SongInterface CreateSong(String filelocation) {
        SongFactory songFactory = null;
        if(filelocation.contains(".mp3")){
            songFactory = new MP3ConcreteFactory();
        } else if (filelocation.contains(".m4a")){
            songFactory = new M4AConcreteFactory();
        }
        if(songFactory != null)
            return songFactory.SongFactoryMethod(filelocation);
        else
            return null;
    }
}
