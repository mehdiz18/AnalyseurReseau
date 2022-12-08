import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CleanFile {
    private File file;

    public CleanFile(String filePath) {
        this.file = new File(filePath);
    }

    File cleanFile(String cleanFilePath) {
        File cleanFile = new File(cleanFilePath);
        try {
            BufferedReader bf = new BufferedReader(new FileReader(this.file));
            PrintWriter bWriter = new PrintWriter(new FileWriter(cleanFile));
            String st;
            String[] stTab;
            int line = 1;
            while ((st = bf.readLine()) != null) {
                // Sauter les lignes vides
                if (st.isBlank() || st.split("  +").length < 2) {
                    bWriter.print("\n");
                } else {
                    // Je split sur 2 espaces ou plus
                    stTab = st.split("  +");
                    if (stTab[0].equals("0000") && line != 1) {
                        bWriter.println();
                    }
                    // Si je rencontre du texte
                    if (stTab.length >= 2) {
                        bWriter.print(stTab[1] + " ");
                    }
                    line++;
                }
            }
            bf.close();
            bWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cleanFile;
    }
}
