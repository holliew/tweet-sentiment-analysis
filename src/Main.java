/*
 * Hollie Wilson
 * CS 3354 
 * Assignment 2
 */
package sentimentanalysis;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import sentimentanalysis.TweetHandler;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author holliewilson
 */
public class Main {
    
    public static void main(String[] args) throws IOException {
        
        Scanner intScanner = new Scanner(System.in);
        Scanner stringScanner = new Scanner(System.in);
        TweetHandler t = new TweetHandler();
        List<AbstractTweet> newTweets = new ArrayList<AbstractTweet>();
        boolean runProgram = true;
        t.loadSerialDB();
        
        if(t.database.size() == 0) {
            System.out.println("No tweets in database.");
        }
        else { 
            System.out.println("TARGET\tID\tDATE\t\t\t\tFLAG\t\t\tUSER\t\t\tTEXT");
            for(AbstractTweet tweet: t.database) {
                System.out.println(tweet.getTarget() + "\t" + tweet.getId() + "\t" + tweet.getDate() + "\t" + tweet.getFlag() + "\t\t\t" + tweet.getUser() + "\t\t\t" + tweet.getText());
                System.out.println();
                System.out.println("Predicted Polarity: " + tweet.getPredictedPolarity());
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
        
        while(runProgram) {
            System.out.println("Tweet Sentiment Analyzer Options: ");
            System.out.println("Enter 0 to exit program and save all tweets.");
            System.out.println("Enter 1 to load new tweet text file.");
            System.out.println("Enter 2 to classify tweets using NLP library and report accuracy.");
            System.out.println("Enter 3 to manually change tweet class label.");
            System.out.println("Enter 4 to add new tweets to database.");
            System.out.println("Enter 5 to delete tweet from database.");
            System.out.println("Enter 6 to search tweets by user, date, flag, or a matching substring.");
            
            int option = intScanner.nextInt();
           
            if(option == 0) {
               t.saveSerialDB();
               System.out.println("Saving changes... Exiting Program.");
               runProgram = false;
            }
            
            else if(option == 1) {
                System.out.println("Enter file path for tweet file: ");
                String tweetFileName = stringScanner.nextLine(); 
                newTweets = t.loadTweetsFromText(tweetFileName);
            }
            
            else if(option == 2) {
                double correct = 0;
                double total = t.database.size();
                
                if(total == 0) {
                    System.out.println("There are no tweets in the database. Choose option 4 to add tweets.");
                }
                else {
                    for(AbstractTweet tweet: t.database) {
                        int target = tweet.getTarget();
                        int prediction = t.classifyTweet(tweet);
                        tweet.setPredictedPolarity(prediction);
                        if(target == prediction){
                            correct++;
                        } 
                    }
                    double accuracy = correct/total * 100;
                    System.out.printf("Accuracy: %.2f%c \n", accuracy, '%');
                } 
            }
            
            else if(option == 3) {
                System.out.println("Enter Tweet ID: ");
                int id = intScanner.nextInt();
                
                System.out.println("Enter new label: ");
                int label = intScanner.nextInt();
                
                for(AbstractTweet tweet: t.database) {
                    if(tweet.getId() == id){
                        tweet.setPredictedPolarity(label);
                    }
                }
            }
             
            else if(option == 4) {
                t.addTweetsToDB(newTweets);
                System.out.println("Tweets added to database.");
            }
            
            else if(option == 5) {
                System.out.println("Enter ID of tweet you would like to delete: ");
                int id = intScanner.nextInt();
                t.deleteTweet(id);    
            }
        
            else {
                System.out.println("What would you like to search by?");
                System.out.println("Enter 1 for user.");
                System.out.println("Enter 2 for date.");
                System.out.println("Enter 3 for flag.");
                System.out.println("Enter 4 for substring.");
                
                List<AbstractTweet> searchResults = new ArrayList<AbstractTweet>();
                int selection = intScanner.nextInt();
                switch(selection){
                    case 1: 
                        System.out.println("Enter the user you would like to search for: ");
                        String user = stringScanner.nextLine();
                        searchResults = t.searchByUser(user);
                        break;
                    case 2: 
                        System.out.println("Enter the date you would like to search for: ");
                        String dateInp = stringScanner.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                        Date date = null;
                        try {
                            date = sdf.parse(dateInp);
                        }catch(ParseException p){
                            System.out.println("Error in parsing date.");
                        }
                        searchResults = t.searchByDate(date);
                        break;
                    case 3:
                        System.out.println("Enter the flag you would like to search for: ");
                        String flag = stringScanner.nextLine();
                        searchResults = t.searchByFlag(flag);
                        break;
                    case 4:
                        System.out.println("Enter the substring you would like to search for: ");
                        String substring = stringScanner.nextLine();
                        searchResults = t.searchBySubstring(substring);
                        break;        
                }
     
                if(searchResults.size() == 0) {
                    System.out.println("There are no tweets that contain what you have searched.");
                    System.out.println();
                }
                else {
                    System.out.println("TARGET\tID\tDATE\t\t\t\tFLAG\t\t\tUSER\t\t\tTEXT");
                    for(AbstractTweet tweet: searchResults) {
                    System.out.println(tweet.getTarget() + "\t" + tweet.getId() + "\t" + tweet.getDate() + "\t" + tweet.getFlag() + "\t\t\t" + tweet.getUser() + "\t\t\t" + tweet.getText());   
                    System.out.println();
                    System.out.println("Predicted Polarity: " + tweet.getPredictedPolarity());
                    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    }
                }        
            }    
        }
        intScanner.close();
        stringScanner.close(); 
    }
}
