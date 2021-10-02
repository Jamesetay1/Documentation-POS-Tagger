package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println(Tokenizer.Tokenize("boolean"));

        System.out.println(System.getProperty("user.dir"));
        File input = new File("src/com/company/InputFile.txt");
//        File input = new File("Data/Parsed_HTML/Parsed_LeetCode/3sum.html");
//        File input = new File("../Data/Parsed_HTML/Parsed_JavaDocs");
        try {
            Tokenizer.TokenizeData(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

