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
                    System.out.println("From Server: " + sentence);
                    view.showNotification(sentence);
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
