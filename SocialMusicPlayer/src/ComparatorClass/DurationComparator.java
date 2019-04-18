package ComparatorClass;

import Model.SongInterface;

import java.util.Comparator;

public class DurationComparator implements Comparator<SongInterface> {
    @Override
    public int compare(SongInterface o1, SongInterface o2) {
        return o1.getLength() - o2.getLength();
    }
}
