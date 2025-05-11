# 📊 Spark-Based Text Processing and Sentiment Analysis Pipeline

This project implements a full data pipeline that processes Reuters news articles, stores them in MongoDB, performs frequency-based text analysis using Apache Spark, and applies sentiment analysis using a Bag of Words (BoW) model.

## 🧩 Project Overview

The pipeline consists of three main components:

1. **Data Cleaning and Storage**  
   - Extracts and transforms raw text data from `.sgm` files.
   - Stores cleaned title and body data into a MongoDB collection.

2. **Text Processing with Spark**  
   - Processes the text using Apache Spark to compute word frequencies.
   - Identifies and stores unique words (frequency = 1).

3. **Sentiment Analysis with BoW Model**  
   - Applies sentiment analysis on article titles.
   - Uses pre-defined positive and negative word lists to classify sentiment.
   - Results (title, score, matched words, and polarity) are stored in a database and exported as a CSV.

## 🛠️ Technologies Used

- Java  
- Apache Spark  
- MongoDB  
- Google Cloud Platform (Dataproc)  
- Regex for text preprocessing  
- Bag of Words (BoW) sentiment analysis

## 🚀 How to Run

### Prerequisites
- Java 8+
- Apache Spark
- MongoDB
- Google Cloud SDK (if running on GCP)

### Steps

1. **Data Extraction & Storage**
   - Run the Java program that reads and parses `.sgm` files.
   - Clean and store titles and bodies in MongoDB (`ReuterDb.reuter` collection).

2. **Spark Job**
   - Package the Spark job into a JAR.
   - Submit it to a Dataproc cluster or local Spark setup.
   - Outputs:
     - Word frequency file
     - Unique word file (frequency = 1)

3. **Sentiment Analysis**
   - Extract titles from MongoDB.
   - Create BoW vectors for each title.
   - Match against positive/negative word lists.
   - Store and export results (news ID, title, matched words, sentiment score, and polarity).

## 📁 Project Structure

```
.
├── data/                      # Input .sgm files and word lists
├── scripts/                   # Java code for parsing and processing
├── spark_jobs/                # Spark frequency count job
├── sentiment_analysis/       # BoW-based sentiment logic
├── output/                    # Result CSVs and frequency files
└── README.md
```

## 📌 Example Output

- **Most Frequent Word:** `said` (2473 times)
- **Least Frequent Words:** Words occurring once (e.g., "asinterseted")
- **Sentiment Report:** Titles classified as Positive, Negative, or Neutral

## 📚 References

- [MongoDB Java Driver Docs](https://www.mongodb.com/docs/drivers/java/sync/)
- [Apache Spark Examples](https://spark.apache.org/examples.html)
- [Positive Words List](https://gist.github.com/mkulakowski2/4289437)
- [Negative Words List](https://gist.github.com/mkulakowski2/4289441)
- [Stop Words](https://algs4.cs.princeton.edu/35applications/stopwords.txt)
