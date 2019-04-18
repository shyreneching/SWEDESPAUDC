package ComparatorClass;

import Model.SongInterface;

import java.util.Comparator;

public class DateUploadedComparator implements Comparator<SongInterface> {
    @Override
    public int compare(SongInterface o1, SongInterface o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
