import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        CleanFile cleanFile = new CleanFile("./samples/http.txt");
        File file = cleanFile.cleanFile("./samples/cleanHttp");
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String st = "";
        int i = 1;
        while ((st = bf.readLine()) != null) {
            System.out.println("Trame: " + i);
            Split sp = new Split(st);
            System.out.println(sp.getEthernetFrame());
            System.out.println(sp.getIpPacket());
            System.out.println(sp.getTcpSegment());
            i++;
            System.out.println("");
        }

        bf.close();
    }
}
