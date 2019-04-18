package ComparatorClass;

import Model.SongInterface;

import java.util.Comparator;

public class GenreComparator implements Comparator<SongInterface> {
    @Override
    public int compare(SongInterface o1, SongInterface o2) {
        return o1.getGenre().trim().compareTo(o2.getGenre().trim());
    }
}
