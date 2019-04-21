import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendUnfollow implements Chain {

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
        if(separator[1].equalsIgnoreCase("unfollow")) {
            if(getUser(separator[2].trim()) != null) {
                getUser(separator[2].trim()).getFollower().remove(separator[0].trim());
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
}
