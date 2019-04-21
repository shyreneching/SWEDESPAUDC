import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {

    private DatagramSocket server;
    private DatagramPacket receivePacket;
    private byte[] receiveData;
    private Model model;
    private Chain chain1, chain2, chain3, chain4, chain5, chain6, chain7, chain8, chain9;

    public Server() {
        model = new Model();
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

                chain1.sendPacket(sentence, server, receivePacket, this.model);
            }
        });
        t.start();
    }
}
