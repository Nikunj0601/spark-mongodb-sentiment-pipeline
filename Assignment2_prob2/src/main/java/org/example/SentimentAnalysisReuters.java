package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentimentAnalysisReuters {

    DatabaseOperations sentimentAnalysisDatabaseHandler = new DatabaseOperations();
    FileReaders fileReaders = new FileReaders();
    public HashMap<String, Integer> bagOfWords(String title) {
        HashMap<String, Integer> bagOfWords = new HashMap<>();
        String[] words = title.toLowerCase().split(" ");

        for (String word : words) {
            if (bagOfWords.containsKey(word)) {
                bagOfWords.put(word, bagOfWords.get(word) + 1);
            } else {
                bagOfWords.put(word, 1);
            }
        }
        System.out.println(bagOfWords);
        return bagOfWords;
    }
    public void sentimentAnalysis() {
        ArrayList<String> positiveWords = fileReaders.readPositiveWords();
        ArrayList<String> negativeWords = fileReaders.readNegativeWords();
        String data = fileReaders.readReuterFiles();
        String[] reutersData = data.split("<REUTERS");
        int newsCount = 0;
        for(String reuter : reutersData) {
            int score = 0;
            String polarity = "";
            StringBuilder matchedWords = new StringBuilder();
            Pattern titlePattern = Pattern.compile("<TITLE>(.*?)</TITLE>");

            Matcher titleMatcher = titlePattern.matcher(reuter);
            String titleText = titleMatcher.find() ? titleMatcher.group(1).trim() : "";
            if(titleText.isEmpty()) {
                continue;
            }

            HashMap<String, Integer> bagOfWords = bagOfWords(titleText);
            for(String word: bagOfWords.keySet()) {
                if (positiveWords.contains(word)) {
                    score = score + bagOfWords.get(word);
                    matchedWords.append(word).append(", ");
                }
                if (negativeWords.contains(word)) {
                    score = score - bagOfWords.get(word);
                    matchedWords.append(word).append(", ");
                }
            }

            if(score > 0) {
                polarity = "positive";
            } else if (score < 0) {
                polarity = "negative";
            }
            else {
                polarity = "neutral";
            }
            if (!polarity.equals("neutral")) {
                matchedWords.delete(matchedWords.length() - 2, matchedWords.length() - 1);
            }
            newsCount++;
            sentimentAnalysisDatabaseHandler.insertData(newsCount, titleText, matchedWords.toString(), score, polarity);
        }
    }
}
