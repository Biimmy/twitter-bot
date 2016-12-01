package c_schell.twitter_test;

import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterBase;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	//Markov chain init
    	Markov.markovChain.put("_start", new Vector<String>());
		Markov.markovChain.put("_end", new Vector<String>());
    	
		//Twitter settings
    	String consumerKey = "Consumer Key";
        String consumerSecret = "Comsumer Secret";
        String accessToken = "accessToken";
        String accessSecret = "accessSecret";
        //Target and empty tweet string that will be filled
        String target = "target twitter name";
        String tweet = "";

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessSecret);

        TwitterFactory factory = new TwitterFactory(cb.build());
        Twitter twitter = factory.getInstance();
        //runs loop w/ sleep to tweet every 60 minutes
        while (true)
        {
        	try 
        	{
        		//Pulls 200 tweets from user
        		Paging paging = new Paging(1, 200); 
        		System.out.println(twitter.getScreenName());
        		//Change name in the string below
        		List<Status> statuses = twitter.getUserTimeline(target,paging);
        		System.out.println("Showing home timeline.");
        		for (Status status : statuses) 
        		{
        			// Add the words to the hash table if no @
        			//Dont want to mention people with the bot
        			if(!status.getText().contains("@"))
        			{
        				if (status.getText().charAt(status.getText().length()-1) != '.')
        				{
        					Markov.addWords(status.getText() + ".");
        				}
        				else
        				{
        					Markov.addWords(status.getText());
        				}
        			}
               //System.out.println(status.getUser().getName() + ":" +
                 //                 status.getText());
        		}
        		tweet = Markov.generateSentence();
        		//make new sentance every time markov chain comes back with > 140 characters
        		while (tweet.length() > 139)
        		{
        			tweet = Markov.generateSentence();
        		}
        		//Update status to generated sentence
        		Status status = twitter.updateStatus(tweet);
        		System.out.println("Successfully updated the status to [" + status.getText() + "].");
        	}
        	catch (TwitterException te) 
        	{
        		te.printStackTrace();
        		System.exit(-1);
        	}
        	try
        	{
        		TimeUnit.MINUTES.sleep(60);
        	} 
        	catch (InterruptedException e) 
        	{
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        		return;
        	}
        }
    }
}