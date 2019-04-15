package Model;

import java.sql.Timestamp;

public interface AlbumInterface{

    public String getAlbumID();

    public void setAlbumID(String albumID);

    public String getAlbumname();

    public void setAlbumname(String albumname);

    public String getArtist();

    public void setArtist(String artist);

    public Timestamp getDate();

    public void setDate(Timestamp date);
}