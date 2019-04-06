package Model;

import Mp3agic.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;

import jaco.mp3.player.MP3Player;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.imageio.ImageIO;

public class AudioParser implements AudioParserInterface{

    /*Sets
    * song name
    * song artist
    * song album
    * song genre
    * song release date
    * song tracknumber
    * song duration
    * song location
    * song filename
    * */
    public SongInterface getSongDetails(String location) {
        String fileLocation = location;
        SongInterface s = new Song();

        try {
            InputStream input = new FileInputStream(new File(fileLocation));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            /*// List all metadata
            String[] metadataNames = metadata.names();

            for(String name : metadataNames){
                System.out.println(name + ": " + metadata.get(name));
            }*/

            // Retrieve the necessary info from metadata
            // Names - title, xmpDM:artist etc. - mentioned below may differ based

            s.setName(metadata.get("title"));
            s.setArtist(metadata.get("xmpDM:artist"));
            s.setAlbum(metadata.get("xmpDM:album"));
            s.setGenre(metadata.get("xmpDM:genre"));
            if (metadata.get("xmpDM:releaseDate") != null && !(metadata.get("xmpDM:releaseDate").equals("")))
                s.setYear(Integer.parseInt(metadata.get("xmpDM:releaseDate")));
            if (metadata.get("xmpDM:trackNumber") != null && !(metadata.get("xmpDM:trackNumber").equals("")))
                s.setTrackNumber(Integer.parseInt(metadata.get("xmpDM:trackNumber")));
            Double d = Double.parseDouble(metadata.get("xmpDM:duration"));
            d =  d/1000;
            s.setLength(d.intValue());
            s.setFilelocation(location);
            s.setFilename("src/Music/" + s.getArtist() + "-" + s.getName()+ ".mp3");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | SAXException | TikaException | NumberFormatException e) {
            e.printStackTrace();
        }
        return s;
    }

