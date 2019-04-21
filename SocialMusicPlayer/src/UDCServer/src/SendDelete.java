import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendDelete implements Chain {

    private Chain chain;
    private Model model;

    @Override
    public void setNextChain(Chain chain) {
        this.chain = chain;
    }

    @Override
    public void sendPacket(String sentence, DatagramSocket server, DatagramPacket receivePacket, Model model) {
        this.model = model;
        String[] separator = sentence.split(",");
        if(separator[1].equalsIgnoreCase("delete")) {
            String next = "delete" + "," + separator[2] + ",";
            byte[] sendData = next.getBytes();
            for(int i = 0; i < model.getUsername().size(); i++) {
                if(!model.getUsername().get(i).getName().equalsIgnoreCase(separator[0])) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, model.getIPAddress().get(i), model.getPort().get(i));
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
        } else {
            chain.sendPacket(sentence, server, receivePacket, this.model);
        }
    }
}
