package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The tokenizer component of the pipeline is responsible for intaking a file full of parsed HTML data.
 * The tokenizer then outputs a set of sentence /
 */
public class Tokenizer {

    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    public static final String SPLIT_PUNCTUATION = "[\\p{Punct}\\s-−™·]+";

    /**
     * Tokenize a set of data held inside of a file. This function will intake a file, and write
     * a new file called (data.name)_tokenized.txt.
     * @param data The initial datafile.
     */
    public static void TokenizeData(File data) throws IOException {
        File file = data;
        File[] directoryListing = data.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                Tokenize(child);
            }
        } else {
            Tokenize(file);

        }
    }

    public static void Tokenize(File file) throws IOException {
        /* Read in the data file */
        String fileBuffer = "";
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            fileBuffer += scanner.nextLine() + "\n";
        }

        /* Start the tokenization pipeline */
        /* First tokenize into an array of tokens */
        ArrayList<Character> splits = new ArrayList<>();
        splits.add('\n');
        splits.add(' ');
        ArrayList<String> split1 = TokenizationUtilities.ParseTokens(fileBuffer, splits);
        ArrayList<String> split2 = TokenizationUtilities.SplitTokens(split1, String.format(WITH_DELIMITER, SPLIT_PUNCTUATION));
        ArrayList<String> tokens = TokenizationUtilities.MergeTags(split2);

        /* Second Stage of the Pipeline: Sentence Splitting */
        ArrayList<String> split3 = SplittingUtilities.SplitSentences(tokens);
        String splitTokens = SplittingUtilities.formatTokenList(split3);
        System.out.println(splitTokens);

        /* Final Stage: Write output file */
        FileWriter fw = new FileWriter("../Data/Tokenized/" + file.getName().substring(0, file.getName().indexOf('.')) + "_tokenized.txt");
        fw.write(splitTokens);
        fw.close();
    }

    public static String Tokenize(String input){
        /* First tokenize into an array of tokens */
        ArrayList<Character> splits = new ArrayList<>();
        splits.add('\n');
        splits.add(' ');
        ArrayList<String> split1 = TokenizationUtilities.ParseTokens(input, splits);
        ArrayList<String> split2 = TokenizationUtilities.SplitTokens(split1, String.format(WITH_DELIMITER, SPLIT_PUNCTUATION));
        ArrayList<String> tokens = TokenizationUtilities.MergeTags(split2);

        /* Second Stage of the Pipeline: Sentence Splitting */
        ArrayList<String> split3 = SplittingUtilities.SplitSentences(tokens);
        String splitTokens = SplittingUtilities.formatTokenList(split3);

        return splitTokens;
    }
}
