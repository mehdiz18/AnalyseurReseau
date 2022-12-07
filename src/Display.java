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
            if (http != null) {
                System.out.println(ANSI_PURPLE + displayHttp(ethernet, ip, tcp, http) + ANSI_RESET);
            } else if (tcp != null) {
                System.out.println(ANSI_GREEN + displayTcp(ethernet, ip, tcp) + ANSI_RESET);
            } else if (ip != null) {
                System.out.println(displayIP(ethernet, ip));
            } else {
                System.out.println(displayEthernet(ethernet));
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
        String tcpInfos = String.format("%s:%d (%s) |" + "-".repeat(60) + ">" + "%s:%d (%s)", ip.getSourceIP(),
                tcp.getSourcePort(), ethernet.getSourceMac(), ip.getDestinationIP(), tcp.getDestinationPort(),
                ethernet.getDestinationMac());
        tcpInfos = tcpInfos.substring(0, tcpInfos.length() / 2) + flags + tcpInfos.substring(tcpInfos.length() / 2);
        return tcpInfos;
    }

    private String displayHttp(Ethernet ethernet, IPPacket ip, TCPSegment tcp, Http http) {
        String flags = "[";
        for (Map.Entry<String, Boolean> flag : tcp.getFlags().entrySet()) {
            if (flag.getValue()) {
                flags += flag.getKey() + " ";
            }
        }
        flags += "]" + " --- " + http.toString();
        String httpInfos = String.format("%s:%d (%s) |" + "-".repeat(60) + ">" + "%s:%d (%s)", ip.getSourceIP(),
                tcp.getSourcePort(), ethernet.getSourceMac(), ip.getDestinationIP(), tcp.getDestinationPort(),
                ethernet.getDestinationMac());
        httpInfos = httpInfos.substring(0, httpInfos.length() / 2) + flags
                + httpInfos.substring(httpInfos.length() / 2);
        return httpInfos;
    }

    public String displayIP(Ethernet ethernet, IPPacket ip) {
        String ANSI_YELLOW = "\u001B[43m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_YELLOW
                + String.format("%s(%s) |" + "-".repeat(60) + ">" + "%s (%s)", ip.getSourceIP(),
                        ethernet.getSourceMac(), ip.getDestinationIP(), ethernet.getDestinationMac()).toString()
                + ANSI_RESET;
    }

    public String displayEthernet(Ethernet ethernet) {
        String ANSI_BLUE = "\u001B[44m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_BLUE + String
                .format("%s |" + "-".repeat(60) + ">" + "%s", ethernet.getSourceMac(), ethernet.getDestinationMac())
                .toString() + ANSI_RESET;
    }
}
