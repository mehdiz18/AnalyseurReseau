import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        CleanFile cleanFile = new CleanFile("./samples/capture.txt");
        File file = cleanFile.cleanFile("./samples/cleanFile");
        BufferedReader bf = new BufferedReader(new FileReader(file));
        bf.readLine();
        bf.readLine();
        String st = bf.readLine();
        Split sp = new Split(st);
        System.out.println(sp.getEthernetFrame());
        System.out.println(sp.getIpPacket());
        System.out.println(sp.getTcpSegment());
        System.out.println(sp.getHttp());
        bf.close();
    }
}
