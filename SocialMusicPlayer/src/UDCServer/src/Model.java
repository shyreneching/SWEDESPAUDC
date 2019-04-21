import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.InetAddress;

public class Model {

    private ObservableList<InetAddress> IPAddress;
    private ObservableList<Integer> port;
    private ObservableList<User> username;
    private ObservableList<String> pended;

    public Model() {
        IPAddress = FXCollections.observableArrayList();
        port = FXCollections.observableArrayList();
        username = FXCollections.observableArrayList();
        pended = FXCollections.observableArrayList();
    }

    public ObservableList<InetAddress> getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(ObservableList<InetAddress> IPAddress) {
        this.IPAddress = IPAddress;
    }

    public ObservableList<Integer> getPort() {
        return port;
    }

    public void setPort(ObservableList<Integer> port) {
        this.port = port;
    }

    public ObservableList<User> getUsername() {
        return username;
    }

    public void setUsername(ObservableList<User> username) {
        this.username = username;
    }

    public ObservableList<String> getPended() {
        return pended;
    }

    public void setPended(ObservableList<String> pended) {
        this.pended = pended;
    }
}
