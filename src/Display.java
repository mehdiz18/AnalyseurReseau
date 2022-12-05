import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Display {
    ArrayList<HashMap<String, Packet>> parsedPackets;

    public Display(ArrayList<HashMap<String, Packet>> parsedPackets) {
        this.parsedPackets = parsedPackets;
    }

    public void display() {
        String ANSI_GREEN = "\u001B[42m";
        String ANSI_RESET = "\u001B[0m";
        String ANSI_PURPLE = "\u001B[45m";
        for (HashMap<String, Packet> packet : parsedPackets) {
            Ethernet ethernet = (Ethernet) packet.get("ethernet");
            IPPacket ip = (IPPacket) packet.get("ip");
            TCPSegment tcp = (TCPSegment) packet.get("tcp");
            Http http = (Http) packet.get("http");
            String tcpString = displayTcp(ethernet, ip, tcp);
            if (http != null) {
                System.out.println(ANSI_PURPLE + displayHttp(tcpString, http) + ANSI_RESET + "\n");
            } else {
                System.out.println(ANSI_GREEN + tcpString + ANSI_RESET + "\n");
            }
        }

    }

    private String displayTcp(Ethernet ethernet, IPPacket ip, TCPSegment tcp) {

        String flags = "[";
        for (Map.Entry<String, Boolean> flag : tcp.getFlags().entrySet()) {
            if (flag.getValue()) {
                flags += flag.getKey() + " ";
            }
        }
        flags += "]";
        String tcpInfos = String.format("%s:%d (%s) |" + "-".repeat(60) + ">" + " %s:%d (%s)", ip.getSourceIP(),
                tcp.getSourcePort(), ethernet.getSourceMac(), ip.getDestinationIP(), tcp.getDestinationPort(),
                ethernet.getDestinationMac());
        tcpInfos = tcpInfos.substring(0, tcpInfos.length() / 2) + flags + tcpInfos.substring(tcpInfos.length() / 2);
        return tcpInfos;
    }

    private String displayHttp(String tcpString, Http http) {
        String httpInfos = tcpString.substring(0, tcpString.length() / 2) + http + " "
                + tcpString.substring(tcpString.length() / 2);
        return httpInfos;
    }
}
