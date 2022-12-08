import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

public class CreatePdf {
    ArrayList<HashMap<String, Packet>> parsedPackets;

    public CreatePdf(ArrayList<HashMap<String, Packet>> parsedPackets) {
        this.parsedPackets = parsedPackets;
    }

    public void generatePdf() throws Exception {
        PdfWriter pdfWrite = new PdfWriter("./out/output" + LocalDateTime.now() + ".pdf");
        PdfDocument pdf = new PdfDocument(new PdfWriter(pdfWrite));
        Document document = new Document(pdf, PageSize.A4.rotate());
        // Create the header of the file
        Paragraph p = new Paragraph("Résultat du Visualisateur");
        p.setTextAlignment(TextAlignment.CENTER);
        p.setFontSize(17);
        document.add(p);
        for (HashMap<String, Packet> packet : parsedPackets) {
            Ethernet ethernet = (Ethernet) packet.get("ethernet");
            IPPacket ip = (IPPacket) packet.get("ip");
            TCPSegment tcp = (TCPSegment) packet.get("tcp");
            Http http = (Http) packet.get("http");
            if (http != null) {
                p = new Paragraph(generateHTTP(ethernet, ip, tcp, http));
                p.setBackgroundColor(new DeviceRgb(114, 204, 132));
            } else if (tcp != null) {
                p = new Paragraph(generateTCP(ethernet, ip, tcp));
                p.setBackgroundColor(new DeviceRgb(133, 197, 222));
            } else if (ip != null) {
                p = new Paragraph(generateIP(ethernet, ip));
                p.setBackgroundColor(new DeviceRgb(232, 125, 125));
            } else {
                p = new Paragraph(generateEthernet(ethernet));
                p.setBackgroundColor(new DeviceRgb(232, 220, 125));
            }
            p.setTextAlignment(TextAlignment.CENTER);
            p.setFontSize(9);
            document.add(p);
        }

        document.close();
    }

    private String generateHTTP(Ethernet ethernet, IPPacket ip, TCPSegment tcp, Http http) {
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

    private String generateIP(Ethernet ethernet, IPPacket ip) {
        return String.format("%s(%s) |" + "-".repeat(60) + ">" + "%s (%s)", ip.getSourceIP(), ethernet.getSourceMac(),
                ip.getDestinationIP(), ethernet.getDestinationMac()).toString();
    }

    private String generateEthernet(Ethernet ethernet) {
        return String
                .format("%s |" + "-".repeat(60) + ">" + "%s", ethernet.getSourceMac(), ethernet.getDestinationMac())
                .toString();
    }

    private String generateTCP(Ethernet ethernet, IPPacket ip, TCPSegment tcp) {
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
}
