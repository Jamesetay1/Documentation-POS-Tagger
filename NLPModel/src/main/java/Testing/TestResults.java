package Testing;

import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class used to contain all results for a given test.
 */
public class TestResults {

    public int numTags;
    public int numFailiures;

    public ArrayList<JSONObject> failedTags;
    public ArrayList<JSONObject> passedTags;

    public TestResults(){
        failedTags = new ArrayList<>();
        passedTags = new ArrayList<>();
    }

    public double getAccuracy(){
        if(numTags == 0) return 1.0; //100 percent if we haven't done anything
        return 1.0 - ((double)numFailiures / numTags);
    }

    public TestResults mergeResults(TestResults other){
        TestResults ret = new TestResults();

        ret.numTags = this.numTags + other.numTags;
        ret.numFailiures = this.numFailiures + other.numFailiures;

        ret.failedTags.addAll(other.failedTags);
        ret.failedTags.addAll(this.failedTags);
        ret.passedTags.addAll(other.passedTags);
        ret.passedTags.addAll(this.passedTags);

        return ret;
    }

    /**
     * Returns a sorted array of the missed tokens along with their number of occurences
     * @return
     */
    public ArrayList<Miss> GetMisses(){
        ArrayList<Miss> misses = new ArrayList<>();

        for(JSONObject failedTag : failedTags){
            /* If this is already contained, get a reference to it */
            Miss existing = null;
            for(Miss miss : misses){
                if(miss.token.equals(failedTag.getString("token"))){
                    existing = miss;
                    break;
                }
            }

            /* Update the number of misses */
            if(existing == null){
                existing = new Miss();
                existing.token = failedTag.getString("token");
                misses.add(existing);
            }

            /* Update the type of miss */
            if(existing.missTypes.containsKey(failedTag.getString("tag"))){
                existing.missTypes.put(failedTag.getString("tag"), existing.missTypes.get(failedTag.getString("tag")) + 1);
            }
            else{
                existing.missTypes.put(failedTag.getString("tag"), 1);
            }

            /* Update the actual miss */
            if(existing.actualVsTagged.containsKey(failedTag.getString("actualTag"))){
                existing.actualVsTagged.get(failedTag.getString("actualTag")).add(failedTag.getString("tag"));
            }
            else{
                existing.actualVsTagged.put(failedTag.getString("actualTag"), new ArrayList<>());
                existing.actualVsTagged.get(failedTag.getString("actualTag")).add(failedTag.getString("tag"));
            }

            existing.numMisses += 1;
        }


        /* Sort highest to lowest and return */
        misses.sort((o1, o2) -> o2.numMisses - o1.numMisses);
        return misses;
    }

    @Override
    public String toString(){
        double percent = getAccuracy() * 100.0;
        return "[RESULTS] Accuracy: " + String.format("%.2f%%",percent) + " Number of Missed Tags: " + numFailiures + " Total Tags: " + numTags;
    }

    public class Miss {

        public String token;
        public int numMisses;
        public HashMap<String, ArrayList<String>> actualVsTagged = new HashMap<>();
        public HashMap<String, Integer> missTypes = new HashMap<>();

        @Override
        public String toString(){
            return "[token: " + token + ", misses: " + numMisses + "]";
        }
    }
}
