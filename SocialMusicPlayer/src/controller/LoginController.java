package controller;

import model.*;

import java.sql.SQLException;
import java.util.List;

public class LoginController {

    private FacadeModel model;

    public LoginController(FacadeModel model) {
        this.model = model;
    }

    public boolean login(String name, String password) {
        try {
            if(model.login(name, password)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean signUp(String username, String password, String type) {
        if(type.equalsIgnoreCase("artist")) {
            AccountInterface acc = new Artist(username, username, password);
            try {
                if(model.createUser((Artist)acc)) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if(type.equalsIgnoreCase("listener")) {
            AccountInterface acc = new Listener(username, username, password);
            try {
                if(model.createUser((Listener)acc)) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
