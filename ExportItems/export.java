
import edu.duke.*;
import org.apache.commons.csv.*;

public class export {
    public void tester() {
        FileResource fr = new FileResource("data/exportdata.csv");
        CSVParser parser = fr.getCSVParser();
        String country = "Nauru";
        System.out.println(countryInfo(parser,country));
        System.out.println();
        
        parser = fr.getCSVParser();
        String item1 = "fish";
        String item2 = "nuts";
        listExportersTwoProducts(parser,item1,item2);
        System.out.println();
        
        parser = fr.getCSVParser();
        String exportItem = "gold";
        int number = numberOfExporters(parser,exportItem);
        System.out.println(number + " countries exports " + exportItem);
        System.out.println();
        
        parser = fr.getCSVParser();
        String amount = "$999,999,999,999";
        bigExporters(parser,amount);
    }
    
    public String countryInfo(CSVParser parser, String country) {
        // If record equals with the given country, print it's name, exports and value.
        // If CSV file does not include the given country, print "NOT_FOUND".
        for(CSVRecord record : parser) {
            String currCountry = record.get("Country");
            if(currCountry.contains(country)) {
                String export = record.get("Exports");
                String value = record.get("Value (dollars)");
                return currCountry + ": " + export + ": " + value;
            }
        }
        return "NOT_FOUND";    
    }
    
    public void listExportersTwoProducts(CSVParser parser, String exportItem1, String exportItem2) {
        // If any country exports exportItem1 and exportItem2, print that country.
        for(CSVRecord record : parser) {
            String currExport = record.get("Exports");
            if(currExport.contains(exportItem1) && currExport.contains(exportItem2)) {
                String country = record.get("Country");
                System.out.println(country + " exports both " + exportItem1 + " and " + exportItem2);
            }
        }
    }
    
    public int numberOfExporters(CSVParser parser, String exportItem) {
        // Determine the how many country exports the exportItem.
        // If a country exports exportItem, add counter by 1.
        int counter = 0;
        for(CSVRecord record : parser) {
            String currExport = record.get("Exports");
            if(currExport.contains(exportItem)){
                counter++;
            }
        }
        return counter;
    }
    
    public void bigExporters(CSVParser parser, String amount) {
        // If any country has export value greater than amount, print it's name and value.
        for(CSVRecord record : parser) {
            String value = record.get("Value (dollars)");
            // Comparing lengths of values is enough. amount paramater will be like "$999,999.
            if(value.length() > amount.length()) {
                String currCountry = record.get("Country");
                System.out.println(currCountry + " " + value);
            }
        }
        
    }
    
    
}
