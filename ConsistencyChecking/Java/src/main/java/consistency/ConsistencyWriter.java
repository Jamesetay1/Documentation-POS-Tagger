package consistency;

import com.opencsv.CSVWriter;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConsistencyWriter {
	private static final String OUTPUT_FILE = "java-output.csv";

	private static final Properties POS_PROPERTIES;
	static {
		POS_PROPERTIES = new Properties();
		POS_PROPERTIES.put("annotators", "tokenize, ssplit, pos");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java ConsistencyWriter [sentenceFile]");
			System.exit(-1);
		}

		List<String> sentenceFileLines = loadSentenceFile(args[0]);

		List<List<POSTag>> SentencePOSTags = sentenceFileLines.stream().map(sentence -> {
			CoreDocument sentenceDocument = new CoreDocument(sentence);

			StanfordCoreNLP pipeline = new StanfordCoreNLP(POS_PROPERTIES);
			pipeline.annotate(sentenceDocument);

			CoreSentence taggedSentence = sentenceDocument.sentences().get(0);

			return taggedSentence.tokens().stream()
					.map(token -> new POSTag(token.word(), token.get(CoreAnnotations.PartOfSpeechAnnotation.class)))
					.collect(Collectors.toList());
		}).collect(Collectors.toList());

		writePOSOutput(SentencePOSTags);
	}

	public static List<String> loadSentenceFile(String path) {
		try {
			return Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			System.err.println("Failed to read sentence file");
			e.printStackTrace();
			System.exit(-2);
		}

		// Unreachable
		return null;
	}

	public static void writePOSOutput(List<List<POSTag>> sentencePOSTags) {
		try {
			FileWriter writer = new FileWriter(OUTPUT_FILE);
			CSVWriter csvWriter = new CSVWriter(writer);

			for (List<POSTag> sentence : sentencePOSTags) {
				String[] words = sentence.stream().map(POSTag::getWord).toArray(String[]::new);
				String[] posTags = sentence.stream().map(POSTag::getTag).toArray(String[]::new);

				csvWriter.writeNext(words);
				csvWriter.writeNext(posTags);
			}

			csvWriter.close();
		} catch (IOException e) {
			System.err.println("Failed to write output");
			System.exit(-3);
		}
	}
}