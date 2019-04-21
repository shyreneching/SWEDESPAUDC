package Controller;

import View.MusicPlayerView;

import java.io.IOException;
import java.net.*;

public class UDCClient {

    private DatagramSocket client;
    private DatagramPacket receivePacket, sendPacket;
    private byte[] receiveData, sendData;
    private InetAddress serverIP;
    private int serverPort;
    private String message;

    private MusicPlayerView view;

    public UDCClient() {
        try {
            client = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            serverIP = InetAddress.getByName("ASUS-SLS");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverPort = 4000;
        init();
    }

    private void init() {
        Thread t = new Thread(() -> {
            while(true) {
                receiveData = new byte[1024];
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    client.receive(receivePacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String sentence = new String(receivePacket.getData());
                if(!sentence.trim().equalsIgnoreCase("")) {
                    if(sentence.trim().contains("notification")) {
                        String[] temp = sentence.split(",");
                        System.out.println("From Server: " + sentence);
                        String time = "";
                        for(int i = 2; i < temp.length; i++) {
                            time += temp[i] + ",";
                        }
                        view.addNotification(temp[1], time);
                    } else {
                        if(sentence.trim().contains("delete")) {
                            String[] temp = sentence.split(",");
                            view.resetSong(temp[1]);
                            view.showNotification("This song has been deleted by the owner");
                        } else if(sentence.trim().contains("edit")) {
                            String[] temp = sentence.split(",");
                            view.updateSong(temp[1]);
                        } else {
                            String[] temp = sentence.split(",");
                            System.out.println("From Server: " + sentence);
                            String time = "";
                            for(int i = 1; i < temp.length; i++) {
                                time += temp[i] + ",";
                            }
                            view.addNotification(temp[0], time);
                            view.showNotification(sentence);
                        }
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void sendPacket() {
        if(message != null) {
            System.out.println("sending packet");
            sendData = message.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
            try {
                client.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message = null;
        }
    }

    public void attach(MusicPlayerView view) {
        this.view = view;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
