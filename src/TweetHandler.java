/*
 * Hollie Wilson
 * CS 3354 
 * Assignment 2
 */
package sentimentanalysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 *
 * @author holliewilson
 */
public class TweetHandler implements TweetHandlerInterface {
    
    public List<AbstractTweet> database;
    
    public TweetHandler(){
        database = new ArrayList<AbstractTweet>();
    }
    
     public List<AbstractTweet> loadTweetsFromText(String filePath) {
          
        File tweetFile = new File(filePath);
        List <AbstractTweet> tweetList = new ArrayList<AbstractTweet>();
        
        try {
           Scanner tweetFileReader = new Scanner(new FileReader(tweetFile));
           
           while(tweetFileReader.hasNextLine()) {                                                                          
            
               String line = tweetFileReader.nextLine();   
               AbstractTweet t = parseTweetLine(line);
               tweetList.add(t);
           }
           return tweetList;
        } catch(IOException e) {
            System.out.println("File Not Found.");
            return new ArrayList<AbstractTweet>();
        }                
        
    }
     
     public AbstractTweet parseTweetLine(String tweetLine){
         
         String[] temp = tweetLine.split("\",\""); 
         String tweet = temp[5];
         
         int target = Integer.parseInt(temp[0].replaceAll("\"",""));
         int id = Integer.parseInt(temp[1].replaceAll("\"",""));
         try {
             Date date = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(temp[2]);
             String flag = temp[3];
             String user = temp[4];
             String text = temp[5];
         
             AbstractTweet result = new Tweet(target, id, date, flag, user, text);
             return result;
         } catch(ParseException p) {
             System.out.println("Error.");
             return null;
         }
         
     }
     
     public int classifyTweet(AbstractTweet tweet){
         return SentimentAnalyzer.getParagraphSentiment(tweet.getText());    
     }
     
     public void addTweetsToDB(List<AbstractTweet> tweets) {
         database.addAll(tweets);
     }
     
     public void deleteTweet(int id) {     
         for(AbstractTweet tweet: database) {
             if(tweet.getId() == id){
                database.remove(tweet);
                break;
             }
         }
         System.out.println("Tweet " + id + " has been deleted.");
     }
     
     public void saveSerialDB() {
         try {
            String dataBaseFileName = "database.ser";
         
            FileOutputStream outFile = new FileOutputStream(dataBaseFileName);
            ObjectOutputStream out = new ObjectOutputStream(outFile);

            out.writeObject(database);

            out.close();
            outFile.close();
         } catch(IOException e) {
             System.out.println("File Not Found.");
         }
     }
     
     public void loadSerialDB() {
         try {
            String dataBaseFileName = "database.ser";
            
            FileInputStream inFile = new FileInputStream(dataBaseFileName);
            ObjectInputStream in = new ObjectInputStream(inFile);
            database = (ArrayList)in.readObject();
            inFile.close();
            in.close();
        } catch(Exception e){
            System.out.println("File Not Found.");
        }
     }
     
     public List<AbstractTweet> searchByUser(String user) {
         List <AbstractTweet> userList = new ArrayList<AbstractTweet>();
        
         for(AbstractTweet tweet: database) {
            if(tweet.getUser().equals(user)) {
                userList.add(tweet);
            }
         }       
         return userList;
     }
     
     public List<AbstractTweet> searchByDate(Date date) {
         List <AbstractTweet> dateList = new ArrayList<AbstractTweet>();

         for(AbstractTweet tweet: database) {
            if(tweet.getDate().equals(date)) {
                dateList.add(tweet);
            }
         }         
         return dateList;
     }
     
     public List<AbstractTweet> searchByFlag(String flag) {
         List <AbstractTweet> flagList = new ArrayList<AbstractTweet>(); 
   
         for(AbstractTweet tweet: database) {
            if(tweet.getFlag().equals(flag)) {
                flagList.add(tweet);
            }
         }
         return flagList;
     }
     
     public List<AbstractTweet> searchBySubstring(String substring) {
         List <AbstractTweet> tweetList = new ArrayList<AbstractTweet>();
        
         for(AbstractTweet tweet: database) {
            if(tweet.getText().contains(substring)) {
                tweetList.add(tweet);
            }
         }
         return tweetList;
     }         
         
}
