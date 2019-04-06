package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Queue implements PlaylistInterface {

    private String playlistid, name;
    private ObservableList<SongInterface> songs = FXCollections.observableArrayList();
    private List<Integer> trackNo = new ArrayList<>();

    public int getNextTrack(int currTrack, boolean repeat){
        if (trackNo.indexOf(currTrack) != trackNo.size() - 1)
            return trackNo.get(trackNo.indexOf(currTrack) + 1);
        else {
            if(repeat == true)
                return trackNo.get(0);
            else
                return -1;
        }
    }

    public int getPrevTrack(int currTrack, boolean repeat){
        if (trackNo.indexOf(currTrack) != 0)
            return trackNo.get(trackNo.indexOf(currTrack) - 1);
        else{
            if(repeat == true)
                return trackNo.get(trackNo.size() - 1);
            else
                return -1;
        }
    }

    public void addSong(SongInterface song){
        trackNo.add(trackNo.size());
        songs.add(song);
    }

    public void addSong(ObservableList<SongInterface> songs){
        for (SongInterface song: songs) {
            trackNo.add(trackNo.size());
            songs.add(song);
        }
    }

    public ObservableList<SongInterface> getQueueList(){
        ObservableList<SongInterface> list = FXCollections.observableArrayList();
        for(int i: trackNo)
            list.add(songs.get(i));
        return list;
    }

    public String getPlaylistid() {
        return playlistid;
    }

    public void setPlaylistid(String playlistid) {
        this.playlistid = playlistid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<SongInterface> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<SongInterface> songs) {
        this.songs = songs;
        int i = 0;
        trackNo = new ArrayList<>();
        for(SongInterface song: songs){
            trackNo.add(i);
            i++;
        }
    }

    public void setShuffle(boolean shuffle) {
        if(shuffle == true){
            Collections.shuffle(trackNo);
        } else {
            Collections.sort(trackNo);
        }
    }
}
