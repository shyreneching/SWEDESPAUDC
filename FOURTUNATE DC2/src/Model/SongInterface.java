package Model;

import java.io.File;
import java.sql.Timestamp;
import javafx.scene.control.Button;

public interface SongInterface {


    public String getSongid();

    public void setSongid(String songid);

    public String getName();

    public void setName(String name);

    public String getGenre();

    public void setGenre(String genre);

    public String getArtist();

    public void setArtist(String artist);

    public String getAlbum();

    public void setAlbum(String album);

    public String getUser();

    public void setUser(String user);

    public int getYear();

    public void setYear(int year);

    public int getTrackNumber();

    public void setTrackNumber(int trackNumber);

    public int getLength();

    public void setLength(int length);

    public double getSize();

    public void setSize(double size);
    
    public String getDuration();
    
    public Button getFavorite();
    
    public Button getEdit();
    
    public Button getAdd();
    
    public Button getDel();

    public Timestamp getDate();

    public void setDate(Timestamp date);

    public int getTimesplayed();

    public void setTimesplayed(int timesplayed);

    public File getSongfile();

    public void setSongfile(File songfile);

    public String getFilename();

    public void setFilename(String filename);

    public String getFilelocation();

    public void setFilelocation(String filelocation);
}
