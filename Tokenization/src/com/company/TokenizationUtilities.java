package com.company;

import java.util.ArrayList;

public class TokenizationUtilities {
    private static final String[][] SUBSTITUTIONS = new String[][] {
        new String[] {"cannot", "can not"},
        new String[] {"\n", ""}
    };

    /**
     * Break apart a string of text from a file into a list of tokens for
     * more processing.
     * @param fullText The full text from a document.
     * @return an arraylist of partially split tokens (strings)
     */
    public static ArrayList<String> ParseTokens(String fullText, ArrayList<Character> splitChars){
        String substituted = fullText;
        for (var substitution : SUBSTITUTIONS) {
            substituted = substituted.replaceAll(substitution[0], substitution[1]);
        }

        ArrayList<String> tokens = new ArrayList<>();
        int lastIndex = 0;

        for(int i = 0; i < substituted.length(); i++){
            char curr = substituted.charAt(i);

            /* Are we splitting? */
            if(splitChars.contains(curr)){
                /* We just ended a token. Make a new token */
                if(i != lastIndex){
                    tokens.add(substituted.substring(lastIndex, i));
                }
                if(curr != ' ') tokens.add("" + curr);
                lastIndex = i + 1;
            }
        }

        /* Finally, add the last token and remove all spaces*/
        if(lastIndex != substituted.length()) tokens.add(substituted.substring(lastIndex));

        return tokens;
    }

    public static boolean isBlank(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c) || c == '\u200B') {
                return true;
            }
        }

        return false;
    }

    /**
     * Split a list of whitespace / standalone punctuation delimited tokens
     * into fully split tokens (including incorrectly split HTML tags.
     * @param tokens The tokens to process.
     * @param regex The REGEX used to determine splittable characters.
     * @return A list of fully split tokens.
     */
    public static ArrayList<String> SplitTokens(ArrayList<String> tokens, String regex){
        ArrayList<String> splitTokens = new ArrayList<>();

        for(String token : tokens){
            String[] split = token.split(regex);
            for(String splitToken : split){
                if (!isBlank(splitToken)) {
                    splitTokens.add(splitToken);
                }
            }
        }

        return splitTokens;
    }

    /**
     * Merge the HTML tags back together after doing a regex punctuation split.
     * @param splitTokens The split tokens.
     * @return A finalized list of tokens.
     */
    public static ArrayList<String> MergeTags(ArrayList<String> splitTokens){
        ArrayList<String> mergedTokens = new ArrayList<>();

        for(int i = 0; i < splitTokens.size(); i++){
            if(splitTokens.get(i).equals("<")){
                if(i + 2 < splitTokens.size() && splitTokens.get(i + 2).equals(">")){
                    /* We are in a start tag */
                    mergedTokens.add(splitTokens.get(i) + splitTokens.get(i+1) + splitTokens.get(i+2));
                    i += 2;
                }
                else if(i + 3 < splitTokens.size() && splitTokens.get(i + 1).equals("/") && splitTokens.get(i + 3).equals(">")){
                    /* We are in an end tag */
                    mergedTokens.add(splitTokens.get(i) + splitTokens.get(i+1) + splitTokens.get(i+2) + splitTokens.get(i+3));
                    i += 3;
                }
                else{
                    /* This was not a tag. Continue */
                    mergedTokens.add(splitTokens.get(i));
                }
            } else {
                /* This was not any sort of tag. Move on */
                mergedTokens.add(splitTokens.get(i));
            }
        }

        /* Final Step: Remove any tags without content */
        ArrayList<String> finalMerge = new ArrayList<>();

        for(int i = 0; i < mergedTokens.size(); i++){
            if(i + 1 < mergedTokens.size() && mergedTokens.get(i).charAt(0) == '<' && mergedTokens.get(i + 1).contains("</")){
                /* We don't want these tokens. Remove them */
                i += 1;
            } else{
                finalMerge.add(mergedTokens.get(i));
            }
        }

        return finalMerge;
    }
}
