package Model;

import java.io.File;
import java.sql.Timestamp;
import javafx.scene.control.Button;

public class Song implements SongInterface{
    
    private String songid, name, genre, artist, album, user, duration;
    private int year, trackNumber, length, timesplayed;
    private double size;
    private Timestamp date;
    private File songfile;
    private String filename, filelocation;
    private Button favorite, edit, add, del;

    public Song(){
        timesplayed = 0;
        favorite = new Button();
        edit = new Button();
        add = new Button();
        del = new Button();
        
        favorite.setPrefSize(30, 30);
        edit.setPrefSize(30, 30);
        add.setPrefSize(30, 30);
        del.setPrefSize(30, 30);
        
        favorite.setStyle("-fx-background-color: transparent; -fx-background-position: center; -fx-background-size: 100%;"
                + "-fx-background-image: url('/Files/starred.PNG');");
        edit.setStyle("-fx-background-color: transparent; -fx-background-position: center; -fx-background-size: 100%;"
                + "-fx-background-image: url('/Files/edit.png');");
        add.setStyle("-fx-background-color: transparent; -fx-background-position: center; -fx-background-size: 100%;"
                + "-fx-background-image: url('/Files/add.png');");
        del.setStyle("-fx-background-color: transparent; -fx-background-position: center; -fx-background-size: 100%;"
                + "-fx-background-image: url('/Files/delete.png');");
        
        favorite.setVisible(false);
        favorite.setDisable(true);
        edit.setVisible(false);
        edit.setDisable(true);
        add.setVisible(false);
        add.setDisable(true);
        del.setVisible(false);
        del.setDisable(true);
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        duration = String.format("%02d", length/60) + ":" + String.format("%02d", length%60);
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }


    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getTimesplayed() {
        return timesplayed;
    }

    public void setTimesplayed(int timesplayed) {
        this.timesplayed = timesplayed;
    }

    public File getSongfile() {
        return songfile;
    }

    public void setSongfile(File songfile) {
        this.songfile = songfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilelocation() {
        return filelocation;
    }

    public void setFilelocation(String filelocation) {
        this.filelocation = filelocation;
    }

    public String getDuration() {
        return duration;
    }

    public Button getFavorite() {
        return favorite;
    }

    public Button getEdit() {
        return edit;
    }

    public Button getAdd() {
        return add;
    }

    @Override
    public Button getDel() {
        return del;
    }
}
