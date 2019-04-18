package ComparatorClass;

import Model.SongInterface;

import java.util.Comparator;

public class ArtistComparator implements Comparator<SongInterface> {

    @Override
    public int compare(SongInterface a1, SongInterface a2) {
        return a1.getArtist().trim().compareTo(a2.getArtist().trim());
    }
}
