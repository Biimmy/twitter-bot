package c_schell.twitter_test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import twitter4j.TwitterException;


//Credit for following code: https://gist.github.com/veryphatic/3190969
//Temp code, want to make it a little more versatile in the future. ie have it search for an end character if getting close to 140 character

public class Markov {

	//Hashmap
	public static Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
	static Random rnd = new Random();
	
	
	/*
	 * Main constructor
	 */
	
	/*
	 * Add words
	 */
	public static void addWords(String phrase) {
		System.out.println("Phrase in: " + phrase);
		// put each word into an array
		String[] words = phrase.split(" ");
				
		// Loop through each word, check if it's already added
		// if its added, then get the suffix vector and add the word
		// if it hasn't been added then add the word to the list
		// if its the first or last word then select the _start / _end key
		try
		{
			for (int i=0; i<words.length; i++) 
			{
				System.out.println(words[i]);			
				// Add the start and end words to their own
				if (i == 0) 
				{
					Vector<String> startWords = markovChain.get("_start");
					startWords.add(words[i]);
				
					Vector<String> suffix = markovChain.get(words[i]);
					if (suffix == null) 
					{
						suffix = new Vector<String>();
						suffix.add(words[i+1]);
						markovChain.put(words[i], suffix);
					}
				} 
				else if (i == words.length-1) 
				{
					Vector<String> endWords = markovChain.get("_end");
					endWords.add(words[i]);
				} 
				else 
				{	
					Vector<String> suffix = markovChain.get(words[i]);
					if (suffix == null) 
					{
						suffix = new Vector<String>();
						suffix.add(words[i+1]);
						markovChain.put(words[i], suffix);
					} 
					else 
					{
						suffix.add(words[i+1]);
						markovChain.put(words[i], suffix);
					}
				}
			}		
		//generateSentence();
		}
		catch (ArrayIndexOutOfBoundsException exception)
		{
			return;	
		}
	}
	
	/*
	 * Generate a markov phrase
	 */
	public static String generateSentence() {
		
		// Vector to hold the phrase
		Vector<String> newPhrase = new Vector<String>();
		
		// String for the next word
		String nextWord = "";
		String phrase = "";
				
		// Select the first word
		Vector<String> startWords = markovChain.get("_start");
		int startWordsLen = startWords.size();
		nextWord = startWords.get(rnd.nextInt(startWordsLen));
		newPhrase.add(nextWord);
		
		// Keep looping through the words until we've reached the end
		while (nextWord.charAt(nextWord.length()-1) != '.') {
			Vector<String> wordSelection = markovChain.get(nextWord);
			System.out.println(wordSelection.size());
			int wordSelectionLen = wordSelection.size();
			nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
			newPhrase.add(nextWord);
		}
		
		for (int i = 0; i < newPhrase.size(); i++)
		{
			if (i < newPhrase.size())
			{
			phrase = phrase + newPhrase.get(i) + " ";
			}
			else
			{
				phrase = phrase + newPhrase.get(i) + ".";
			}
			
		}
		
		System.out.println("New phrase: " + phrase);
		return phrase;
	}
}