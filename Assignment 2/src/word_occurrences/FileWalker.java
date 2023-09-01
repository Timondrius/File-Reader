
package word_occurrences;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;


final class FileWalker {

    private final Occurrences occ;
    private final File rootDir;

    FileWalker(String rootDirPath, Occurrences occ) throws FileNotFoundException {
        this.occ = occ;
        this.rootDir = new File(rootDirPath);

        if (!this.rootDir.isDirectory()) {
            throw new FileNotFoundException(rootDirPath + " does not exist, " +
                    "or is not a directory.");
        }
    }

    void populateOccurrenceMap() {
        try {
            populateOccurrenceMap(rootDir);
        } catch (IOException ex) {
            // We should never really get to this point, but just in case...
            System.out.println(ex);
        }
    }

    private void populateOccurrenceMap(File fileOrDir) throws IOException {
        if (fileOrDir.isFile()) {
            BufferedReader reader = new BufferedReader(new FileReader(fileOrDir));
            int ch;
            int line = 1;
            int col = 0;
            String sb = "";
            FilePosition pos = new FilePosition(1, 0);
            do {
                col++;
                ch = reader.read();
                if (Syntax.isInWord((char) ch)) {
                    if (sb.equals(""))
                        pos = new FilePosition(line, col);
                    sb += ((char) ch);
                } else if (!sb.equals("")) {
                    this.occ.put(sb.toLowerCase(), fileOrDir.getPath(), pos);
                    sb = "";
                } else if ((char) ch == '\n') {
                    line++;
                    col = 0;
                }
            } while (ch != -1);
            reader.close();
            // System.out.println("reader closed succesfully");
        } else if (fileOrDir.isDirectory()) {
            if (fileOrDir.listFiles() != null) {
                for (File i : fileOrDir.listFiles()) {
                    // System.out.println("directory detected");
                    if (i.isDirectory() || i.isFile())
                        populateOccurrenceMap(i);
                    // System.out.println("FIle Checked");
                }
            }

        }
    }
}


