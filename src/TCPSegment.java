import java.util.HashMap;

public class TCPSegment implements Packet {
    private int sPort;
    private int dPort;
    private long seq;
    private long ack;
    private int thl;
    private int window;
    private int tcpSegmentLength;
    private HashMap<String, Boolean> flags;

    public TCPSegment(int sPort, int dPort, long seq, long ack, int thl, int tcpSegmentLength,
            HashMap<String, Boolean> flags, int window) {
        this.sPort = sPort;
        this.dPort = dPort;
        this.seq = seq;
        this.ack = ack;
        this.thl = thl;
        this.window = window;
        this.tcpSegmentLength = tcpSegmentLength;
        this.flags = flags;
    }

    public int getThl() {
        return this.thl;
    }

    public int getSourcePort() {
        return sPort;
    }

    public int getDestinationPort() {
        return dPort;
    }

    public int getTcpSegmentLength() {
        return this.tcpSegmentLength;
    }

    public HashMap<String, Boolean> getFlags() {
        return flags;
    }

    public boolean isHTTP() {
        if ((this.sPort == 80 || this.dPort == 80) && this.getTcpSegmentLength() - this.getThl() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String ANSI_GREEN = "\u001B[42m";
        String ANSI_RESET = "\u001B[0m";
        String ret = ANSI_GREEN + "TCP Segment" + ANSI_RESET + " Source Port: " + sPort + " ; Destination Port: "
                + dPort + " ; Sequence Number: " + seq + " ; Acknowledgment Number: " + ack + " ; Header Length: " + thl
                + " ; Flags: ";
        for (String flagName : flags.keySet()) {
            if (flags.get(flagName)) {
                ret += flagName + " : true ; ";
            }
        }
        ret += " Window: " + window;
        return ret;
    }
}
