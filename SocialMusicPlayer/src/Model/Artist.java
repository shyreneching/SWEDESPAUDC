package Model;

import javafx.collections.FXCollections;

public class Artist extends Account {

    public Artist(){}

    public Artist(String name, String username, String password) {
        super.name = name;
        super.username = username;
        super.password = password;
    }
}
