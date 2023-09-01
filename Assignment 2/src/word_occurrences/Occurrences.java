
package word_occurrences;

import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.TreeSet;

public class Occurrences {

    // Maps words -> filename -> sets of their Positions in the file.
    private final TreeMap<String, TreeMap<String, TreeSet<FilePosition>>> occMap;

    public Occurrences(String rootDirPath) throws FileNotFoundException {
        occMap = new TreeMap<>();
        FileWalker walker = new FileWalker(rootDirPath, this);
       // System.out.println("created"); used for testing
        walker.populateOccurrenceMap();
    }

    /*
        Called by FileWalker to add an occurrence to the occMap
     */
    void put(String word, String filePath, FilePosition pos) {
        word = word.toLowerCase();
        if(this.occMap.get(word) != null){ // checking if the word is in the TreeMap
            if(this.occMap.get(word).get(filePath) != null){ // checking if the word is in the TreeMap and the filepath is too
                this.occMap.get(word).get(filePath).add(pos);
            } else {
                TreeSet<FilePosition> kek = new TreeSet<>();
                kek.add(pos);
                this.occMap.get(word).put(filePath, kek);
            }
        } else {
            TreeSet<FilePosition> kek = new TreeSet<>();
            TreeMap<String, TreeSet<FilePosition>> lol = new TreeMap<>();
            kek.add(pos);
            lol.put(filePath, kek);
            this.occMap.put(word, lol);
        }
        //System.out.println("added"); Checked if the put function worked well
    }


    /**
     * @return the number of distinct words found in the files
     */
    public int distinctWords() {
        return this.occMap.size();
    }

    /**
     * @return the number of total word occurrences across all files
     */
    public int totalOccurrences() {
        int sum = 0;
        for(String i = this.occMap.firstKey(); i != null; i = this.occMap.higherKey(i)) {
            for(String j = this.occMap.get(i).firstKey(); j != null; j = this.occMap.get(i).higherKey(j)){
                sum += this.occMap. get(i).get(j).size();
            }
        }
        return sum;
    }

    /**
     * Finds the total number of occurrences of a given word across
     * all files.  If the word is not found among the occurrences,
     * simply return 0.
     *
     * @param word whose occurrences we are counting
     * @return the number of occurrences
     */
    public int totalOccurrencesOfWord(String word) {
        int sum = 0;
        word = word.toLowerCase();
        if(this.occMap.get(word) != null) {
            for (String i = this.occMap.get(word).firstKey(); i != null; i = this.occMap.get(word).higherKey(i)) {
                sum += this.occMap.get(word).get(i).size();
            }
        }
        return sum;
    }

    /**
     * Finds the total number of occurrences of a given word in the given file.
     * If the file is not found in Occurrences, or if the word does not occur
     * in the file, simply return 0.
     *
     * @param word whose occurrences we are counting
     * @param filepath of the file
     * @return the number of occurrences
     */
    public int totalOccurrencesOfWordInFile(String word, String filepath) {
        word = word.toLowerCase();
        if(this.occMap.get(word).get(filepath) != null)
            return this.occMap.get(word).get(filepath).size();
        else
            return 0;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        for(String i = this.occMap.firstKey(); i != null; i = this.occMap.higherKey(i)) {
            String word = i;
            sb.append("\"" + word + "\" has " + totalOccurrencesOfWord(word) +
                    " occurrence(s):\n");
            for (String j = this.occMap.get(i).firstKey(); j != null; j = this.occMap.get(i).higherKey(j)) {
                for (FilePosition k : this.occMap.get(i).get(j)) {
                    String filepath = j;
                    FilePosition pos = k;


                    sb.append("   ( file: \"" + filepath + "\"; " + pos + " )\n");
                }
            }
        }
        return sb.toString();
    }
}


