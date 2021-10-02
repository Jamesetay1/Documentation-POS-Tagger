package Testing;

import org.json.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * A static class used to compare two JSON files.
 */
public class JSONCompare {


    /**
     * Test all of the results in a given directory against the correct directory's results.
     * NOTE: The files must be identically named between the two directories and in JSON format.
     * @param correctDirectory The directory of manually tagged files (correct tags).
     * @param taggedDirectory The directory of model tagged files to be tested.
     * @return A test results object with the results of the test.
     */
    public static TestResults doTest(String correctDirectory, String taggedDirectory){
        /* Open both directories */
        File correctDir = new File(correctDirectory);
        File taggedDir = new File(taggedDirectory);
        if(!correctDir.isDirectory()) throw new IllegalArgumentException(correctDirectory + " is not a path to a directory.");
        if(!taggedDir.isDirectory()) throw new IllegalArgumentException(taggedDirectory + " is not a path to a directory.");

        /* Get a listing for all files in the correctDirectory */
        File[] correctFiles = correctDir.listFiles();

        /* Iterate through the tagged directory and do lookups on the correct directory as necessary */
        TestResults sumResults = new TestResults();
        for(File file : taggedDir.listFiles()){
            if(file.isDirectory()) continue; //don't need to go deeper, change if needed

            /* Attempt to open the corresponding file */
            File correct = getFile(correctFiles, file.getName());
            if(correct == null){
                throw new IllegalStateException("No correct file to compare " + file.getName() + " to found. Make sure this file has a parallel entry in " + correctDirectory);
            }

            /* Compare the two files */
            String taggedFileData = "";
            String correctFileData = "";
            try {
                taggedFileData = Files.readString(file.toPath());
                correctFileData = Files.readString(correct.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to read file data.");
            }

            sumResults = sumResults.mergeResults(compareJSON(correctFileData, taggedFileData));
        }

        /* Finally, return the processed result */
        return sumResults;
    }

    /**
     * Test two JSON files for equality. FOR THIS, ORIGINAL IS THE CORRECT VERSION. TAGGED IS THE MODEL'S OUTPUT.
     * @param original The correct output json file.
     * @param tagged The model tagged output json file to be tested.
     * @return A <code>TestResults</code> object with test results.
     */
    private static TestResults compareJSON(String original, String tagged){
        /* Get a list of JSON objects from the strings */
        JSONObject originalJSON = new JSONObject(original);
        JSONObject taggedJSON = new JSONObject(tagged);

        ArrayList<JSONObject> originalObjs = convertArray(originalJSON.getJSONArray("tokens"));
        ArrayList<JSONObject> taggedObjs = convertArray(taggedJSON.getJSONArray("tokens"));

        if(originalObjs.size() != taggedObjs.size()) throw new IllegalStateException("The two files are not the same length.");

        /* Generate a test results object */
        TestResults results = new TestResults();

        /* Compare the two files and tabulate results */
        for(int i = 0; i < originalObjs.size(); i++){
            JSONObject originalObj = originalObjs.get(i);
            JSONObject taggedObj = taggedObjs.get(i);

            /* If the tokens do not match, something is not lined up correctly */
            if(!originalObj.getString("token").equals(taggedObj.getString("token"))){
                throw new IllegalStateException("Tokens do not match up. " + originalObj.getString("token") + " is not equivalent to " + taggedObj.getString("token"));
            }
            else {
                /* Update the results JSON */
                results.numTags += 1;
                try{
                    String actualTag = originalObj.getString("tag");
                    String taggedTag = taggedObj.getString("tag");

                    if(actualTag.equals(taggedTag)){
                        results.passedTags.add(taggedObj);
                    } else{
                        taggedObj.put("actualTag", actualTag);
                        results.failedTags.add(taggedObj);
                        results.numFailiures += 1;
                    }
                }
                catch(JSONException ex){
                    results.passedTags.add(taggedObj);
                }

            }
        }

        /* Return the test results */
        return results;
    }

    /**
     * Convert a JSONarray into an arraylist of JSON objects.
     * @param array The array to be converted.
     * @return A converted array.
     */
    private static ArrayList<JSONObject> convertArray(JSONArray array){
        ArrayList<JSONObject> objs = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            objs.add(array.getJSONObject(i));
        }

        return objs;
    }

    /**
     * Get a file from a list of files by name.
     * @param files
     * @param name
     * @return
     */
    private static File getFile(File[] files, String name){
        for(File f : files){
            if(f.getName().equals(name)) return f;
        }

        return null;
    }
}
