package Training;

import JSonToNLP.JSonToNLP;
import API.NLPAPI;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TrainModel {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        train("C:\\Users\\aalra\\Documents\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\TrainingData",
                "C:\\Users\\aalra\\Documents\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\ModelData\\Model(Ahmad)4-4-2021",
                "C:\\Users\\aalra\\Documents\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\UsedData\\Model(Ahmad)4-4-2021",
                "C:\\Users\\aalra\\Documents\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\temp");
    }

    /**
     * Takes in 4 arguments, the input directory that contains .JSON files, the model directory which contains the model, the output directory that
     * @param inputDir
     * @param modelDir
     * @param outputDir
     * @param tempDir
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void train(String inputDir,String modelDir,String outputDir, String tempDir) throws IOException, ClassNotFoundException {

        BasicConfigurator.configure();


        if(inputDir ==null|| modelDir ==null || outputDir ==null || tempDir == null )
        {
            throw new IllegalArgumentException("Incomplete arguments");
        }
        File inputFolder = new File(inputDir); 
        File[] listOfFiles = inputFolder.listFiles();
        File temporaryDir = new File(tempDir);
        temporaryDir.mkdir();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                JSonToNLP.convert(file.getPath(),tempDir + "\\" +  file.getName().substring(0, file.getName().indexOf(".")) + ".tsv",false);
            }
        }
        File[] listOfTempFiles = temporaryDir.listFiles();
        boolean modelExists = new File(modelDir).isFile();
        CRFClassifier model;
        if(modelExists)
        {
            model = CRFClassifier.getClassifier(modelDir);

            for (File file : listOfTempFiles) {
                if (file.isFile()) {
                    model.train(file.getPath());
                }
            }
        }
        else
        {
            NLPAPI.buildModel(modelDir,"src/main/resources/properties", listOfTempFiles[0].getPath());
            model = CRFClassifier.getClassifier(modelDir);
            for(int i = 1; i < listOfTempFiles.length; i++)
            {
                model.train(listOfTempFiles[i].getPath());
            }

        }
        FileUtils.deleteDirectory(temporaryDir);
        for (File file : listOfFiles) {
            if (file.isFile()) {
               Files.move(Paths.get(file.getPath()),Paths.get(outputDir + "\\" +  file.getName()));
            }
        }
    }

}
