package Model;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface Service {
    public ObservableList<Object> getAll()  throws SQLException;
    public boolean add(Object o) throws SQLException;
    public boolean delete(String s) throws SQLException;
    public boolean update(String s, Object o) throws SQLException;


}
