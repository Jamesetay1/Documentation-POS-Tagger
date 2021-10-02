package Tagging;


import API.NLPAPI;
import Testing.JSONtoXML;
import Testing.XMLtoJSON;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import org.apache.log4j.BasicConfigurator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class TagDocs {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        tag("D:\\__SENIOR_DESIGN__\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\GradingData",
                "D:\\__SENIOR_DESIGN__\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\ModelData\\Model(Ahmad)4-4-2021",
                "D:\\__SENIOR_DESIGN__\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\TaggingOut"
                );



    }
    public static void tag(String inputDir,String modelDir,String outputDir) throws IOException, ClassNotFoundException {
        BasicConfigurator.configure();
        if (inputDir == null || modelDir == null || outputDir == null) {
            throw new IllegalArgumentException("Incomplete arguments");
        }
        File inputFolder = new File(inputDir);
        File[] listOfFiles = inputFolder.listFiles();
        boolean modelExists = new File(modelDir).isFile();
        CRFClassifier model;
        if (modelExists) {
            model = CRFClassifier.getClassifier(modelDir);

             for (File file : listOfFiles) {
                if (file.isFile()) {
                    String content = JSONtoXML.ConvertJSONtoXML(Files.readString(file.toPath()));
                    String tagged = XMLtoJSON.XMLtoJSON(NLPAPI.doTagging(model,content));
                    System.out.println(tagged);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + "\\" + file.getName().substring(0, file.getName().indexOf(".")) + ".json", true));
                    writer.append(tagged);
                    writer.close();
                }
            }
        }
    }
}
