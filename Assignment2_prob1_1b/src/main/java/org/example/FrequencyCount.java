package org.example;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrequencyCount {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
            "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
            "their", "then", "there", "these", "they", "this", "to", "was", "will", "with"
    ));

    public static void main(String[] args) {

        String reuterFile = "./reut2-009.sgm";

        try {
            SparkSession spark = SparkSession.builder().appName("FrequencyCount").getOrCreate();
            JavaSparkContext javaSparkContext = new JavaSparkContext(spark.sparkContext());

            Path filePath = Paths.get(reuterFile);
            StringBuilder content = new StringBuilder();
            Files.lines(filePath, StandardCharsets.UTF_8).forEach(
                    line -> content.append(line).append("\n"));
            String inputFile = content.toString();

            String transformedText = inputFile
                    .replaceAll("&lt;", "")
                    .replaceAll("[^a-zA-Z ]", "")
                    .replaceAll("\\b\\w\\b", "")
                    .replaceAll("\\s+", " ")
                    .trim()
                    .toLowerCase();

            JavaRDD<String> lines = javaSparkContext.parallelize(Arrays.asList(transformedText.split(" ")));

            JavaPairRDD<String, Integer> wordCounts = lines
                    .flatMapToPair(line -> Arrays.asList(line.split(" ")).stream()
                            .filter(word -> !isStopWord(word))
                            .map(word -> new Tuple2<>(word, 1))
                            .iterator())
                    .reduceByKey(Integer::sum);

            JavaPairRDD<String, Integer> singleCountWords = wordCounts.filter(pair -> pair._2() == 1);
            List<Tuple2<String, Integer>> singleCountResults = singleCountWords.collect();
            System.out.println("\nUnique Words in the reuter file given is :");

            long uniqueWordsCount = singleCountResults.size();
            System.out.println("Number of Unique Words: " + uniqueWordsCount);

            String wordCountsOutputFilePath = args[0];
            wordCounts.coalesce(1).saveAsTextFile(wordCountsOutputFilePath);

            String uniqueWordsOutputFilePath = args[1];
            singleCountWords.keys().coalesce(1).saveAsTextFile(uniqueWordsOutputFilePath);

            System.out.println("Unique words with frequency 1 is saved in the file: " + uniqueWordsOutputFilePath);
            System.out.println("Words frequency count is saved in the file: " + wordCountsOutputFilePath);
            Tuple2<String, Integer> highestFrequency = wordCounts.reduce((t1, t2) -> t1._2() > t2._2() ? t1 : t2);
            Tuple2<String, Integer> lowestFrequency = wordCounts.reduce((t1, t2) -> t1._2() < t2._2() ? t1 : t2);

            System.out.println("Highest Frequency Word: " + highestFrequency._1() + ", Count: " + highestFrequency._2());
            System.out.println("Lowest Frequency Word: " + lowestFrequency._1() + ", Count: " + lowestFrequency._2());

            javaSparkContext.stop();
            spark.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isStopWord(String word) {
        return STOP_WORDS.contains(word.toLowerCase());
    }
}
