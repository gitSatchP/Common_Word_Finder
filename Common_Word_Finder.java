import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Class that reads a .txt file and prints the n most common words in the document.
 *
 * @author Satchel Peterson
 */

public class CommonWordFinder {
    //creating a variable with reference to the MyMap interface
    static MyMap<String, Integer> map;
    static int limit;

    /**
     * Helper method for checking arguments.
     *
     * @param args a String array of the command line arguments.
     */
    public static void checkArgs(String[] args){
        //checks if the number of arguments specified is incorrect
        if(args.length!=2 && args.length!=3){
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }
        //verifies that the input file exists
        File file = new File(args[0]);
        boolean fileExists = file.exists();
        if(fileExists == false){
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        }
        //verifies that the data structure specified is correct
        if(!args[1].equals("bst")&&!args[1].equals("avl")&&!args[1].equals("hash")){
            System.err.println("Error: Invalid data structure '" + args[1] + "' received." );
            System.exit(1);
        }
        //verifies that limit argument is correct if provided, otherwise initializes limit to 10
        if (args.length==3) {
            try {
                limit = Integer.parseInt(args[2]);
                if(limit<1){
                    System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                    System.exit(1);
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error: Invalid limit '" + args[2] + "' received.");
                System.exit(1);
            }
        }else{
            limit = 10;
        }
    }

    /**
     * Parses the input file. Based on the second argument specified
     * initializes static variable map as either a BSTmap, AVLTreeMap, or MyHashMap object.
     * Calls helper method process().
     *
     * @param args a String array of the command line arguments.
     */
    public static void parse(String[] args) {
        //catches I/O error if it occurs as the reading takes place
        File f = new File(args[0]);
        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + args[0] + "'." );
            System.exit(1);
        }
        BufferedReader br = new BufferedReader(fr);

