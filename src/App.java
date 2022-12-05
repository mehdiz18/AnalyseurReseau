import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {
    static String ANSI_GREEN = "\u001B[42m";
    static String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws Exception {
        CleanFile cleanFile = new CleanFile(args[0]);
        File file = cleanFile.cleanFile(args[1]);
        Parser parser = new Parser(file);
        ArrayList<HashMap<String, Packet>> parsedPackets = parser.parseFile();
        for (HashMap<String, Packet> packet : parsedPackets) {
            Ethernet ethernet = (Ethernet) packet.get("ethernet");
            IPPacket ip = (IPPacket) packet.get("ip");
            TCPSegment tcp = (TCPSegment) packet.get("tcp");
            Http http = (Http) packet.get("http");
            if (http != null) {
                System.out.println(http);
            }
        }
        // String flags = "[";
        // for (Map.Entry<String, Boolean> flag : tcp.getFlags().entrySet()) {
        // if (flag.getValue()) {
        // flags += flag.getKey() + " ";
        // }
        // }
        // flags += "]";
        // String infos = String.format(ANSI_GREEN + "%s:%d (%s) |" + "-".repeat(100) +
        // ">" + " %s:%d (%s)" + ANSI_RESET,
        // ip.getSourceIP(), tcp.getSourcePort(), ethernet.getSourceMac(),
        // ip.getDestinationIP(),
        // tcp.getDestinationPort(), ethernet.getDestinationMac());
        // System.out.println(infos.substring(0, infos.length() / 2) + flags +
        // infos.substring(infos.length() / 2));

    }
}
