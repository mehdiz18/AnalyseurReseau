import java.util.HashMap;
import java.util.Map;

public class Split {
    private String packets;
    private Ethernet ethernetFrame;
    private IPPacket ipPacket;
    private TCPSegment tcpSegment;
    private Http httpHeader;
    private String tcpSegmentString;
    private String httpString;

    public Split(String packets) {
        this.packets = packets;
    }

    private void parseEthernetFrame() {
        String dMac = this.packets.substring(0, 17).replace(" ", ":");
        String sMac = this.packets.substring(18, 35).replace(" ", ":");
        String type = this.packets.substring(36, 41).replace(" ", "");

        this.ethernetFrame = new Ethernet(dMac, sMac, type);
    }

    private void parseIPPacket() {
        try {
            String ipPackeString = this.packets.substring(42);
            int version = Integer.parseInt(ipPackeString.substring(0, 1), 16);
            int headerLength = Integer.parseInt(ipPackeString.substring(1, 2), 16) * 4;
            int totalLength = Integer.parseInt(ipPackeString.substring(6, 11).replace(" ", ""), 16);
            String id = ipPackeString.substring(12, 17).replace(" ", "");
            int fragmentation = Integer.parseInt(ipPackeString.substring(18, 20), 16);
            String fragBinary = Integer.toBinaryString(fragmentation);
            if (fragBinary.length() < 8) {
                fragBinary = "0".repeat(8 - fragBinary.length()) + fragBinary;
            }
            boolean r = false;
            boolean df = fragBinary.substring(1, 2).equals("1");
            boolean mf = fragBinary.substring(2, 3).equals("1");
            String fragOffsetString = String.format("%x", Integer.parseInt(fragBinary.substring(3), 2))
                    + ipPackeString.substring(21, 23);
            int fragOffset = Integer.parseInt(fragOffsetString, 16);
            int ttl = Integer.parseInt(ipPackeString.substring(24, 26), 16);
            int protocol = Integer.parseInt(ipPackeString.substring(27, 29), 16);
            String[] sIPTab = ipPackeString.substring(36, 47).split(" ");
            String[] dIPTab = ipPackeString.substring(48, 60).split(" ");
            String sIP = "";
            String dIP = "";
            for (int i = 0; i < 4; i++) {
                sIP += Integer.parseInt(sIPTab[i], 16);
                dIP += Integer.parseInt(dIPTab[i], 16);
                if (i < 3) {
                    sIP += ".";
                    dIP += ".";
                }
            }
            this.ipPacket = new IPPacket(version, headerLength, totalLength, id, r, df, mf, fragOffset, ttl, protocol,
                    sIP, dIP);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseTCPSegment() {
        if (ipPacket != null) {
            int sPort;
            int dPort;
            int seq;
            int ack;
            int thl;
            int window;
            HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
            flags.put("URG", null);
            flags.put("ACK", null);
            flags.put("PSH", null);
            flags.put("RST", null);
            flags.put("SYN", null);
            flags.put("FIN", null);
            tcpSegmentString = this.packets.substring(42 + ipPacket.getHeaderLength() * 2 + ipPacket.getHeaderLength());
            sPort = Integer.parseInt(tcpSegmentString.substring(0, 5).replace(" ", ""), 16);
            dPort = Integer.parseInt(tcpSegmentString.substring(6, 11).replace(" ", ""), 16);
            seq = Integer.parseInt(tcpSegmentString.substring(12, 23).replace(" ", ""), 16);
            ack = Integer.parseInt(tcpSegmentString.substring(24, 35).replace(" ", ""), 16);
            thl = Integer.parseInt(tcpSegmentString.substring(36, 37), 16) * 4;
            String flagsString = tcpSegmentString.substring(39, 41);
            flagsString = Integer.toBinaryString(Integer.parseInt(flagsString, 16));
            flagsString = "0".repeat(8 - flagsString.length()) + flagsString;
            int i = 2;
            for (String flagName : flags.keySet()) {
                flags.put(flagName, flagsString.substring(i, i + 1).equals("1"));
                i += 1;
            }
            window = Integer.parseInt(tcpSegmentString.substring(42, 47).replace(" ", ""), 16);
            this.tcpSegment = new TCPSegment(sPort, dPort, seq, ack, thl,
                    ipPacket.getTotalLength() - ipPacket.getHeaderLength() - thl, flags, window);
        } else {
            this.tcpSegment = null;
        }
    }

    // private void parseHttp() {
    // httpString = tcpSegmentString.substring((tcpSegment.getTcpSegmentLength() +
    // tcpSegment.getThl()) * 2);
    // String[] httpTab = httpString.replace(" ", "").split("20|0d0a");
    // System.out.println(httpString);
    // // if (tcpSegment.getSourcePort() == 80) {
    // // String version = toAscii(httpTab[0]);
    // // String codeReponse = toAscii(httpTab[1]);
    // // httpHeader = new HttpResponse(version, codeReponse);
    // // } else if (tcpSegment.getDestinationPort() == 80) {
    // // String methode = toAscii(httpTab[0]);
    // // String url = toAscii(httpTab[1]);
    // // String version = toAscii(httpTab[2]);
    // // httpHeader = new HttpRequest(methode, url, version);
    // // }
    // }

    public Ethernet getEthernetFrame() {
        this.parseEthernetFrame();
        return this.ethernetFrame;
    }

    public IPPacket getIpPacket() {
        this.parseIPPacket();
        return this.ipPacket;
    }

    public TCPSegment getTcpSegment() {
        if (this.ipPacket == null) {
            this.parseIPPacket();
        }
        this.parseTCPSegment();
        this.parseIPPacket();
        return this.tcpSegment;
    }

    // public Http getHttp() {
    // this.parseHttp();
    // return httpHeader;
    // }

    private String toAscii(String hexString) {
        StringBuilder asciiString = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            asciiString.append((char) Integer.parseInt(hexString.substring(i, i + 2), 16));
        }
        return asciiString.toString();
    }
}
