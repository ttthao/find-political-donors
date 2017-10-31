import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class FindPoliticalDonors {
    public static boolean isFields(String cmteId, String transAmt, String otherId) {
        Boolean isOtherId = (otherId != null && otherId.isEmpty());
        Boolean isCmteId = (cmteId != null && !cmteId.isEmpty());
        Boolean isTransAmt = (transAmt != null && !transAmt.isEmpty());

        if (isCmteId && isTransAmt && isOtherId)
            return true;
        return false;
    }

    public static boolean isZipCodeValid(String zipCode) {
        if (zipCode.length() < 5 || zipCode.isEmpty()) {
            return false;
        }
        return true;
    }

    public static void parseInput(String fileName)
    {
        // This will reference one line at a time
        String line = null;
        
        // Breaks entries on pipes
        Pattern pattern = Pattern.compile("\\|");

        // CMTE_ID to ZIP_CODE map
        // HashMap<String,Integer> cmteIdToZip = new HashMap<String,Integer>();  
        HashMap<String,HashMap<String,TransByZip>> cmteIdToZipCodeMap = new HashMap<String,HashMap<String,TransByZip>>();  

        // CMTE_ID to  map
        // HashMap<String,HashMap<String,TransByDate>> cmteIdToTransDateMap = new HashMap<String,HashMap<String,TransByDate>>();  

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                // Split on '|'
                String[] entry = pattern.split(line);

                // Every field is a string
                String cmteId = entry[0];
                String transAmt = entry[14];
                String otherId = entry[15];
                boolean isFieldsValid = isFields(cmteId, transAmt, otherId);

                // Skip invalid entries
                if (isFieldsValid) {
                    // Sort hm keys: https://stackoverflow.com/questions/9047090/how-to-sort-hashmap-keys
                    // Store map in map: https://stackoverflow.com/questions/5056708/storing-hashmap-in-a-hashmap

                    String zipCode = entry[10].substring(0, 5);
                    String transDate = entry[13];
                    boolean isZipCodeValid = isZipCodeValid(zipCode);

                    // Check if cmteId is a key in the zipCodeToTransByZip
                    HashMap<String,TransByZip> zipCodeToTransByZip = cmteIdToZipCodeMap.get(cmteId);
                    Integer transAmtInteger = Integer.parseInt(transAmt);
                    TransByZip transByZip;

                    // CMTE_ID is mapped to zip code map
                    if (zipCodeToTransByZip != null) {
                        // System.out.println("CMTE_ID exists in zipCodeToTransByZip");

                        // Integer transAmtInteger = Integer.parseInt(transAmt);
                        
                        // Get transactions per valid zipcode
                        // TransByZip transByZip = zipCodeToTransByZip.get(zipCode);
                        transByZip = zipCodeToTransByZip.get(zipCode);
                        
                        // Current zip code is mapped to transactions by zipcode object
                        if (transByZip != null) {
                            // Update zip code
                            // Add transaction
                            transByZip.addTransAmt(transAmtInteger);
                            
                            // Update streaming total amount
                            transByZip.setTotalAmount(transAmtInteger);

                            // Update streaming total contributions
                            transByZip.setTotalContributions();

                            // Rebalance
                            transByZip.rebalanceHeaps();

                            Integer runningMedium = transByZip.getMedian();
                            Integer totalContributions = transByZip.getTotalContributions();
                            Integer totalAmount = transByZip.getTotalAmount();

                            // Check
                            System.out.println(String.format ("%s|%s|%s|%s|%s", cmteId, zipCode, Integer.toString(runningMedium), Integer.toString(totalContributions), Integer.toString(totalAmount)));

                        } else {
                            // Current zip code is not in zip code map

                            // Create new TransByZip for current zip code
                            // TransByZip transByZip = new TransByZip();
                            transByZip = new TransByZip();

                            // Add transaction
                            transByZip.addTransAmt(transAmtInteger);
                            
                            // Update streaming total amount
                            transByZip.setTotalAmount(transAmtInteger);

                            // Update streaming total contributions
                            transByZip.setTotalContributions();

                            // Rebalance
                            transByZip.rebalanceHeaps();

                            Integer runningMedium = transByZip.getMedian();
                            Integer totalContributions = transByZip.getTotalContributions();
                            Integer totalAmount = transByZip.getTotalAmount();

                            // Check
                            System.out.println(String.format ("%s|%s|%s|%s|%s", cmteId, zipCode, Integer.toString(runningMedium), Integer.toString(totalContributions), Integer.toString(totalAmount)));
                            
                            // Track zipcode to transactions
                            zipCodeToTransByZip.put(zipCode, transByZip);
                        }

                    } else {
                        if (isZipCodeValid) {
                            // Integer transAmtInteger = Integer.parseInt(transAmt);

                            // Create new TransByZip for current zip code
                            // TransByZip transByZip = new TransByZip();
                            transByZip = new TransByZip();

                            // CMTE_ID wasn't found, create new mapping between id and zip map
                            // HashMap<String,TransByZip> zipCodeToTransByZip = new HashMap<String, TransByZip>();
                            zipCodeToTransByZip = new HashMap<String, TransByZip>();
                            
                            // Add transaction
                            transByZip.addTransAmt(transAmtInteger);

                            // Update streaming total amount
                            transByZip.setTotalAmount(transAmtInteger);

                            // Update streaming total contributions
                            transByZip.setTotalContributions();

                            // Rebalance
                            transByZip.rebalanceHeaps();

                            Integer runningMedium = transByZip.getMedian();
                            Integer totalContributions = transByZip.getTotalContributions();
                            Integer totalAmount = transByZip.getTotalAmount();

                            // Check
                            System.out.println(String.format ("%s|%s|%s|%s|%s", cmteId, zipCode, Integer.toString(runningMedium), Integer.toString(totalContributions), Integer.toString(totalAmount)));
                            
                            // Track zipcode to transactions by zipcode
                            zipCodeToTransByZip.put(zipCode, transByZip);

                            // Track cmteid to zipCodeMap
                            cmteIdToZipCodeMap.put(cmteId, zipCodeToTransByZip);
                        }
                    }

                    // Check if cmteId is a key in the cmteIdToTransDateMap

                }
            }
            // for(Map.Entry m:cmteIdToZip.entrySet()){  
            //     System.out.println(m.getKey()+" "+m.getValue());  
            // }  

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

