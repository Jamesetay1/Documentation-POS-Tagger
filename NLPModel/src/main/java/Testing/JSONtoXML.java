package Testing;

import org.json.*;

public class JSONtoXML {

    public static String ConvertJSONtoXML(String jsonFile){
        JSONObject file = new JSONObject(jsonFile);
        assert file.getString("type").equals("autotagged");

        JSONArray tokens = file.getJSONArray("tokens");

        String ret = "";

        for(int i = 0; i < tokens.length(); i++){
            JSONObject obj = tokens.getJSONObject(i);
            ret += obj.getString("token") + " ";
        }

        return ret.trim();
    }
}
