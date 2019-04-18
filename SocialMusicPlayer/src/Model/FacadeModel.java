/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Mp3agic.InvalidDataException;
import Mp3agic.NotSupportedException;
import Mp3agic.UnsupportedTagException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import View.View;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;

public class FacadeModel {

    private AccountInterface user;
    private ObservableList<SongInterface> songs;
    private ObservableList<PlaylistInterface> groups;
    private SongInterface currentSong, selectedSong;
    private PlaylistInterface currentPlaylist, selectedPlaylist;

    private Service accountService;
    private Service playlistService;
    private Service songService;
    private Service recentlyplayedService;
    private Service albumService;
    private AudioParserInterface parser;
    /*private ObservableList<View> view;*/
    private View view;

    public FacadeModel() {
        songs = FXCollections.observableArrayList();
        groups = FXCollections.observableArrayList();
        accountService = new AccountService();
        playlistService = new PlaylistService();
        songService = new SongService();
        albumService = new AlbumService();
        parser = new AudioParser();
        currentPlaylist = new Playlist();
        recentlyplayedService = new RecentlyPlayedService();
    }

    public void attach(View view) {
        this.view = view;
    }

    public void update() {
        /*for(View v : view) {
            v.update();
        }*/
        view.update();
    }

    public PlaylistInterface getSelectedPlaylist() {
        return selectedPlaylist;
    }

