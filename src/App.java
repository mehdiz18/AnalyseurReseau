import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class App {
    public static void main(String[] args) throws Exception {
        CleanFile cleanFile = new CleanFile(args[0]);
        File file = cleanFile.cleanFile(args[1]);
        Parser parser = new Parser(file);
        ArrayList<HashMap<String, Packet>> parsedPackets = parser.parseFile();
        for (HashMap<String, Packet> map : parsedPackets) {
            System.out.println(map.get("ethernet"));
            System.out.println(map.get("ip"));
            System.out.println(map.get("tcp"));
            System.out.println();
        }

    }
}
