public class TCPSegment {
    private int sPort;
    private int dPort;
    private int seq;
    private int ack;
    private int thl;
    private int window;
    private int tcpSegmentLength;

    public TCPSegment(int sPort, int dPort, int seq, int ack, int thl, int tcpSegmentLength, int window) {
        this.sPort = sPort;
        this.dPort = dPort;
        this.seq = seq;
        this.ack = ack;
        this.thl = thl;
        this.window = window;
        this.tcpSegmentLength = tcpSegmentLength;
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

    @Override
    public String toString() {
        String ANSI_GREEN = "\u001B[42m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_GREEN + "TCP Segment" + ANSI_RESET + " Source Port: " + sPort + " ; Destination Port: " + dPort
                + " ; Sequence Number: " + seq + " ; Acknowledgment Number: " + ack + " ; Header Length: " + thl
                + " ; Window: " + window;
    }
}
