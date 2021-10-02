package Testing;

import org.json.*;

import java.util.Scanner;

/**
 * Convert an NLP formatted document (TSV) to
 * a JSON formatted document.
 */
public class NLPtoJSON {

    /**
     * Convert the provided string from NLP format
     * to JSON format (also in string form).
     * @param nlpDocument
     * @return
     */
    public static String ConvertString(String nlpDocument){
        Scanner scanner = new Scanner(nlpDocument);

        JSONArray array = new JSONArray();

        while(scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("\\\\t"); //MUST BE TAB DELIMITED!
            assert line.length == 2;

            JSONObject line_obj = new JSONObject();
            line_obj.put("tag", line[1]);
            line_obj.put("token", line[0]);
            array.put(line_obj);
        }

        JSONObject output = new JSONObject();
        output.put("tokens", array);
        output.put("type", "nlp-converted");

        return output.toString();
    }
}