    // Returns the album art of the song in File Format
    // Need to pass song with album art
    // Returns null if no album art
    public File getSongImage(SongInterface s) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        Mp3File music = new Mp3File(s.getFilelocation());
        File outputfile = new File("src/Music/" + s.getArtist() + "_" + s.getAlbum() + ".jpg");
        if (music.hasId3v2Tag()){
            ID3v2 id3v2tag = music.getId3v2Tag();
            byte[] imageData = id3v2tag.getAlbumImage();
            if (imageData != null){
                //converting the bytes to an image
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                ImageIO.write(img, "jpg", outputfile);
                return outputfile;
            }
            else{
                File file = new File("src/files/album_art.png");
                byte[] defaultImage = Files.readAllBytes(file.toPath());
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(defaultImage));
                ImageIO.write(img, "jpg", outputfile);
                return outputfile;
            }
        }
         return null;
    }

    public SongInterface setSongImage(SongInterface s, File image) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        Mp3File mp3file = new Mp3File(s.getFilelocation());
        ID3v2 id3v2Tag;
        id3v2Tag = new ID3v24Tag();
        mp3file.setId3v2Tag(id3v2Tag);
        byte[] imageData = Files.readAllBytes(image.toPath());
        id3v2Tag.setAlbumImage(imageData, ".jpg");
        id3v2Tag.setTrack(s.getTrackNumber() + "");
        id3v2Tag.setArtist(s.getArtist());
        id3v2Tag.setTitle(s.getName());
        id3v2Tag.setAlbum(s.getAlbum());
        id3v2Tag.setYear(s.getYear() + "");
        id3v2Tag.setGenre(ID3v1Genres.matchGenreDescription(s.getGenre()));
        /*id3v2Tag.setComment("Some comment");
        id3v2Tag.setLyrics("Some lyrics");
        id3v2Tag.setComposer("The Composer");
        id3v2Tag.setPublisher("A Publisher");
        id3v2Tag.setOriginalArtist("Another Artist");*/
        id3v2Tag.setAlbumArtist(s.getArtist());
        //temporarily sets the file name to "temp.mp3"
        mp3file.save("src/Music/temp.mp3");
        //gets the old file and deletes it
        File file = new File(s.getFilename());
        if (file != null)
            file.delete();
        //renames the temp file to the actual file
        File tempfile =new File("src/Music/temp.mp3");
        File newfile =new File(s.getFilename());
        s.setSongfile(tempfile);
        tempfile.renameTo(newfile);
        return s;
    }

    public SongInterface editSongDetails(SongInterface original, SongInterface changed) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        Mp3File mp3file = new Mp3File(original.getFilelocation());
        ID3v2 id3v2Tag;
        id3v2Tag = new ID3v24Tag();
        mp3file.setId3v2Tag(id3v2Tag);
        byte[] imageData = Files.readAllBytes(getSongImage(changed).toPath());

        if (mp3file.hasId3v1Tag()) {
            mp3file.removeId3v1Tag();
        }
        if (mp3file.hasId3v2Tag()) {
            mp3file.removeId3v2Tag();
        }
        if (mp3file.hasCustomTag()) {
            mp3file.removeCustomTag();
        }

        id3v2Tag.setAlbumImage(imageData, ".jpg");
        id3v2Tag.setTrack(changed.getTrackNumber() + "");
        id3v2Tag.setArtist(changed.getArtist());
        id3v2Tag.setTitle(changed.getName());
        id3v2Tag.setAlbum(changed.getAlbum());
        id3v2Tag.setYear(changed.getYear() + "");
        id3v2Tag.setGenre(ID3v1Genres.matchGenreDescription(changed.getGenre()));
        /*id3v2Tag.setComment("Some comment");
        id3v2Tag.setLyrics("Some lyrics");
        id3v2Tag.setComposer("The Composer");
        id3v2Tag.setPublisher("A Publisher");
        id3v2Tag.setOriginalArtist("Another Artist");*/
        id3v2Tag.setAlbumArtist(changed.getArtist());
        //temporarily sets the file name to "temp.mp3"
        mp3file.save("src/Music/temp.mp3");

        //takes the file again, place it in a song file and delete the mp3 file
        File file = new File("src/Music/temp.mp3");
        SongInterface s = getSongDetails("src/Music/temp.mp3");
        s.setSongfile(file);
        s.setUser(changed.getUser());
        file.delete();
        return s;
    }


    /*
    public static void main(String[] args) {
        AudioParser ap = new AudioParser();
        String fileLocation = "C:/Users/Shyrene/Downloads/Music/Taeyeon - I’m the Greatest.mp3";

        try {
            InputStream input = new FileInputStream(new File(fileLocation));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            // List all metadata
            String[] metadataNames = metadata.names();

            for(String name : metadataNames){
                System.out.println(name + ": " + metadata.get(name));
            }

            // Retrieve the necessary info from metadata
            // Names - title, xmpDM:artist etc. - mentioned below may differ based
            System.out.println("----------------------------------------------");
            System.out.println("Title: " + metadata.get("title"));
            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
            System.out.println("Composer : "+metadata.get("xmpDM:composer"));
            System.out.println("Genre : "+metadata.get("xmpDM:genre"));
            System.out.println("Album : "+metadata.get("xmpDM:album"));
            System.out.println("Track Number : "+metadata.get("xmpDM:trackNumber"));
            double i = Double.parseDouble(metadata.get("xmpDM:duration"));
            i = i/1000;
            //int k  = Integer.parseInt(k);
            System.out.println("Duration : "+ (int)i);



            SongInterface s = ap.getSongDetails("C:/Users/Shyrene/Downloads/Music/Taeyeon - I’m the Greatest.mp3");
            System.out.println(s.getLength());

            //MusicPlayerDB db = new MusicPlayerDB();
            SongService ss = new SongService();
            //s.setSongid("S01");
            //s.setFilelocation("C:/Users/Shyrene/Downloads/Music/Taeyeon - I’m the Greatest.mp3");
            //s.setSongfile(new File(fileLocation));
            //s.setUser("A01");
            //ss.add(s);
            SongInterface song =  ss.getSong("S01", "A01");
            File f = song.getSongfile();
            System.out.println(f.getName());

            Mp3File music = new Mp3File(song.getFilelocation());
            if (music.hasId3v2Tag()){
                ID3v2 id3v2tag = music.getId3v2Tag();
                byte[] imageData = id3v2tag.getAlbumImage();
                //converting the bytes to an image
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                File outputfile = new File("image.jpg");
                ImageIO.write(img, "jpg", outputfile);
            }



            Mp3File mp3file = new Mp3File(song.getFilelocation());
            if (mp3file.hasId3v1Tag()) {
                mp3file.removeId3v1Tag();
            }
            if (mp3file.hasId3v2Tag()) {
                mp3file.removeId3v2Tag();
            }
            if (mp3file.hasCustomTag()) {
                mp3file.removeCustomTag();
            }
            ID3v2 id3v2Tag;
            id3v2Tag = new ID3v24Tag();
            mp3file.setId3v2Tag(id3v2Tag);

            id3v2Tag.setTrack("5");
            id3v2Tag.setArtist("Taeyeon");
            id3v2Tag.setTitle("The Title");
            id3v2Tag.setAlbum("The Album");
            id3v2Tag.setYear("2001");
            id3v2Tag.setGenre(12);
            id3v2Tag.setComment("Some comment");
            id3v2Tag.setLyrics("Some lyrics");
            id3v2Tag.setComposer("The Composer");
            id3v2Tag.setPublisher("A Publisher");
            id3v2Tag.setOriginalArtist("Another Artist");
            id3v2Tag.setAlbumArtist("An Artist");
            id3v2Tag.setCopyright("Copyright");
            id3v2Tag.setUrl("http://foobar");
            id3v2Tag.setEncoder("The Encoder");
            ID3v2 id3v2tag = music.getId3v2Tag();
            byte[] imageData = id3v2tag.getAlbumImage();
            id3v2Tag.setAlbumImage(imageData, ".jpg");
            mp3file.save("MyMp3File.mp3");

            File newFile = new File("C:/Users/Shyrene/IdeaProjects/MusicPlayer/(G)I-DLE - Senorita.m4a");



            SongInterface sounds = ap.getSongDetails(newFile.getAbsolutePath());
            sounds.setSongfile(newFile);
            sounds.setFilelocation(newFile.getAbsolutePath());
            sounds.setFilename(sounds.getArtist() + "-" + sounds.getName());
            sounds.setSongid("S02");
            sounds.setSongfile(newFile);
            sounds.setUser("A02");
            ss.add(sounds);
            ap.getSongImage(sounds);
            /*InputStream ios = new FileInputStream(ap.getSongImage(sounds));
            byte[] fileContent = Files.readAllBytes(ap.getSongImage(sounds).toPath());
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileContent));
            ImageIO.write(image, "jpg", newFile);*//*

            File ff = new File("../MusicPlayer/MyMp3File.mp3");
            SongInterface newsong = ap.getSongDetails("C:/Users/Shyrene/IdeaProjects/MusicPlayer/MyMp3File.mp3");
            newsong.setFilename("MyMp3File.mp3");
            SongInterface soo = ap.setSongImage(newsong, ap.getSongImage(sounds));
            System.out.println(soo.getFilename());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }
    }*/
    /*
    public static void main(String args[]) throws SQLException {
        String filelocation = "src/Music/Bea Miller - Fire N Gold.mp3";
        SongInterface song = CreateSongFromLocal.CreateSong(filelocation);
        song.setSongid("S01");
        song.setUser("User");

        SongService songService = new SongService();
        songService.add(song);

        MP3Player mp3Player = new MP3Player();
        for(Object s : songService.getAll()){
            mp3Player.addToPlayList(((SongInterface)s).getSongfile());
        }
        mp3Player.play();
        while (true){

        }
    }
*/
}
