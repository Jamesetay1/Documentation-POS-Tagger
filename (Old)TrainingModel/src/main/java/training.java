
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.trees.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import edu.stanford.nlp.util.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.xpath.operations.String;

/**
 * Hello world!
 *
 */
public class training
{
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    private static void trainAndWrite(String modelOutPath, String prop, String trainingFilepath) {
        Properties props = StringUtils.propFileToProperties(prop);
        props.setProperty("serializeTo", modelOutPath);
        //if input use that, else use from properties file.
        if (trainingFilepath != null) {
            props.setProperty("trainFile", trainingFilepath);
        }
        SeqClassifierFlags flags = new SeqClassifierFlags(props);
        CRFClassifier<CoreLabel> crf = new CRFClassifier<>(flags);
        crf.train();
        crf.serializeClassifier(modelOutPath);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BasicConfigurator.configure();
       // trainAndWrite("src/main/resources/out", "src/main/resources/properties","src/main/resources/training");
        CRFClassifier model = CRFClassifier.getClassifier("src/main/resources/out");
        doTagging(model,readFile("src/main/resources/test",Charset.defaultCharset()));
    }
    public static void doTagging(CRFClassifier model, String input) {
        input = input.trim();
        System.out.println(input + "=>"  +  model.classifyWithInlineXML(input));

    }
}
