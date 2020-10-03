/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 *  
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyBirths {
    // There is no column title row so CSVParser method take an argument. 
    // (public CSVParser getCSVParser(boolean withHeader))
    // First column Name, Second Gender, Third count.
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            // Count total births for both boys and girls.
            totalBirths += numBorn;
            // If gender is "M" which means Male, add its count to totalBoys.
            // Otherwise add to totalGirls.
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
            }
            else {
                totalGirls += numBorn;
            }
        }
        // Print total, female and male births.
        System.out.println("total births = " + totalBirths);
        System.out.println("female girls = " + totalGirls);
        System.out.println("male boys = " + totalBoys);
    }

    public void testTotalBirths () {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    
    public int getRank(int year, String name, String gender) {
        // Return the rank of the name in the file for the given gender, where rank 1 is the name with the largest number of births.
        int rank = 0;
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        // FileResource fr = new FileResource("us_babynames/us_babynames_test/yob" + year + "short.csv");
        CSVParser parser = fr.getCSVParser(false);
        // CSV file sorted by total births for each gender.
        for(CSVRecord record : parser) {   
            // If current gender is equals to given gender, count rank by 1.
            if(record.get(1).equals(gender)) {
                rank++;
                // If current name is equal to the given name, return the given names rank.
                if(record.get(0).equals(name)) {
                    String name1 = record.get(1);
                    //System.out.println("Top gender = " + name1);
                    return rank;
                }
            }
        }
        // If given name is not in the file, return -1.
        return -1;
    }
    
    public String getName(int year, int rank, String gender) {
        // Return the name of the person in the file at this rank, for the given gender, where rank 1 is the name with the largest number of births.
        FileResource fr = new FileResource();
        int count = 0;
        for(CSVRecord record : fr.getCSVParser(false)) {
            // For the given gender, count rank by 1.
            if(record.get(1).contains(gender)) {
                count++;
                // When count reaches the given rank, print the name. 
                if(count == rank) {
                    String currName = record.get(0);
                    return currName;
                }
            }
        }
        return "NO NAME";   
    }
    
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        // Determine what would your name be if you were born in a different year
        // Get the rank of given name.
        int rank = getRank(year,name,gender);
        // Determine new name for the another year and the same rank.
        String newName = getName(newYear, rank, gender);
        // Print result.
        System.out.println(name + " born in " + year + " would be " + newName + " if (he/she) was born in " + newYear);
        
    }
    
    public int yearOfHighestRank(String name, String gender) {
        DirectoryResource dr = new DirectoryResource();
        int rank = 999999999;
        int year = -1;
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            String currYearStr = f.getName();
            currYearStr = currYearStr.substring(3,7);
            int currYear = Integer.parseInt(currYearStr);
            int currRank = getRank(currYear,name,gender); 
            if(currRank == -1) {
                continue;
            }
            if(currRank < rank) {
                rank = currRank;
                year = currYear;
            }
        }
        
        
        return year;
    }
    
    public double getAverageRank(String name, String gender) {
        DirectoryResource dr = new DirectoryResource();
        double total = 0;
        int counter = 0;
        // For each file, determine its rank.
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            // Get file name.
            String currYearStr = f.getName();
            // All file name contains its year. To get current year, trim the file name.
            currYearStr = currYearStr.substring(3,7);
            // Convert year string to integer.
            int currYear = Integer.parseInt(currYearStr);
            // Get rank for the given file.
            int currRank = getRank(currYear,name,gender);
            // Return -1 if the name is not ranked in any of the selected files.
            if(currRank == -1) {
                return -1;
            }
            // To calculate average, find the total rank and file count.
            total = total + currRank;
            counter++;
        }  
        return total/counter;   
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        // Get the rank of the name in given year.
        int rank = getRank(year,name,gender);
        int totalBirths = 0;
        int currRank = 0;
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            // Get current gender.
            String currGender = rec.get(1);
            // Get current name.
            String currName = rec.get(0);
            // Get current rank.
            currRank = getRank(year,currName,currGender);
            // For the given gender:
            if(currGender.equals(gender)) {
                // If it has better rank than current, add it's number to totalBirths.
                // Otherwise, return totalBirths. 
                if(currRank < rank) {
                    totalBirths += Integer.parseInt(rec.get(2));
                }
                else{
                    return totalBirths;
                }
            }
        }
        // If it is not in the file, return -1.
        return -1;   
    }
    
    public int getTotalBirthsRankedHigher2(int year, String name, String gender){
        int totalNumBorn = 0;
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        CSVParser parser = fr.getCSVParser();
        for(CSVRecord record : parser){
            String childGender = record.get(1);
            String childName = record.get(0);
            if(childGender.equals(gender)){
                if(childName.equals(name)){
                    return totalNumBorn;
                }
                else{
                    int numBorn = Integer.parseInt(record.get(2));
                    totalNumBorn += numBorn;
                }
            }
        }
        return -1;
    }
    
    public void numberOfNames(){
        int male = 0, female = 0;
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser(false);
        for(CSVRecord rec : parser){
            String gender = rec.get(1);
            if(gender.equals("M")){
                male++;
            }
            else{
                female++;
            }
        }
        System.out.println("Number of males: " + male + "\n" + "Number of females: " + female);
    }
}
