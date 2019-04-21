import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendLogout implements Chain {

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
        if (separator[1].equalsIgnoreCase("logout")) {
            User x = null;
            for(User u : model.getUsername()) {
                if(u.getName().trim().equalsIgnoreCase(separator[0].trim())) {
                    x = u;
                    break;
                }
            }
            int i = getUserIndex(x.getName().trim());
            model.getIPAddress().remove(i);
            model.getPort().remove(i);
            model.getUsername().remove(x);
        } else {
            chain.sendPacket(sentence, server, receivePacket, this.model);
        }
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
