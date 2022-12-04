public class IPPacket implements Packet {
    private int version;
    private int headerLength;
    private int totalLength;
    private String id;
    private boolean r;
    private boolean df;
    private boolean mf;
    private int fragOffset;
    private int ttl;
    private int protocol;
    private String sIP;
    private String dIP;

    public IPPacket(int version, int headerLength, int totalLength, String id, boolean r, boolean df, boolean mf,
            int fragOffset, int ttl, int protocol, String sIP, String dIP) {
        this.version = version;
        this.headerLength = headerLength;
        this.totalLength = totalLength;
        this.id = id;
        this.r = r;
        this.df = df;
        this.mf = mf;
        this.fragOffset = fragOffset;
        this.ttl = ttl;
        this.protocol = protocol;
        this.sIP = sIP;
        this.dIP = dIP;
    }

    @Override
    public String toString() {
        String ANSI_YELLOW = "\u001B[43m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_YELLOW + "IP Packet" + ANSI_RESET + " Version:" + " " + this.version + " ; IHL: "
                + this.headerLength + " ; Total Length: " + this.totalLength + " ; ID: 0x" + this.id + " ; r: " + this.r
                + " ; DF: " + this.df + "; MF: " + this.mf + " ; Frag Offset: " + this.fragOffset + " ; TTL: "
                + this.ttl + " ; Protocol: 0x" + this.protocol + " ; IP Source: " + this.sIP + " ; IP Destination: "
                + this.dIP;
    }

    public int getVersion() {
        return this.version;
    }

    public int getHeaderLength() {
        return this.headerLength;
    }

    public int getTotalLength() {
        return this.totalLength;
    }

    public String getId() {
        return this.id;
    }

    public boolean getR() {
        return this.r;
    }

    public boolean getDf() {
        return this.df;
    }

    public boolean getMf() {
        return this.mf;
    }

    public int getFragOffset() {
        return this.fragOffset;
    }

    public int getTtl() {
        return this.ttl;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public String getSourceIP() {
        return this.sIP;
    }

    public String getDestinationIP() {
        return this.dIP;
    }

}
