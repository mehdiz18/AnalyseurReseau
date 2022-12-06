import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private String packets;
    private Ethernet ethernetFrame;
    private IPPacket ipPacket;
    private TCPSegment tcpSegment;
    private Http httpHeader;
    private File file;

    public Parser(File file) {
        this.file = file;
    }

    private void parseEthernetFrame() {
        String dMac = this.packets.substring(0, 17).replace(" ", ":");
        String sMac = this.packets.substring(18, 35).replace(" ", ":");
        String type = this.packets.substring(36, 41).replace(" ", "");

        this.ethernetFrame = new Ethernet(dMac, sMac, type);
    }

    private void parseIPPacket() {
        try {
            if (ethernetFrame.getType().equals("0800")) {
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
                this.ipPacket = new IPPacket(version, headerLength, totalLength, id, r, df, mf, fragOffset, ttl,
                        protocol, sIP, dIP);
            } else {
                ipPacket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseTCPSegment() {
        String tcpSegmentString;
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
            flags.put("URG", flagsString.substring(2, 3).equals("1"));
            flags.put("ACK", flagsString.substring(3, 4).equals("1"));
            flags.put("PSH", flagsString.substring(4, 5).equals("1"));
            flags.put("RST", flagsString.substring(5, 6).equals("1"));
            flags.put("SYN", flagsString.substring(6, 7).equals("1"));
            flags.put("FIN", flagsString.substring(7, 8).equals("1"));
            window = Integer.parseInt(tcpSegmentString.substring(42, 47).replace(" ", ""), 16);
            this.tcpSegment = new TCPSegment(sPort, dPort, seq, ack, thl,
                    ipPacket.getTotalLength() - ipPacket.getHeaderLength() - thl, flags, window);
        } else {
            this.tcpSegment = null;
        }
    }

    private void parseHttp() {
        if (tcpSegment.getTcpSegmentLength() - tcpSegment.getThl() > 0) {
            String httpString = this.packets.substring((42 + ipPacket.getHeaderLength() * 3 + tcpSegment.getThl() * 3));

            String[] httpTab = httpString.replace(" ", "").split("20|0d0a");
            if (tcpSegment.getSourcePort() == 80) {
                String version = toAscii(httpTab[0]);
                // Matcher si la version correspond bien a un html
                if (version.matches("HTTP.*")) {
                    int codeReponse = Integer.parseInt(toAscii(httpTab[1]));
                    String message = toAscii(httpTab[2]);
                    httpHeader = new HttpResponse(version, codeReponse, message);
                } else {
                    httpHeader = null;
                }
            } else if (tcpSegment.getDestinationPort() == 80) {
                String methode = toAscii(httpTab[0]);
                String url = toAscii(httpTab[1]);
                String version = toAscii(httpTab[2]);
                String host = toAscii(httpTab[4]);
                httpHeader = new HttpRequest(methode, url, version, host);
            }
        } else {
            httpHeader = null;
        }
    }

    public Ethernet getEthernetFrame() {
        if (this.ethernetFrame == null) {
            this.parseEthernetFrame();
        }
        return this.ethernetFrame;
    }

    public IPPacket getIpPacket() {
        if (this.ipPacket == null) {
            this.parseIPPacket();
        }
        return this.ipPacket;
    }

    public TCPSegment getTcpSegment() {
        this.parseIPPacket();
        if (this.ipPacket == null) {
            return null;
        }
        this.parseTCPSegment();
        return this.tcpSegment;
    }

    public Http getHttp() {
        this.parseHttp();
        return httpHeader;
    }

    private String toAscii(String hexString) {
        StringBuilder asciiString = new StringBuilder();
        for (int i = 0; i < hexString.length() - 1; i += 2) {
            asciiString.append((char) Integer.parseInt(hexString.substring(i, i + 2), 16));
        }
        return asciiString.toString();
    }

    private HashMap<String, Packet> toMap() {
        HashMap<String, Packet> map = new HashMap<String, Packet>();
        map.put("ethernet", this.getEthernetFrame());
        map.put("ip", this.getIpPacket());
        map.put("tcp", this.getTcpSegment());
        map.put("http", this.getHttp());
        this.ethernetFrame = null;
        this.ipPacket = null;
        this.tcpSegment = null;
        this.httpHeader = null;
        return map;
    }

    public ArrayList<HashMap<String, Packet>> parseFile() throws Exception {
        ArrayList<HashMap<String, Packet>> parsedPackets = new ArrayList<HashMap<String, Packet>>();
        BufferedReader bf = new BufferedReader(new FileReader(file));
        HashMap<String, Packet> map = new HashMap<String, Packet>();
        while ((this.packets = bf.readLine()) != null) {
            map = this.toMap();
            parsedPackets.add(map);
        }
        bf.close();
        return parsedPackets;
    }
}
