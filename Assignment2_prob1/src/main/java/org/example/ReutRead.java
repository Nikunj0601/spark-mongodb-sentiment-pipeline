package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ReutRead {

    MongoDatabase mongoDatabase = new MongoDatabase();
    public String readReuterFiles() {
        StringBuilder reuterData = new StringBuilder();
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("Reuters/reut2-009.sgm"));
            BufferedReader reader2 = new BufferedReader(new FileReader("Reuters/reut2-014.sgm"));
            reader1.readLine();
            reader2.readLine();
            reuterData.append(reader1.lines().collect(Collectors.joining()));
            reuterData.append(reader2.lines().collect(Collectors.joining()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reuterData.toString();
    }
    public void parseSGMData(String data) {
        String[] reutersData = data.split("<REUTERS");
        System.out.println(reutersData[1]);
        for(String reuter : reutersData) {
            Pattern titlePattern = Pattern.compile("<TITLE>(.*?)</TITLE>");
            Pattern bodyPattern = Pattern.compile("<BODY>(.*?)</BODY>");

            Matcher titleMatcher = titlePattern.matcher(reuter);
            String titleText = titleMatcher.find() ? titleMatcher.group(1).trim() : "";

            Matcher bodyMatcher = bodyPattern.matcher(reuter);
            String bodyText = bodyMatcher.find() ? bodyMatcher.group(1).trim() : "";
            if(titleText.isEmpty() && bodyText.isEmpty()) {
                continue;
            }
            // Add the data to MongoDB
            mongoDatabase.addDataToMongoDB(titleText, bodyText);
        }
    }

}
