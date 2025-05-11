package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseOperations {
    private static final String LOCAL_DATABASE_URL = "jdbc:mysql://localhost:3306/sentiment_analysis";
    private static final String LOCAL_USERNAME = "root";
    private static final String LOCAL_PASSWORD = "password";

    public void insertData(int news, String titleContent, String matchedWords, int score, String polarity) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(LOCAL_DATABASE_URL, LOCAL_USERNAME, LOCAL_PASSWORD);

            String query = "INSERT INTO sentiment_result (news, title_content, matched_words, score, polarity) values (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, news);
            preparedStatement.setString(2, titleContent);
            preparedStatement.setString(3, matchedWords);
            preparedStatement.setInt(4, score);
            preparedStatement.setString(5, polarity);

            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
