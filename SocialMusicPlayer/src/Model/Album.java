public class Album{
    private int albumID;
    private String albumname;
    private String artist;
    //private File albumArt;


    public Album(int albumID, String albumname, String artist) {
        this.albumID = albumID;
        this.albumname = albumname;
        this.artist = artist;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}