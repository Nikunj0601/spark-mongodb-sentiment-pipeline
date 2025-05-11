package org.example;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDatabase {
    MongoClient mongoClient = new MongoClient("localhost", 27017);
    com.mongodb.client.MongoDatabase database = mongoClient.getDatabase("ReuterDb");
    MongoCollection<Document> collection = database.getCollection("reuter");

    public void addDataToMongoDB(String title, String body) {
        Document document = new Document();
        document.append("title", title);
        document.append("body", body);
        collection.insertOne(document);
    }
}
