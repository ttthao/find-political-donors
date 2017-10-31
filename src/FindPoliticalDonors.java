import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.nio.*;

public class FindPoliticalDonors {
    // Skips entry if id, contribution amount or other id is invalid
    public static boolean isFields(String cmteId, String transAmt, String otherId) {
        Boolean isOtherId = (otherId != null && otherId.isEmpty());
        Boolean isCmteId = (cmteId != null && !cmteId.isEmpty());
        Boolean isTransAmt = (transAmt != null && !transAmt.isEmpty());

        if (isCmteId && isTransAmt && isOtherId)
            return true;
        return false;
    }

    // Determines if zip code is valid
    public static boolean isZipCode(String zipCode) {
        if (zipCode.length() < 5 || zipCode.isEmpty()) {
            return false;
        }
        return true;
    }

    // Determins if transaction date is valid
    public static boolean isTransDate(String transDate) {
        DateFormat format = new SimpleDateFormat("mmddyyyy");
        format.setLenient(false);
        try {
            format.parse(transDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Deletes and recreates output files
    public static void resetFiles(String zipCodeFilePath, String dateFilePath) {
        File zipCodeFile = new File(zipCodeFilePath);
        File dateFile = new File(dateFilePath);

        try {
            if (zipCodeFile.exists()) {
                zipCodeFile.delete();
                zipCodeFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (dateFile.exists()) {
                dateFile.delete();
                dateFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseInput(String inputFilePath, String zipCodeFilePath, String dateFilePath) {
        int ID_INDEX = 0;
        int TRANS_AMT_INDEX = 14;
        int OTHER_ID_INDEX = 15;
        int ZIP_CODE_INDEX = 10;
        int TRANS_DATE_INDEX = 13;

        // This will reference one line at a time
        String line = null;
        
        // Breaks entries on pipes
        Pattern pattern = Pattern.compile("\\|");

        // CMTE_ID to ZIP_CODE map
        HashMap<String,HashMap<String,TransByZip>> cmteIdToZipCodeMap = new HashMap<String,HashMap<String,TransByZip>>();  

        // CMTE_ID to date map
        HashMap<String,HashMap<String,TransByDate>> cmteIdToTransDateMap = new HashMap<String,HashMap<String,TransByDate>>();  

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(inputFilePath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                // Split on '|'
                String[] entry = pattern.split(line);

                // Every field is a string
                String cmteId = entry[ID_INDEX];
                String transAmt = entry[TRANS_AMT_INDEX];
                String otherId = entry[OTHER_ID_INDEX];
                boolean isFields = isFields(cmteId, transAmt, otherId);

                // Skip invalid entries
                if (isFields) {
                    String zipCode = entry[ZIP_CODE_INDEX].substring(0, 5);
                    String transDate = entry[TRANS_DATE_INDEX];
                    boolean isZipCode = isZipCode(zipCode);
                    boolean isTransDate = isTransDate(transDate);

                    // Transaction amount string
                    Integer transAmtInteger = Integer.parseInt(transAmt);
                    
                    // Track medianvals_by_zip
                    if (isZipCode) {
                        // Check if current cmteid maps to zipcode-to-trans map
                        HashMap<String,TransByZip> zipCodeToTransByZip = cmteIdToZipCodeMap.get(cmteId) != null 
                                                                         ? cmteIdToZipCodeMap.get(cmteId) 
                                                                         : new HashMap<String, TransByZip>();

                        TransByZip transByZip = zipCodeToTransByZip.get(zipCode) != null 
                                                ? zipCodeToTransByZip.get(zipCode) 
                                                : new TransByZip(); 

                        // Update median, total amount and contributions for this (id, zip)
                        transByZip.update(transAmtInteger);

                        // Track new zipcode to transactions by zipcode
                        if (!zipCodeToTransByZip.containsKey(zipCode)) {
                           zipCodeToTransByZip.put(zipCode, transByZip);
                        }

                        // Track new cmteid to zipCodeMap
                        if(!cmteIdToZipCodeMap.containsKey(cmteId)) {
                           cmteIdToZipCodeMap.put(cmteId, zipCodeToTransByZip); 
                        }

                        String zipStr = transByZip.toString(cmteId, zipCode);
                        try {
                            // FileReader reads text files in the default encoding.
                            FileWriter zipFileWriter = new FileWriter(zipCodeFilePath, true);
                
                            // Always wrap FileReader in BufferedReader.
                            BufferedWriter bufferedZipWriter = new BufferedWriter(zipFileWriter);

                            bufferedZipWriter.write(zipStr);
                            bufferedZipWriter.write("\n");
                            bufferedZipWriter.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("Unable to open file '" + zipCodeFilePath + "'");
                        } catch (IOException ex) {
                            System.out.println("Error writing file '" + zipCodeFilePath + "'");
                            // Or we could just do this: 
                            // ex.printStackTrace();
                        }
                    }
                    // Track medianvals_by_date
                    if (isTransDate) {
                        // Check if current cmteid maps to date-to-trans map
                        HashMap<String,TransByDate> transDateToTransByDate = cmteIdToTransDateMap.get(cmteId) != null 
                                                                         ? cmteIdToTransDateMap.get(cmteId) 
                                                                         : new HashMap<String, TransByDate>();

                        TransByDate transByDate = transDateToTransByDate.get(transDate) != null 
                                                ? transDateToTransByDate.get(transDate) 
                                                : new TransByDate(); 
                        
                        // Update median, total amount and contributions for this (id, date)
                        transByDate.update(transAmtInteger);

                        // Track new date to transactions by date
                        if (!transDateToTransByDate.containsKey(transDate)) {
                           transDateToTransByDate.put(transDate, transByDate);
                        }

                        // Track new cmteid to trans date
                        if(!cmteIdToTransDateMap.containsKey(transDate)) {
                           cmteIdToTransDateMap.put(cmteId, transDateToTransByDate); 
                        }
                    }
                }
            }

            // Print medianvals_by_date by alphabetical order on id, then chrono. on date
            Object[] cmteIdKeys = cmteIdToTransDateMap.keySet().toArray();
            Arrays.sort(cmteIdKeys);

            for(Object cmteId : cmteIdKeys) {
                Object[] transDateKeys = cmteIdToTransDateMap.get(cmteId).keySet().toArray();
                Arrays.sort(transDateKeys);

                for(Object transDate : transDateKeys) {
                    HashMap<String,TransByDate> td = cmteIdToTransDateMap.get(cmteId);
                    TransByDate trans = td.get(transDate);
                    String dateStr = trans.toString((String) cmteId, (String) transDate);
                    try {

                        // FileReader reads text files in the default encoding.
                        FileWriter dateFileWriter = new FileWriter(dateFilePath, true);
            
                        // Always wrap FileReader in BufferedReader.
                        BufferedWriter bufferedDataWriter = new BufferedWriter(dateFileWriter);

                        bufferedDataWriter.write(dateStr);
                        bufferedDataWriter.write("\n");
                        bufferedDataWriter.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println("Unable to open file '" + dateFilePath + "'");
                    } catch (IOException ex) {
                        System.out.println("Error writing file '" + dateFilePath + "'");
                        // Or we could just do this: 
                        // ex.printStackTrace();
                    }
                }
            }

            /*
            */

            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + inputFilePath + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + inputFilePath + "'");
            // ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputFilePath = args[0];
        String zipCodeFilePath = args[1];
        String dateFilePath = args[2];

        resetFiles(zipCodeFilePath, dateFilePath);
        parseInput(inputFilePath, zipCodeFilePath, dateFilePath);
    }
}

