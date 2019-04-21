import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendUploadPlaylist implements Chain {

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
        if(separator[1].equalsIgnoreCase("uploadplaylist")) {
            String next = separator[0].trim() + " uploaded a new playlist" + "," + separator[2] + "," + separator[3] + "," + separator[4] + ",";
            System.out.println(next);
            byte[] sendData = next.getBytes();
            for(int i = 0; i < getUser(separator[0].trim()).getFollower().size(); i++) {
                String temp = getUser(separator[0].trim()).getFollower().get(i).trim();
                System.out.println(temp);
                if(getUser(temp) != null) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, model.getIPAddress().get(getUserIndex(temp)), model.getPort().get(getUserIndex(temp)));
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
                    model.getPended().add(temp + "," + next);
                }
            }
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

    public User getUser(String name) {
        for(User u : model.getUsername()) {
            if(u.getName().trim().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }
}
