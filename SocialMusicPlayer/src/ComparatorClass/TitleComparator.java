package ComparatorClass;

import Model.SongInterface;

import java.util.Comparator;

public class TitleComparator implements Comparator<SongInterface> {

    @Override
    public int compare(SongInterface s1, SongInterface s2) {
        return s1.getName().trim().compareTo(s2.getName().trim());
    }
}
