import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Pattern;

public class Server {

    private ObservableList<InetAddress> IPAddress;
    private ObservableList<Integer> port;
    private ObservableList<User> username;

    private DatagramSocket server;
    private DatagramPacket receivePacket, sendPacket;
    private byte[] receiveData, sendData;

    public Server() {
        IPAddress = FXCollections.observableArrayList();
        port = FXCollections.observableArrayList();
        username = FXCollections.observableArrayList();
        try {
            server = new DatagramSocket(4000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        Thread t = new Thread(() -> {
            while(true) {
                receiveData = new byte[1024];
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Waiting for user..");
                try {
                    server.receive(receivePacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String sentence = new String(receivePacket.getData());
                System.out.println(sentence);
                String next = "";
                String[] separator = sentence.split(",");
                if(separator[1].equalsIgnoreCase("login")) {
                    if(inUserList(separator[0].trim())) {
                        IPAddress.set(getUserIndex(separator[0].trim()),receivePacket.getAddress());
                        port.set(getUserIndex(separator[0].trim()), receivePacket.getPort());
                    } else {
                        username.add(new User(separator[0].trim()));
                        IPAddress.add(receivePacket.getAddress());
                        port.add(receivePacket.getPort());

                        if(isValid(separator[2].trim())) {
                            for(int i = 2; i < separator.length; i++) {
                                if(isValid(separator[i].trim())) {
                                    System.out.println(separator[i]);
                                    getUser(separator[0].trim()).getFollower().add(separator[i].trim());
                                }
                            }
                        }
                    }
                    next = "";
                    sendData = next.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    try {
                        server.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(separator[1].equalsIgnoreCase("follow")) {
                    getUser(separator[2].trim()).getFollower().add(separator[0].trim());
                    next = separator[0].trim() + " has followed you";
                    sendData = next.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    try {
                        server.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(separator[1].equalsIgnoreCase("unfollow")) {
                    getUser(separator[2].trim()).getFollower().remove(separator[0].trim());
                    next = "";
                    sendData = next.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    try {
                        server.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(separator[1].equalsIgnoreCase("uploadsong") || separator[1].equalsIgnoreCase("uploadplaylist")) {
                    if(separator[1].equalsIgnoreCase("uploadsong")) {
                        next = separator[0].trim() + " uploaded a new song";
                    } else if(separator[1].equalsIgnoreCase("uploadplaylist")) {
                        next = separator[0].trim() + " uploaded a new playlist";
                    }
                    System.out.println(next);
                    sendData = next.getBytes();
                    for(int i = 0; i < getUser(separator[0].trim()).getFollower().size(); i++) {
                        String temp = getUser(separator[0].trim()).getFollower().get(i).trim();
                        System.out.println(temp);
                        if(getUser(temp) != null) {
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress.get(getUserIndex(temp)), port.get(getUserIndex(temp)));
                            try {
                                server.send(sendPacket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        t.start();
    }

    public boolean inUserList(String name) {
        for(User u : username) {
            if(u.getName().trim().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public int getUserIndex(String name) {
        for(int i = 0; i < username.size(); i++) {
            if(username.get(i).getName().trim().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public User getUser(String name) {
        for(User u : username) {
            if(u.getName().trim().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    public boolean isValid(String text) {
        for(int i = 0; i < text.length(); i++) {
            if(Character.isLetter(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

}
