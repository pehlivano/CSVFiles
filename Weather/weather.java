
import org.apache.commons.csv.*;
import edu.duke.*;
import java.io.*;

public class weather {
    public CSVRecord coldestHourInFıle(CSVParser parser) {
        // Determine the coldest hour in the file.
        CSVRecord coldestSoFar = null;
        for(CSVRecord currRecord : parser) {
            coldestSoFar = getColdestOfTwo(currRecord,coldestSoFar);
        }
        
        return coldestSoFar;
    }
   
    // It is a helper method for comparing two records.
    private CSVRecord getColdestOfTwo(CSVRecord currRecord, CSVRecord coldestSoFar){
        // In the first record, choose coldest record as first record.
        if(coldestSoFar == null) {
            coldestSoFar = currRecord;
        }
        // Otherwise, compare current record with coldestSoFar. If current temperature lower, choose it as coldest.
        else {
            double currTemp = Double.parseDouble(currRecord.get("TemperatureF"));
            double coldestTemp = Double.parseDouble(coldestSoFar.get("TemperatureF"));
            if(currTemp < coldestTemp) {
                coldestSoFar = currRecord;
            }
        }
        return coldestSoFar;
    }
    
    public void testColdestHourInFile() {
        FileResource fr = new FileResource();
        CSVRecord coldest = coldestHourInFıle(fr.getCSVParser());
        System.out.println("Coldest temperature was " + coldest.get("TemperatureF") + " " + "Hour is " + coldest.get("TimeEST"));      
        
    }
    
    public String fileWithColdestTemperature() {
        //Return a string that is the name of the file from selected files that has the coldest temperature.
        DirectoryResource dr = new DirectoryResource();    
        CSVRecord coldestSoFar = null;
        File fileName = null;
        for(File f : dr.selectedFiles()) {
            // For each file, calculate the coldest hour. 
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(); 
            CSVRecord currColdest = coldestHourInFıle(parser);
            // Compare coldest temperatures in the given files.
            // Choose coldest temperature in the first file as coldestSoFar in the first case. 
            if(coldestSoFar == null) {
                coldestSoFar = currColdest;
                fileName = f;
            }
            // In other cases, compare and choose coldest.
            else{
                double currTemp = Double.parseDouble(currColdest.get("TemperatureF"));
                double coldestTemp = Double.parseDouble(coldestSoFar.get("TemperatureF"));
                if(currTemp < coldestTemp) {
                    coldestSoFar = currColdest;
                    fileName = f;
                }
            }
        }
        
        return fileName.getAbsolutePath();
    }
    
    public void testFileWithColdestTemperature() {
        
        String coldestFilePath = fileWithColdestTemperature();
        File f = new File(coldestFilePath);
        String nameOfFile = f.getName();
        System.out.println("Coldest day was in file " + nameOfFile);
        
        FileResource fr = new FileResource(f);
        CSVRecord coldestTemp = coldestHourInFıle(fr.getCSVParser());
        System.out.println("Coldest Temperature on that day was " + coldestTemp.get("TemperatureF"));
        
        System.out.println("All the temperatures on the coldest day were:");
        CSVParser parser = fr.getCSVParser();
        for(CSVRecord record: parser) {
            String Time = record.get("DateUTC");
            double temp = Double.parseDouble(record.get("TemperatureF"));
            System.out.println(Time + " " + temp);
        }
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser) {
        // Calculate the lowest humidity in given file.
        CSVRecord lowestHumidity = null;
        for(CSVRecord currHumidity : parser) {
            lowestHumidity = lowestHumidityOfTwo(currHumidity,lowestHumidity);
        }
        return lowestHumidity;
    }
    
    // It is a helper method for comparing two humidity records.
    private CSVRecord lowestHumidityOfTwo(CSVRecord currHumidity, CSVRecord lowestHumidity) {
        // For the first record.
        if(lowestHumidity == null) {
                lowestHumidity = currHumidity;
        }
        else {
            // In some case, humidity records is not available as "N/A". 
            if( !(currHumidity.get("Humidity").contains("N/A")) && !(lowestHumidity.get("Humidity").contains("N/A")) ) {
                double currHum = Double.parseDouble(currHumidity.get("Humidity"));
                double lowestHum = Double.parseDouble(lowestHumidity.get("Humidity"));
                if(currHum < lowestHum) {
                    lowestHumidity = currHumidity;
                }
            }
        }
        return lowestHumidity;
    }
    
    public void testLowestHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csv = lowestHumidityInFile(parser);
        
        String time = csv.get("DateUTC");
        int humidity = Integer.parseInt(csv.get("Humidity"));
        System.out.println("Lowest Humidity was " + humidity + " at " + time); 
        
    }
    
    public CSVRecord lowestHumidityInManyFiles() {
        // Calculate lowest humidity in given files.
        CSVRecord lowestHumidity = null;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
            // For each file, calculate it's lowest humidity, and compare it with other files.
            FileResource fr = new FileResource(f);
            // Get current humidity.
            CSVRecord currHumidity = lowestHumidityInFile(fr.getCSVParser());
            // Compare with lowest so far.
            lowestHumidity = lowestHumidityOfTwo(currHumidity, lowestHumidity);
        }
        
        return lowestHumidity;
    }
    
    public void testLowestHumidityInManyFiles() {
        CSVRecord csv = lowestHumidityInManyFiles();
        String time = csv.get("DateUTC");
        int humidity = Integer.parseInt(csv.get("Humidity"));
        System.out.println("Lowest Humidity was " + humidity + " at " + time);
    }
    
    public double averageTemperatureInFile(CSVParser parser) {
        int count = 0;
        double total = 0;
        for(CSVRecord record : parser) {
            // Count how many data exist in the file.
            count++;
            // Sum temperatures in the file.
            total = total + (Double.parseDouble(record.get("TemperatureF")));
        }
        // Return the average.
        return total/count;
    }
    
    public void testAverageTemperatureInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double average = averageTemperatureInFile(parser);
        System.out.println("Average temperature in file is " + average);
    }
    
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value) {
        int count = 0;
        double total = 0;
        for(CSVRecord record : parser) {
            // Count only records that has greater or eqeal humidity value than 'value' parameter.
            int currHumidity = Integer.parseInt(record.get("Humidity"));
            double currTemperature = Double.parseDouble(record.get("TemperatureF"));
            if(currHumidity >= value) {
                count++;
                total = total + currTemperature;
            }
        }
        return total/count;
    }
    
    public void testAverageTemperatureWithHighHumidityInFile() {
        int value = 80;
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double average = averageTemperatureWithHighHumidityInFile(parser,value);
        //System.out.println(average);
        if(Double.isNaN(average)) {
            System.out.println("No temperatures with that humidity");
        }
        else {
            System.out.println("Average Temp when high Humidity is " + average);
        }
    }
}
