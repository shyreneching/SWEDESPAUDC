package Model;

import java.sql.Timestamp;

public interface RecentlyPlayedInterface {
    public int getRecentlyplayedid();

    public void setRecentlyplayedid(int recentlyplayedid);

    public String getIdsong();

    public void setIdsong(String idsong);

    public String getUsername();

    public void setUsername(String username);

    public Timestamp getTimeplayed();

    public void setTimeplayed(Timestamp timeplayed);
}
