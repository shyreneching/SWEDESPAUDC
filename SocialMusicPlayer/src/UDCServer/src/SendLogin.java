import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendLogin implements Chain {

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
        if(separator[1].equalsIgnoreCase("login")) {
            model.getUsername().add(new User(separator[0].trim()));
            model.getIPAddress().add(receivePacket.getAddress());
            model.getPort().add(receivePacket.getPort());

            if(isValid(separator[2].trim())) {
                for(int i = 2; i < separator.length; i++) {
                    if(isValid(separator[i].trim())) {
                        System.out.println(separator[i]);
                        getUser(separator[0].trim()).getFollower().add(separator[i].trim());
                    }
                }
            }

            if(model.getPended().isEmpty()) {
                String next = "";
                byte[] sendData = next.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                try {
                    server.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for(int i = 0; i < model.getPended().size(); i++) {
                    if(model.getPended().get(i).contains(separator[0].trim())) {
                        String next = "notification" + ",";
                        String[] temp = model.getPended().get(i).split(",");
                        for(int m = 1; m < temp.length; m++) {
                            next += temp[m] + ",";
                        }
                        byte[] sendData = next.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
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
                    model.getPended().set(i, "");
                }
                model.getPended().removeIf(e->e.equalsIgnoreCase(""));
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

    public boolean isValid(String text) {
        for(int i = 0; i < text.length(); i++) {
            if(Character.isLetter(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
