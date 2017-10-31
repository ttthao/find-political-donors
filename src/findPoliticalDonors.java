import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class findPoliticalDonors {
    public static void parseInput(String fileName)
    {
        // This will reference one line at a time
        String line = null;
        
        // Breaks entries on pipes
        Pattern pattern = Pattern.compile("\\|");

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                // Split on '|'
                String[] entry = pattern.split(line);

                String cmteId = entry[0];
                String transAmt = entry[14];
                System.out.println(Arrays.toString(entry));
            }

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            // Or we could just do this: 
            // ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // The name of the file to open.
        String fileName = args[0];

        // Read itcont.txt from arg[0],
        // For each line, assert checks
        // If valid, push to data structures
        parseInput(fileName);
    }
}