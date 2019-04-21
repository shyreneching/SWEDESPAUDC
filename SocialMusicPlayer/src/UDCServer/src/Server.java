import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {

    /*private ObservableList<InetAddress> IPAddress;
    private ObservableList<Integer> port;
    private ObservableList<User> username;
    private ObservableList<String> pended;*/

    private DatagramSocket server;
    private DatagramPacket receivePacket, sendPacket;
    private byte[] receiveData, sendData;
    private Model model;
    private Chain chain1, chain2, chain3, chain4, chain5, chain6, chain7, chain8, chain9;

    public Server() {
        model = new Model();
        /*IPAddress = FXCollections.observableArrayList();
        port = FXCollections.observableArrayList();
        username = FXCollections.observableArrayList();
        pended = FXCollections.observableArrayList();*/
        try {
            server = new DatagramSocket(4000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        chain1 = new SendLogin();
        chain2 = new SendLogout();
        chain3 = new SendFollow();
        chain4 = new SendUnfollow();
        chain5 = new SendLike();
        chain6 = new SendDelete();
        chain7 = new SendEdit();
        chain8 = new SendUploadSong();
        chain9 = new SendUploadPlaylist();

        chain1.setNextChain(chain2);
        chain2.setNextChain(chain3);
        chain3.setNextChain(chain4);
        chain4.setNextChain(chain5);
        chain5.setNextChain(chain6);
        chain6.setNextChain(chain7);
        chain7.setNextChain(chain8);
        chain8.setNextChain(chain9);

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
                /*String[] separator = sentence.split(",");*/

                chain1.sendPacket(sentence, server, receivePacket, this.model);

                /*if(separator[1].equalsIgnoreCase("login")) {
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

                    if(pended.isEmpty()) {
                        next = "";
                        sendData = next.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                        try {
                            server.send(sendPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        for(int i = 0; i < pended.size(); i++) {
                            if(pended.get(i).contains(separator[0].trim())) {
                                next = "notification" + ",";
                                String[] temp = pended.get(i).split(",");
                                for(int m = 1; m < temp.length; m++) {
                                    next += temp[m] + ",";
                                }
                                sendData = next.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                                try {
                                    server.send(sendPacket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            pended.set(i, "");
                        }
                        pended.removeIf(e->e.equalsIgnoreCase(""));
                    }
                } else if (separator[1].equalsIgnoreCase("logout")) {
                    User x = null;
                    for(User u : username) {
                        if(u.getName().trim().equalsIgnoreCase(separator[0].trim())) {
                            x = u;
                            break;
                        }
                    }
                    int i = getUserIndex(x.getName().trim());
                    IPAddress.remove(i);
                    port.remove(i);
                    username.remove(x);
                } else if(separator[1].equalsIgnoreCase("follow")) {
                    if(getUser(separator[2].trim()) != null) {
                        getUser(separator[2].trim()).getFollower().add(separator[0].trim());
                        next = separator[0].trim() + " has followed you" + "," + separator[3] + "," + separator[4] + "," + separator[5] + ",";
                        sendData = next.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress.get(getUserIndex(separator[2].trim())), port.get(getUserIndex(separator[2].trim())));
                        try {
                            server.send(sendPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        pended.add(separator[2].trim() + "," + next);
                    }
                } else if(separator[1].equalsIgnoreCase("unfollow")) {
                    if(getUser(separator[2].trim()) != null) {
                        getUser(separator[2].trim()).getFollower().remove(separator[0].trim());
                    }
                } else if (separator[1].equalsIgnoreCase("like")) {
                    if(getUser(separator[2].trim()) != null) {
                        next = separator[0].trim() + " liked your song" + "," + separator[3] + "," + separator[4] + "," + separator[5] + ",";
                        sendData = next.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress.get(getUserIndex(separator[2].trim())), port.get(getUserIndex(separator[2].trim())));
                        try {
                            server.send(sendPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        pended.add(separator[2].trim() + "," + next);
                    }
                } else if(separator[1].equalsIgnoreCase("delete")) {
                    next = "delete" + "," + separator[2] + ",";
                    sendData = next.getBytes();
                    for(int i = 0; i < username.size(); i++) {
                        if(!username.get(i).getName().equalsIgnoreCase(separator[0])) {
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress.get(i), port.get(i));
                            try {
                                server.send(sendPacket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if(separator[1].equalsIgnoreCase("edit")) {
                    next = "edit" + "," + separator[2] + ",";
                    sendData = next.getBytes();
                    for(int i = 0; i < username.size(); i++) {
                        if(!username.get(i).getName().equalsIgnoreCase(separator[0])) {
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress.get(i), port.get(i));
                            try {
                                server.send(sendPacket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if(separator[1].equalsIgnoreCase("uploadsong") || separator[1].equalsIgnoreCase("uploadplaylist")) {
                    if(separator[1].equalsIgnoreCase("uploadsong")) {
                        next = separator[0].trim() + " uploaded a new song" + "," + separator[2] + "," + separator[3] + "," + separator[4] + ",";
                    } else if(separator[1].equalsIgnoreCase("uploadplaylist")) {
                        next = separator[0].trim() + " uploaded a new playlist" + "," + separator[2] + "," + separator[3] + "," + separator[4] + ",";
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
                        } else {
                            pended.add(temp + "," + next);
                        }
                    }
                }*/
            }
        });
        t.start();
    }

    /*public int getUserIndex(String name) {
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
    }*/

}
