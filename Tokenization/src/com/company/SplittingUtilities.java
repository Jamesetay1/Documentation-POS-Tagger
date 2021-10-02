package com.company;

import java.util.*;

public class SplittingUtilities {
    public static ArrayList<String> SplitSentences(ArrayList tokensList){

        ListIterator<String> tokensIter = tokensList.listIterator();
        String prev = "!PREV!";
        String cur = "!CUR!";
        boolean inCode = false;
        while (tokensIter.hasNext()){

            //This is kinda dumb but I have to go back and forth somewhere.
            String next = tokensIter.next();
            tokensIter.previous();

            if(cur.equals("<code>")){
                inCode = true;
            }
            if(cur.equals("</code>")){
                inCode = false;
            }
            if(next.matches("<p>")){
                tokensIter.add("!SPLIT!");
            }
            //Check for two new lines in a row, indicating a break
            else if(cur.equals("\n")){
                if(next.equals("\n")) {
                    tokensIter.add("!SPLIT!");
                }
            }
            //Check for .?! followed by a String starting with a capitol
            else if(!inCode && cur.matches("[.?!]")){
                if(Character.isUpperCase(next.charAt(0))){
                    tokensIter.add("!SPLIT!");
                }
            }

            cur = next;
            tokensIter.next();
        }

        return tokensList;
    }

    public static String formatTokenList(List<String> tokensList) {
        String formattedString = "";
        ListIterator<String> tokensIter = tokensList.listIterator();
        String prev = "!PREV!";

        while (tokensIter.hasNext()){
            String token = tokensIter.next();
            if (token.equals("\n")){
                tokensIter.remove();
            }
            else if (token == "!SPLIT!"){
                formattedString += "\n";
            }
            else {
                formattedString += token + " ";
            }

            prev = token;
        }

        //Cleaning up some outlier stuff
        formattedString = formattedString.replaceAll("\n+", "\n");

        return formattedString;
    }

}
