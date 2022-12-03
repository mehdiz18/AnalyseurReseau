import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CleanFile {
    private File file;

    public CleanFile(String filePath) {
        this.file = new File(filePath);
    }

    File cleanFile(String cleanFilePath) {
        File cleanFile = new File(cleanFilePath);
        try {
            BufferedReader bf = new BufferedReader(new FileReader(this.file));
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(cleanFile));
            String st;
            String[] stTab;
            while ((st = bf.readLine()) != null) {
                // Sauter les lignes vides
                if (st.isBlank()) {
                    bWriter.newLine();
                } else {
                    String cleanString = "";
                    // Je split sur 2 espaces ou plus
                    stTab = st.split("  +");
                    // Si je rencontre du texte
                    if (stTab.length >= 2) {
                        for (int j = 1; j < stTab.length; j++) {
                            if (stTab[j].matches("^[0-9a-fA-F ]+$")) {
                                cleanString += stTab[j];
                            }
                        }
                        bWriter.write(cleanString + " ", 0, cleanString.length() + 1);
                    }
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
