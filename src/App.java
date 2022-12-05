import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class App {

    public static void main(String[] args) throws Exception {
        CleanFile cleanFile = new CleanFile(args[0]);
        File file = cleanFile.cleanFile(args[1]);
        Parser parser = new Parser(file);
        ArrayList<HashMap<String, Packet>> parsedPackets = parser.parseFile();
        Display display = new Display(parsedPackets);
        display.display();

    }
}
