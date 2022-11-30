public class Ethernet {
    private String destinationMac;
    private String sourceMac;
    private String type;

    public Ethernet(String destinationMac, String sourceMac, String type) {
        this.destinationMac = destinationMac;
        this.sourceMac = sourceMac;
        this.type = type;
    }

    @Override
    public String toString() {
        String ANSI_BLUE = "\u001B[44m";
        String ANSI_RESET = "\u001B[0m";
        return ANSI_BLUE + "Ethernet Frame" + ANSI_RESET + " " + "Destination Mac: " + this.destinationMac
                + "; Source Mac: " + this.sourceMac + "; Type: 0x" + this.type;
    }
}