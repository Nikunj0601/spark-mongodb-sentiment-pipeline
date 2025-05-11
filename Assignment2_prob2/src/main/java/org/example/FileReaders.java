package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileReaders {

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
    public ArrayList<String> readPositiveWords() {
        String reuterData;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Reuters/positive-words.txt"));
            while (true) {
                if(reader.readLine().isEmpty()){
                    break;
                }
            }
            reuterData = reader.lines().collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(Arrays.asList(reuterData.split(" ")));
    }

    public ArrayList<String> readNegativeWords() {
        String reuterData;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Reuters/negative-words.txt"));
            while (true) {
                if(reader.readLine().isEmpty()){
                    break;
                }
            }
            reuterData = reader.lines().collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(Arrays.asList(reuterData.split(" ")));
    }
}
