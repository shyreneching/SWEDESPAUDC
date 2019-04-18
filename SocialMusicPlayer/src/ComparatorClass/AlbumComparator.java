package ComparatorClass;

import Model.SongInterface;

import java.util.Comparator;

public class AlbumComparator implements Comparator<SongInterface> {
    @Override
    public int compare(SongInterface o1, SongInterface o2) {
        return o1.getAlbum().trim().compareTo(o2.getAlbum().trim());
    }
}
