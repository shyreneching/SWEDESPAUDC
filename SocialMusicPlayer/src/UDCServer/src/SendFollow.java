import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendFollow implements Chain {

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
        if(separator[1].equalsIgnoreCase("follow")) {
            String next = "";
            if(getUser(separator[2].trim()) != null) {
                getUser(separator[2].trim()).getFollower().add(separator[0].trim());
                next += separator[0].trim() + " has followed you" + "," + separator[3] + "," + separator[4] + "," + separator[5] + ",";
                byte[] sendData = next.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, model.getIPAddress().get(getUserIndex(separator[2].trim())), model.getPort().get(getUserIndex(separator[2].trim())));
                try {
                    server.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                model.getPended().add(separator[2].trim() + "," + next);
            }
        } else {
            chain.sendPacket(sentence, server, receivePacket, this.model);
        }
    }

    public User getUser(String name) {
        for(User u : model.getUsername()) {
            if(u.getName().trim().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    public int getUserIndex(String name) {
        for(int i = 0; i < model.getUsername().size(); i++) {
            if(model.getUsername().get(i).getName().trim().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
}
