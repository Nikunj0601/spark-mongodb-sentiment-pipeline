package org.example;

public class Main {
    public static void main(String[] args) {
        ReutRead readReuters = new ReutRead();
        MongoDatabase mongoDatabase = new MongoDatabase();
        String data = readReuters.readReuterFiles();
        readReuters.parseSGMData(data);
        mongoDatabase.mongoClient.close();
        System.out.println("Program successfully executed!");
    }
}