    public void setSelectedPlaylist(PlaylistInterface selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    public PlaylistInterface getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setCurrentPlaylist(PlaylistInterface currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    public SongInterface getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(SongInterface currentSong) {
        this.currentSong = currentSong;
        update();
    }

    public AccountInterface getUser() {
        return user;
    }

    public AccountInterface getUser(String username) {
        try {
            return ((AccountService)accountService).getAccount(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUser(AccountInterface user) {
        this.user = user;
        update();
    }

    public ObservableList<AccountInterface> getAllUsers() {
        ObservableList<Object> list = FXCollections.observableArrayList();
        ObservableList<AccountInterface> accs = FXCollections.observableArrayList();
        try {
             list = accountService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Object o : list) {
            accs.add((AccountInterface)o);
        }
        return accs;
    }

    public ObservableList<SongInterface> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<SongInterface> songs) {
        this.songs = songs;
        update();
    }

    public ObservableList<PlaylistInterface> getGroups() {
        return groups;
    }

    public void setGroups(ObservableList<PlaylistInterface> groups) {
        this.groups = groups;
        update();
    }

    //METHOD CONNECTED TO DATABASE
    /*Pass the username and password of the login attempt
     * Compares the username and password if it matches anthing in the database
     * */
    public boolean login(String username, String password) throws SQLException {
        ObservableList<Object> accounts = null;
        accounts = accountService.getAll();
        if (accounts != null) {
            for (Object temp : accounts) {
                if (((Account) temp).getUsername().compareTo(username) == 0 && ((Account) temp).getPassword().compareTo(password) == 0) {
                    user = (AccountInterface) temp;
                    user.setPlaylists(getUserPlaylist());
                    user.setSongs(getUserSongs());
                    user.setFollowedPlaylist(((PlaylistService) playlistService).getFollowedPlaylist(user.getUsername()));
                    user.setFollowedPeople(getFollowed());
                    return true;
                }
            }
        }
        return false;
    }

    public void logout() {
        user = null;
        update();
    }

    //    public void addSongLocally(String filelocation) {
//        if (songs == null) {
//            songs = FXCollections.observableArrayList();
//        }
//        songs.add(CreateSongFromLocal.CreateSong(filelocation));
//        update();
//    }
//
//    public void removeSongLocally(SongInterface song) {
//        if (songs != null) {
//            if (groups != null) {
//                for (PlaylistInterface s : groups) {
//                    s.getSongs().remove(song);
//                }
//            }
//            songs.remove(song);
//        }
//        update();
//    }
//
//    public void addPlaylistLocally(PlaylistInterface playlist) {
//        this.groups.add(playlist);
//        update();
//    }
//
//    public void deletePlaylistLocally(PlaylistInterface playlist) {
//        this.groups.remove(playlist);
//        update();
//    }

    public ObservableList<AccountInterface> getFollowed() throws SQLException {
        ObservableList<Object> obs;
        ObservableList<AccountInterface> accounts = FXCollections.observableArrayList();
        obs = ((AccountService) accountService).getFollowed(user.getUsername());
        if(obs != null) {
            for (Object o : obs) {
                accounts.add((AccountInterface) o);
            }
        }
        return accounts;
    }

    public ObservableList<AccountInterface> getFollowed(String username) throws SQLException {
        ObservableList<Object> obs;
        ObservableList<AccountInterface> accounts = FXCollections.observableArrayList();
        obs = ((AccountService) accountService).getFollowed(username);
        if(obs != null) {
            for (Object o : obs) {
                accounts.add((AccountInterface) o);
            }
        }
        return accounts;
    }

    public ObservableList<AccountInterface> getFollowers() throws SQLException {
        ObservableList<Object> obs;
        ObservableList<AccountInterface> accounts = FXCollections.observableArrayList();
        obs = ((AccountService)accountService).getFollowers(user.getUsername());
        for(Object o : obs) {
            accounts.add((AccountInterface) o);
        }
        return accounts;
    }

    public ObservableList<AccountInterface> getFollowers(String username) throws SQLException {
        ObservableList<Object> obs;
        ObservableList<AccountInterface> accounts = FXCollections.observableArrayList();
        obs = ((AccountService)accountService).getFollowers(username);
        for(Object o : obs) {
            accounts.add((AccountInterface) o);
        }
        return accounts;
    }

    public File getSongArt(SongInterface s) throws SQLException{
        return getAlbumArt(s.getAlbum());
    }

    /*public void removeSongFromPlaylist(String playlist, SongInterface song) {
        for (PlaylistInterface s : groups) {
            if (s.getName().equalsIgnoreCase(playlist)) {
                s.getSongs().remove(song);
            }
        }
        update();
    }*/

    public PlaylistInterface getStarredSongs() throws SQLException {
        ObservableList<PlaylistInterface> playlists = getUserPlaylist();
        for (PlaylistInterface pp : playlists) {
            if (pp.getName().equalsIgnoreCase("Favorites")) {
                return pp;
            }
        }
        return null;
    }

    /*initialize the starred playlist to the user in the database*/
    public boolean createStarred() throws SQLException {
        ObservableList<Object> playlists = null;
        playlists = playlistService.getAll();

        PlaylistInterface p = new Playlist();
        p.setName("Favorites");

        if (playlists == null) {
            p.setPlaylistid("P01");
            if (((PlaylistService) playlistService).add(p, (Account) user)) {
                user.setPlaylists(getUserPlaylist());
                update();
                return true;
            }
        } else {
            p.setPlaylistid(String.format("P%02d", playlists.size() + 1));
            if (((PlaylistService) playlistService).add(p, (Account) user)) {
                user.setPlaylists(getUserPlaylist());
                update();
                return true;
            }
        }
        update();
        return false;
    }

    /*Adds one specific song to the starred */
    public boolean starSong(SongInterface s) throws SQLException {
        ObservableList<PlaylistInterface> playlists = getUserPlaylist();

        for (PlaylistInterface pp : playlists) {
            if (pp.getName().equals("Favorites")) {
                PlaylistInterface play = ((PlaylistService) playlistService).getPlaylist(pp.getPlaylistid(), user.getUsername());
                ObservableList<SongInterface> songs = play.getSongs();
                if (songs != null) {
                    for (SongInterface temp : songs) {
                        if (temp.getSongid().equals(s.getSongid())) {
                            return false;
                        }
                    }
                }
                boolean b = ((PlaylistService) playlistService).addSongPlaylist(s, pp.getPlaylistid());
                user.setPlaylists(getUserPlaylist());
                update();
                return b;
            }
        }
        return false;
    }

    /*Deletes one specific song in the starred playlist*/
    public boolean deleteStarred(SongInterface s) throws SQLException {
        ObservableList<PlaylistInterface> playlists = getUserPlaylist();

        for (PlaylistInterface pp : playlists) {
            if (pp.getName().equals("Starred")) {
                boolean b = ((PlaylistService) playlistService).deleteSongInPlaylist(s.getSongid(), pp.getPlaylistid());
                user.setPlaylists(getUserPlaylist());
                update();
                return b;
            }
        }
        return false;
    }

    /*Add/import one song to the database under the current user
     * */
//    public boolean addSong(String filelocation) throws SQLException {
    public boolean addSong(String filelocation, String albumid) throws SQLException {
        ObservableList<Object> songs = null;
        songs = songService.getAll();
        SongInterface s = CreateSongFromLocal.CreateSong(filelocation);
        s.setUser(this.user.getUsername());
        boolean exist = false;
        String id = null;
        s.setAlbum(albumid);
        s.setArtist(user.getUsername());

        if (songs.size() == 0) {
            s.setSongid("S01");
            if (songService.add(s)) {
                user.setSongs(getUserSongs());
                return true;
            }
        } else {
            for (Object temp : songs) {
                if (((SongInterface) temp).getName().compareToIgnoreCase(s.getName()) == 0
                        && ((SongInterface) temp).getArtist().compareToIgnoreCase(s.getArtist()) == 0
                        && ((SongInterface) temp).getAlbum().compareToIgnoreCase(s.getAlbum()) == 0) {
                    exist = true;
                    id = ((SongInterface) temp).getSongid();
                    break;
                }
            }
            if (exist) {
                boolean b = ((SongService) songService).addSongtoUser(id, user.getUsername());
                System.out.println(b);
                user.setSongs(getUserSongs());
                update();
                return b;
            } else {
                s.setSongid(String.format("S%02d", songs.size() + 1));
                if (songService.add(s)) {
                    user.setSongs(getUserSongs());
                    update();
                    return true;
                }
            }
        }

        update();
        return false;
    }

    /*Stanley was here*/
    public PlaylistInterface getPlaylistFromUser(String playlist, String username) throws SQLException {
        return ((PlaylistService)playlistService).getPlaylistName(playlist, username);
    }

    public AlbumInterface getAlbumFromUser(String albumName, String artist) throws SQLException{
        return ((AlbumService)albumService).getAlbumName(artist, albumName);
    }

    //add
    public boolean likeSong(SongInterface song){
        try {
            return ((SongService) songService).addSongtoUser(song.getSongid(), user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Returns the songs that the user liked
    public ObservableList<SongInterface> getlikedSongs(){
        ObservableList<SongInterface> original = FXCollections.observableArrayList();
        try {
            original = getUserSongs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<SongInterface> liked = FXCollections.observableArrayList();

        for(SongInterface s: original){
            if(!(s.getArtist().equals(user.getUsername()))){
                liked.add(s);
            }
        }
        return liked;
    }

    // Returns the songs that the user uploaded
    public ObservableList<SongInterface> getUserOwnedSong(){
        ObservableList<SongInterface> original = FXCollections.observableArrayList();
        try {
            original = getUserSongs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<SongInterface> liked = FXCollections.observableArrayList();

        for(SongInterface s: original){
            if(s.getArtist().equals(user.getUsername())){
                liked.add(s);
            }
        }
        return liked;
    }

    /*Returns list of playlists that user follows*/
    public ObservableList<PlaylistInterface> getFollowedPlaylistList() throws SQLException{
        PlaylistFactory playlistFactory = new FollowedPlaylistListConcreteFactory();
        return playlistFactory.playlistFactoryMethod(user.getUsername(), null);
    }

    /*Returns list of playlists that user can see*/
    public ObservableList<PlaylistInterface> getPlaylistUserCanFollow() throws SQLException {
        PlaylistFactory playlistFactory = new PlaylistUserCanFollowConcreteFactory();
        return playlistFactory.playlistFactoryMethod(user.getUsername(), null);
    }

    public SongInterface getSong(String songid) throws SQLException {
        return ((SongService) songService).getSong(songid, user.getUsername());
    }

    public ObservableList<SongInterface> getAlbumSong(String name) {
        AlbumInterface aa = null;
        ObservableList<Object> temp = FXCollections.observableArrayList();
        ObservableList<SongInterface> list = FXCollections.observableArrayList();
        try {
            aa = ((AlbumService)albumService).getAlbumName(user.getUsername(), name);
            temp = songService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Object o : temp) {
            if(((SongInterface)o).getAlbum().equalsIgnoreCase(aa.getAlbumname())) {
                list.add((SongInterface)o);
            }
        }
        return list;
    }

    public int getAlbumSize(String name) {
        AlbumInterface aa = null;
        ObservableList<Object> temp = FXCollections.observableArrayList();
        try {
            aa = ((AlbumService)albumService).getAlbumName(user.getUsername(), name);
            temp = songService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int counter = 0;
        for(Object o : temp) {
            if(((SongInterface)o).getAlbum().equalsIgnoreCase(aa.getAlbumname())) {
                counter++;
            }
        }
        return counter;
    }

    /*Deletes one specific song in the database using songid
     * Automatically deletes the song in the playlist that contains the song*/
    public boolean deleteSong(String songid) throws SQLException {
        boolean b = ((SongService) songService).delete(songid, (AccountInterface) user);
        user.setSongs(getUserSongs());
        update();
        return b;
    }

    /*Updates the title of the song given the new name and the song that wants to be changed*/
    public boolean updateSongName(String name, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s.setName(name);
        /*parser.editSongDetails(old, s);*/
        /*boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());*/
        ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        /*update();
          return b;*/
        return true;
    }

    /*Updates the genre of the song given the new name and the song that wants to be changed*/
    public boolean updateSongGenre(String genre, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s.setGenre(genre);
        /*parser.editSongDetails(old, s);*/
        /*boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());*/
        ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        /*update();
        return b;*/
        return true;
    }

    /*Updates the artist of the song given the new name and the song that wants to be changed*/
    public boolean updateSongArtist(String artist, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s.setArtist(artist);
        parser.editSongDetails(old, s);
        /*boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());*/
        ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        /*update();
          return b;*/
        return true;
    }

    /*Updates the album of the song given the new name and the song that wants to be changed*/
    public boolean updateSongAlbum(String album, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s.setAlbum(album);
        /*parser.editSongDetails(old, s);*/
        /*boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());*/
        ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        /*update();
        return b;*/
        return true;
    }

    /*Updates the year of the song given the new name and the song that wants to be changed*/
    public boolean updateSongYear(int year, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s.setYear(year);
        /*parser.editSongDetails(old, s);*/
        /*boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());*/
        ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        /*update();
        return b;*/
        return true;
    }

    public boolean updateAlbum(AlbumInterface album, String albumname) throws SQLException {
        return albumService.update(albumname, album);
    }

    public boolean updateAlbumName(AlbumInterface album, String albumname) {
        try {
            return((AlbumService)albumService).updateAlbumName(album.getAlbumID(), albumname);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAlbumInSong(String albumid, String songid) {
        try {
            return ((AlbumService)albumService).updateAlbumInSong(albumid, songid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*Updates the track number of the song given the new name and the song that wants to be changed*/
    public boolean updateSongTrackNumber(int trackNumber, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s.setTrackNumber(trackNumber);
        parser.editSongDetails(old, s);
        boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        update();
        return b;
    }

    /*Updates the albumart of the song given the new name and the song that wants to be changed*/
    public boolean updateSongCover(File img, SongInterface s) throws SQLException, UnsupportedTagException, NotSupportedException, InvalidDataException, IOException {
        SongInterface old = s;
        s = parser.setSongImage(old, img);
        parser.editSongDetails(old, s);
        boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        update();
        return b;
    }

    /*Updates the filename of the song given the new name and the song that wants to be changed*/
    public boolean updateSongFileName(String filename, SongInterface s) throws SQLException {
        s.setFilename(filename);
        boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        update();
        return b;
    }

    /*Updates the location the song will be save given the new name and the song that wants to be changed*/
    public boolean updateSongFileLocation(String filelocation, SongInterface s) throws SQLException {
        s.setFilename(filelocation);
        boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        update();
        return b;
    }

    public File getsongImage(SongInterface s) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        return parser.getSongImage(s);
    }


    /*Updates the album of the song given the new name and the song that wants to be changed*/
//    public boolean playSong(SongInterface s) throws SQLException {
    public File playSong(SongInterface s) throws SQLException {
        s.setTimesplayed(s.getTimesplayed() + 1);
        setCurrentSong(s);
//        if(user != null) {
//            boolean b = ((SongService) songService).update(s.getSongid(), s, s.getUser());
        ((SongService) songService).update(s.getSongid(), s, s.getUser());
        user.setSongs(getUserSongs());
        RecentlyPlayedInterface recentlyPlayed = new RecentlyPlayed();
        recentlyPlayed.setIdsong(s.getSongid());
        recentlyPlayed.setUsername(user.getUsername());
        recentlyplayedService.add(recentlyPlayed);
        /*update();*/
        /*return s.getSongfile();*/
        return ((SongService)songService).getSongFile(s.getSongid());
//            return b;
//        }
//        update();
//        return true;
    }

    public ObservableList<PlaylistInterface> displayPlaylist() throws SQLException {
        return ((PlaylistService) playlistService).getUserDisplayedPlaylist(user.getUsername());
    }

    /*Returns all the songs in the database*/
    public ObservableList<SongInterface> getAllSong() throws SQLException {
        ObservableList<Object> o = songService.getAll();
        ObservableList<SongInterface> songs = FXCollections.observableArrayList();
        if (o != null) {
            for (Object temp : o) {
                songs.add((SongInterface) temp);
            }
            return songs;
        }
        return null;
    }

    /*Returns the songs that the user imported*/
    public ObservableList<SongInterface> getUserSongs() throws SQLException {
        ObservableList<SongInterface> songs = ((SongService) songService).getUserSong(user.getUsername());
        if (songs != null) {
            return songs;
        }
        return null;
    }

    /*Adds the playlist in the database*/
    public boolean addPlaylist(PlaylistInterface p) throws SQLException {
        ObservableList<Object> playlists = null;
        playlists = playlistService.getAll();

        if (playlists.isEmpty()) {
            p.setPlaylistid("P01");
            if (((PlaylistService) playlistService).add(p, /*(AccountInterface)*/ user)) {
                user.setPlaylists(getUserPlaylist());
                /*update();*/
                return true;
            }
        } else {
            p.setPlaylistid(String.format("P%02d", playlists.size() + 1));
            if (((PlaylistService) playlistService).add(p, /*(AccountInterface)*/ user)) {
                user.setPlaylists(getUserPlaylist());
                /*update();*/
                return true;
            }
        }
        /*update();*/
        return false;
    }

    public ObservableList<AlbumInterface> getUserAlbum(String username) {
        try {
            return ((AlbumService)albumService).getUserAlbum(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createAlbum(String artist, String albumName) throws SQLException{
        ObservableList<Object> albums = albumService.getAll();
        AlbumInterface a = new Album();
        a.setAlbumname(albumName);
        a.setArtist(artist);
        if (albums.isEmpty()) {
            a.setAlbumID("A01");
            if ((albumService).add(a)) {
                update();
                return true;
            }
        } else {
            a.setAlbumID(String.format("A%02d", albums.size() + 1));
            if (albumService.add(a)) {
                update();
                return true;
            }
        }
        update();
        return false;
    }

    public boolean deleteAlbum(AlbumInterface album) throws SQLException {
        if(albumService.delete(album.getAlbumID())) {
            return true;
        }
        return false;
    }

    public boolean editAlbum(AlbumInterface newalbum) throws SQLException {
        if(albumService.update(newalbum.getAlbumID(), newalbum)) {
            return true;
        }
        return false;
    }

    public boolean uploadALbumArt(String albumid, File albumart){
        try {
            return ((AlbumService) albumService).uploadALbumArt(albumid, albumart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<SongInterface> searchSong(String key) {
        ObservableList<Object> temp = FXCollections.observableArrayList();
        ObservableList<SongInterface> list = FXCollections.observableArrayList();
        try {
            temp = songService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Object o : temp) {
            if(((SongInterface)o).getName().toLowerCase().contains(key.toLowerCase())) {
                list.add((SongInterface)o);
            }
        }
        return list;
    }

    public ObservableList<AccountInterface> searchArtist(String key) {
        ObservableList<AccountInterface> list = FXCollections.observableArrayList();
        try {
            list = ((AccountService)accountService).searchArtist(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<AccountInterface> searchListener(String key) {
        ObservableList<AccountInterface> list = FXCollections.observableArrayList();
        try {
            list = ((AccountService)accountService).searchListener(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<PlaylistInterface> searchPlaylist(String key) {
        ObservableList<Object> temp = FXCollections.observableArrayList();
        ObservableList<PlaylistInterface> list = FXCollections.observableArrayList();
        try {
            temp = playlistService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Object o : temp) {
            if(((PlaylistInterface)o).getName().toLowerCase().contains(key.toLowerCase())) {
                list.add((PlaylistInterface)o);
            }
        }
        return list;
    }

    public ObservableList<AlbumInterface> searchAlbum(String key) {
        ObservableList<AlbumInterface> list = FXCollections.observableArrayList();
        try {
            list = ((AlbumService)albumService).searchAlbum(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
        /*ObservableList<Object> temp = FXCollections.observableArrayList();
        ObservableList<AlbumInterface> list = FXCollections.observableArrayList();
        try {
            temp = albumService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Object o : temp) {
            if(((AlbumInterface)o).getAlbumname().toLowerCase().contains(key.toLowerCase())) {
                list.add((AlbumInterface)o);
            }
        }
        return list;*/
    }

    public File getAlbumArt(String albumid) throws SQLException{
        return ((AlbumService) albumService).getAlbumArt(albumid);
    }

    /*Adds one song to the playlist*/
    public boolean addSongToPlaylist(SongInterface s, PlaylistInterface p) throws SQLException {
        PlaylistInterface playlist = ((PlaylistService) playlistService).getPlaylist(p.getPlaylistid(), user.getUsername());
        ObservableList<SongInterface> songs = playlist.getSongs();

        if (songs != null) {
            for (SongInterface temp : songs) {
                if (temp.getSongid().equals(s.getSongid())) {
                    return false;
                }
            }
        }

        ((PlaylistService) playlistService).addSongPlaylist(s, p.getPlaylistid());
        user.setPlaylists(getUserPlaylist());
        return true;
    }

    /*Deletes one song in the playlist*/
    public boolean deleteSongToPlaylist(SongInterface s, PlaylistInterface p) throws SQLException {
        ((PlaylistService) playlistService).deleteSongInPlaylist(s.getSongid(), p.getPlaylistid());
        user.setPlaylists(getUserPlaylist());
        /*update();*/
        return true;
    }

    public boolean updatePlaylist(PlaylistInterface p) throws SQLException {
        return playlistService.update(p.getPlaylistid(), p);
    }

    public boolean makePlaylistPrivate(PlaylistInterface p) throws SQLException {
        p.setStatus("private");
        return playlistService.update(p.getPlaylistid(), p);
    }

    public boolean makePlaylistPublic(PlaylistInterface p) throws SQLException {
        p.setStatus("public");
        return playlistService.update(p.getPlaylistid(), p);
    }

    public boolean makeFollowedPlaylistPrivate(PlaylistInterface p) throws SQLException {
        p.setStatus("private");
        return ((PlaylistService)playlistService).updateFollowedPlaylist(p.getPlaylistid(), (FollowedPlaylist) p);
    }

    public boolean makeFollowedPlaylistPublic(PlaylistInterface p) throws SQLException {
        p.setStatus("public");
        return ((PlaylistService)playlistService).updateFollowedPlaylist(p.getPlaylistid(), (FollowedPlaylist) p);
    }

    /*Takes specifc playlist*/
    public PlaylistInterface getPlaylist(String playlistid) throws SQLException {
        return ((PlaylistService) playlistService).getPlaylist(playlistid, user.getUsername());
    }


    /*Takes all the playlist the user created*/
    public ObservableList<PlaylistInterface> getUserPlaylist() throws SQLException {
        PlaylistFactory playlistFactory = new UserPlaylistConcreteFactory();
        ObservableList<PlaylistInterface> playlists = playlistFactory.playlistFactoryMethod(user.getUsername(), null);
        if (playlists != null) {
            user.setPlaylists(playlists);
            return playlists;
        }
        return null;
    }

    /*Takes all the playlist the user created*/
    public ObservableList<PlaylistInterface> getUserPlaylist(String username) throws SQLException {
        PlaylistFactory playlistFactory = new UserPlaylistConcreteFactory();
        ObservableList<PlaylistInterface> playlists = playlistFactory.playlistFactoryMethod(username, null);
        if (playlists != null) {
            user.setPlaylists(playlists);
            return playlists;
        }
        return null;
    }

    /*Returns list of playlist grouped by album*/
    public ObservableList<PlaylistInterface> getAlbumPlaylist() throws SQLException {
        PlaylistFactory playlistFactory = new AlbumPlaylistConcreteFactory();
        if (user != null) {
            ObservableList<PlaylistInterface> pp = playlistFactory.playlistFactoryMethod(user.getUsername(), null);
            return pp;
        } else {
            return playlistFactory.playlistFactoryMethod(null, songs);
        }
    }

    /*Returns list of playlist grouped by artist*/
    public ObservableList<PlaylistInterface> getArtistPlaylist() throws SQLException {
        PlaylistFactory playlistFactory = new ArtistPlaylistConcreteFactory();
        if (user != null) {
            return playlistFactory.playlistFactoryMethod(user.getUsername(), null);
        } else {
            return playlistFactory.playlistFactoryMethod(null, songs);
        }
    }

    /*Returns list of playlist grouped by year*/
    public ObservableList<PlaylistInterface> getYearPlaylist() throws SQLException {
        PlaylistFactory playlistFactory = new YearPlaylistConcreteFactory();
        if (user != null) {
            return playlistFactory.playlistFactoryMethod(user.getUsername(), null);
        } else {
            return playlistFactory.playlistFactoryMethod(null, songs);
        }
    }

    /*Returns list of playlist grouped by genre*/
    public ObservableList<PlaylistInterface> getGenrePlaylist() throws SQLException {
        PlaylistFactory playlistFactory = new GenrePlaylistConcreteFactory();
        if (user != null) {
            return playlistFactory.playlistFactoryMethod(user.getUsername(), null);
        } else {
            return playlistFactory.playlistFactoryMethod(null, songs);
        }
    }

    /*Deletes the playlist in the database*/
    public boolean deletePlaylist(String playlistid) throws SQLException {
        /*boolean b = playlistService.delete(playlistid);*/
        playlistService.delete(playlistid);
        user.setPlaylists(getUserPlaylist());
        /*update();
        return b;*/
        return true;
    }

    public boolean updatePlaylistName(String name, String id) {
        try {
            return ((PlaylistService)playlistService).updatePlaylistName(name, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*Edits the name of the playlist*/
    public boolean renamePlaylist(String playlistname, PlaylistInterface p) throws SQLException {
        p.setName(playlistname);
        /*boolean b = playlistService.update(p.getPlaylistid(), p);*/
        playlistService.update(p.getPlaylistid(), p);
        user.setPlaylists(getUserPlaylist());
        /*update();
        return b;*/
        return true;
    }

    /*Edits the name of the user*/
    public boolean updateNameofUser(String name, AccountInterface a) throws SQLException {
        a.setName(name);
        boolean b = accountService.update(a.getUsername(), a);
        update();
        return b;
    }

    /*Edits the password of the user*/
    public boolean updateUserPassword(String password, AccountInterface a) throws SQLException {
        a.setPassword(password);
        boolean b = accountService.update(a.getUsername(), a);
        update();
        return b;
    }

    /*public void addUnregisteredSongs() throws SQLException {
        for (SongInterface ss : songs) {
            addSong(ss.getFilelocation());
        }
    }

    public void addUnregisteredPlaylist() throws SQLException {
        for (PlaylistInterface pp : groups) {
            for (SongInterface s : pp.getSongs()) {
                s.setSongid(((SongService) songService).getSongName(s.getName(), s.getAlbum(), s.getArtist()).getSongid());
            }
            addPlaylist(pp);
        }
    }*/

    public ObservableList<PlaylistInterface> getPublicPlaylist() throws SQLException {
        return ((PlaylistService)playlistService).getPublicPlaylist();
    }

    public ObservableList<PlaylistInterface> getUserPublicPlaylist() throws SQLException {
        return ((PlaylistService) playlistService).getUserPublicPlaylist(user.getUsername());
    }

    public ObservableList<PlaylistInterface> getUserPrivatePlaylist() throws SQLException {
        return ((PlaylistService) playlistService).getUserPrivatePlaylist(user.getUsername());
    }

    /*Register/sign-up a new user and saves it to the database
     * Needs to pass account data type with*/
    public boolean createUser(Account a) throws SQLException {
        ObservableList<Object> accounts = null;
        accounts = accountService.getAll();

//        if (accounts == null) {
//            if (accountService.add(a)) {
//                user = (AccountInterface) a;
//				createStarred();
//				if(songs != null)
//				    addUnregisteredSongs();
//				if(groups != null)
//				    addUnregisteredPlaylist();
//                return true;
//            }
//
//        } else {
        for (Object temp : accounts) {
            if (((Account) temp).getUsername().compareTo(a.getUsername()) == 0) {
                return false;
            }
        }
        if (accountService.add(a)) {
            user = (AccountInterface) a;
            createStarred();
            /*if(songs != null)
                addUnregisteredSongs();
            if(groups != null)
                addUnregisteredPlaylist();*/
            return true;
        }
        // }
        return true;
    }
    //METHOD CONNECTED TO DATABASE

    public ObservableList<SongInterface> getMostPlayed() {
        ObservableList<SongInterface> mostplayed = FXCollections.observableArrayList();
        for (SongInterface song : songs) {
            mostplayed.add(song);
        }
        mostplayed.sort(Comparator.comparing(SongInterface::getTimesplayed));

        mostplayed = reverseList(mostplayed);
        return mostplayed;
    }

    private ObservableList<SongInterface> reverseList(ObservableList<SongInterface> list) {
        ObservableList<SongInterface> reverse = FXCollections.observableArrayList();
        for (int i = list.size() - 1; i >= 0; i--) {
            reverse.add(list.get(i));
        }
        return reverse;
    }

    public boolean followUser(AccountInterface person) throws SQLException {
        /*return ((AccountService) accountService).followPeople(user, person);*/
        ((AccountService) accountService).followPeople(user, person);
        return true;
    }

    public boolean unfollowUser(AccountInterface person) throws SQLException {
        /*return ((AccountService) accountService).unfollowPeople(user, person);*/
        ((AccountService) accountService).unfollowPeople(user, person);
        return true;
    }

    public boolean followPlaylist(PlaylistInterface playlist) throws SQLException {
        return ((PlaylistService) playlistService).followPlaylist(playlist.getPlaylistid(), user.getUsername());
    }

    public boolean unfollowPlaylist(PlaylistInterface playlist) throws SQLException {
        return ((PlaylistService) playlistService).unfollowfollowPlaylist(playlist.getPlaylistid(), user.getUsername());
    }

    public SongInterface getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(SongInterface selectedSong) {
        this.selectedSong = selectedSong;
    }
}
