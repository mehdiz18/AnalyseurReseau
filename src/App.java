import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class App {

    public static void main(String[] args) throws Exception {
        CleanFile cleanFile = new CleanFile(args[0]);
        File file = cleanFile.cleanFile("./out/cleanFile");
        Parser parser = new Parser(file);
        ArrayList<HashMap<String, Packet>> parsedPackets = parser.parseFile();
        Display display = new Display(parsedPackets);
        CreatePdf createPdf = new CreatePdf(parsedPackets);
        display.display();
        createPdf.generatePdf();
    }
}
