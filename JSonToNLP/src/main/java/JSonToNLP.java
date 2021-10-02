import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
public class JSonToNLP {
    /**
     * Converts a JSon file into the correct format to be used by the NLP, the isInResources boolean should be false
     * unless if the json is located in a maven resources folder, as that requires special code.
     * @param fileIn
     * @param fileOut
     * @param isInResources
     */
    public static void convert(String fileIn,String fileOut,boolean isInResources){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File in;
        if(isInResources)
        {
            JSonToNLP test = new JSonToNLP();
            in = test.getResourceFile(fileIn);
        }
        else
        {
            in = new File(fileIn);
        }
        Carrier carrier;
        try {
            carrier = objectMapper.readValue(in,Carrier.class);
            FileWriter outWriter = new FileWriter(fileOut);
            for(int i = 0; i < carrier.getTokens().length; i++)
            {
                outWriter.write(carrier.getTokens()[i].getToken() + " " + carrier.getTokens()[i].getTag() + '\n');
            }
            outWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * This code runs if the specific file is in the resource folder
     * @param fileName
     * @return
     */
    private File getResourceFile(String fileName)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file;
    }

}
