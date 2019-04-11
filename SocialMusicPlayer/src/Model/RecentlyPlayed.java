package Model;

import java.sql.Timestamp;

public class RecentlyPlayed implements RecentlyPlayedInterface{
    private int recentlyplayedid;
    private String idsong, username;
    private Timestamp timeplayed;

    public RecentlyPlayed() {
    }

    public int getRecentlyplayedid() {
        return recentlyplayedid;
    }

    public void setRecentlyplayedid(int recentlyplayedid) {
        this.recentlyplayedid = recentlyplayedid;
    }

    public String getIdsong() {
        return idsong;
    }

    public void setIdsong(String idsong) {
        this.idsong = idsong;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimeplayed() {
        return timeplayed;
    }

    public void setTimeplayed(Timestamp timeplayed) {
        this.timeplayed = timeplayed;
    }
}
