import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {

    private ObservableList<String> follower;
    private String name;

    public User(String name) {
        this.name = name;
        follower = FXCollections.observableArrayList();
    }

    public ObservableList<String> getFollower() {
        return follower;
    }

    public void setFollower(ObservableList<String> follower) {
        this.follower = follower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
