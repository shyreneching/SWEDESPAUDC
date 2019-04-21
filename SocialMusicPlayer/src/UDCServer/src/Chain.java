import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface Chain {

    public void setNextChain(Chain chain);
    public void sendPacket(String sentence, DatagramSocket server, DatagramPacket receivePacket, Model model);
}
