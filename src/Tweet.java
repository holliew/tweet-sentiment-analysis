/*
 * Hollie Wilson
 * CS 3354 
 * Assignment 2
 */
package sentimentanalysis;

import java.util.Date;

/**
 *
 * @author holliewilson
 */
public class Tweet extends AbstractTweet {
    public Tweet(int target, int id, Date date, String flag, String user, String text) {
        super(target, id, date, flag, user, text);
    }
}