        //initializes map as either a BSTMap, AVLTreeMap, or MyHashMap object.
        switch (args[1]) {
            case "bst" -> map = new BSTMap<>();
            case "avl" -> map = new AVLTreeMap<>();
            case "hash" -> map = new MyHashMap<>();
        }
        process(br, args);
    }

    /**
     * Helper method for parsing the input file. Using ASCII representation,
     * checks if a character is uppercase, lowercase or an apostrophe, a space
     * or end of line character, or a hyphen not at the start of a new word.
     * If uppercase, the character is converted to lowercase before adding it to
     * a string builder that keeps track of valid characters in the current word.
     * If lowercase or an apostrophe, the character is added to the string builder.
     * If a space or end of line character, the contents of the string builder are
     * put in the map by first checking if the same word has already been put in the
     * map and if so incrementing the count by 1. Then the string builder is emptied
     * and boolean newWord is set to true.If newWord is false, and the character is a
     * hyphen, the character is added to the string builder. Every other ASCII is
     * considered an invalid number and is not considered as part of a word.
     *
     * @param br   a buffered reader for reading the .txt file character by character.
     * @param args a String array of the command line arguments.
     */
    public static void process(BufferedReader br, String[] args){
        StringBuilder builder = new StringBuilder();
        boolean newWord = true;

        //catches I/O error if it occurs as the reading takes place
        try {
            int character = br.read();
            while(character!=-1){
                if(character>=65 && character<=90){
                    //checks if the character is uppercase and if so converts to lowercase and adds
                    //to stringbuilder
                    char c = Character.toLowerCase((char)character);
                    builder.append(c);
                    newWord = false;
                } else if (character==39|| character>=97 && character<=122) {
                    //checks if the character is valid and if so adds to stringbuilder
                    builder.append((char)character);
                    newWord = false;
                }else if((character==32 || character==10 || character ==13) && !builder.isEmpty()){
                    //checks if the character is a space or end of line character.
                    //If so, adds current builder to data structure by first checking if the word
                    //is already in the map to update the count, then Stringbuilder is emptied,
                    //and newWord set to true.
                    Integer value = map.get(builder.toString());
                    if(value == null){
                        map.put(builder.toString(), 1);
                    }else{
                        map.put(builder.toString(), value+1);
                    }
                    builder.delete(0, builder.length());
                    newWord = true;
                }else if(character == 45 && !newWord){
                    //checks if the character is a hyphen and also not the start of a word
                    //and if so adds to stringbuilder
                    builder.append((char)character);
                }
                character = br.read();
            }
            //adds the last word to the map if the last character was not an end of line character
            if(!builder.isEmpty()){
                Integer value = map.get(builder.toString());
                if(value == null){
                    map.put(builder.toString(), 1);
                }else{
                    map.put(builder.toString(), value+1);
                }
                builder.delete(0, builder.length());
            }
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + args[0] + "'." );
            System.exit(1);
        }
    }

    /**
     * Formats and sorts the <String, Integer> data in the array by the following rules for
     * alignment, sorting, and leading spaces: starting with 1, each line begins with the number of
     * the word and a period. The number is right-aligned to the width of the largest number. The
     * word in all lowercase letters appears after the period, left-aligned with one space between
     * the period and the word. If two lowercase words have the same count, the words are alphabetized
     * in the output. The count of the words appears at the end of each line, with one space
     * between the longest word and the count.
     *
     * @param map a reference to the map that contains all words found in the text document and their
     * associated counts.
     */
    public static void output(MyMap<String, Integer> map){
        int uniqueWords = map.size();
        //total unique words
        System.out.println("Total unique words: " + uniqueWords);

        //right align numbers
        //convert the map size or limit to a string to
        //get the length which yields the width of the widest number
        int totalNums = Math.min(uniqueWords, limit);
        final int width = String.valueOf(totalNums).length();

        //get sorted array of entries and find the width of the longest string
        Pair[] entries = sorted();
        int maxLength = 0;
        for(int i=0; i<totalNums; i++){
            maxLength = Math.max(maxLength, entries[i].key.length());
        }

        //formatting the output
        for(int i = 0; i<totalNums; i++){
            //calculate the number of spaces needed to right-align numbers
            int currentNumberWidth = String.valueOf(i+1).length();
            int numberSpaces = width - currentNumberWidth;
            StringBuilder numSpaces = new StringBuilder();
            for(int j = 0; j < numberSpaces; j++){
                numSpaces.append(" ");
            }
            //calculate the number of spaces between word and word count
            int currentWordWidth = entries[i].key.length();
            int wSpaces = maxLength - currentWordWidth + 1;
            StringBuilder wordSpaces = new StringBuilder();
            for(int j = 0; j < wSpaces; j++){
                wordSpaces.append(" ");
            }
            //format for each line
            System.out.print(numSpaces.toString() + (i+1) + ". " + entries[i].key +
                    wordSpaces + entries[i].value);
            //add new line
            System.out.print(System.lineSeparator());
        }

    }

    /**
     * Converts all entries in the map to Pair objects in an array.
     */
    public static Pair[] mapToArray(){
        //Creates a map iterator, encapsulates each <String, Integer> entry as
        //a Pair, and adds it to an array.
        Pair[] pairs = new Pair[map.size()];
        Iterator<Entry<String, Integer>> iterator = map.iterator();
        int i = 0;
        while(iterator.hasNext()){
            Entry<String, Integer> current = iterator.next();
            Pair pair = new Pair(current.key, current.value);
            pairs[i] = pair;
            i++;
        }
        return pairs;
    }

    /**
     * Sorts the array of Pair objects.
     *
     * @return a sorted array of Pairs
     */
    public static Pair[] sorted(){
        Pair[] sorted = mapToArray();
        Arrays.sort(sorted);
        return sorted;
    }

    /**
     * Class for encapsulating a key-value entry object as a Pair.
     */
    private static class Pair implements Comparable<Pair>{
        String key;
        Integer value;

        /**
         * Creates a key-value pair.
         *
         * @param key   the specified key to encapsulate
         * @param value the value to associate with the key
         */
        Pair(String key, Integer value){
            this.key = key;
            this.value = value;
        }

        /**
         * Compares Pair values first to sort using Arrays.sort() by descending
         * order and if values are equal, compares key to sort in alphabetically
         * ascending order.
         *
         * @param p   the Pair to be compared with the implicit parameter of
         *            the method call
         */
        @Override
        public int compareTo(Pair p){
            if(!this.value.equals(p.value)){
                return p.value - this.value;
            }else{
                return this.key.compareTo(p.key);
            }
        }
    }

    /**
     * Main method which first calls checkArgs(args) checking correct argument
     * input, then parse(args) parsing the text file and inserting it to map,
     * and finally output(map) printing the words and count according to specified
     * formatting directions in the method comments for output(MyMap<String, Integer> map).
     *
     * @param args   the Pair to be compared with the implicit parameter of
     *            the method call
     */
    public static void main(String[] args) {
        checkArgs(args);
        parse(args);
        output(map);
    }
}