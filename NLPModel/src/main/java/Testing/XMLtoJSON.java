package Testing;

import org.json.*;

import java.util.Scanner;

public class XMLtoJSON {

    public static String XMLtoJSON(String xml){
//        System.out.println(xml);
        JSONObject output = new JSONObject();
        output.put("type", "xml_converted");

        JSONArray array = new JSONArray();
        Scanner scanner = new Scanner(xml);

        while(scanner.hasNext()){
            String token = scanner.next();
//            System.out.println(token);
            JSONObject object = new JSONObject();

            String tag;
            if(token.equals("<code>")){
                tag = "<code>";
                token = "<code>";
            }
            else if(token.equals("</code>")){
                tag = "<code>";
                token = "</code>";
            }
            else{
                /* If there is a start or end tag, tag accordingly */
                /* If there is no tag, use the previous tag */
                if(token.charAt(0) == '<'){
                    /* We have a start tag */
                    tag = token.substring(1, token.indexOf(">"));
                    if(token.charAt(token.indexOf(">") + 1) == '>'){
                        tag += ">";
                        token = token.substring(token.indexOf(">") + 2);
                    }
                    else{
                        token = token.substring(token.indexOf(">") + 1);
                    }

                    if(token.charAt(token.length() - 1) == '>') token = token.substring(0, token.indexOf('<'));
                }
                else if(token.charAt(token.length() - 1) == '>'){
                    /* We have an end tag */
                    tag = token.substring(token.indexOf("<") + 1, token.length() - 1);
                    token = token.substring(0, token.indexOf("<"));
                }
                else{
                    /* We have no tag */
                    tag = array.getJSONObject(array.length() - 1).getString("tag");
                }
            }

            /* Generate and store an object */
            object.put("token", token);
            object.put("tag", tag);
//            System.out.println("token: " + token + " tag: " + tag);
            array.put(object);
        }

        output.put("tokens", array);
        return output.toString();
    }
}
