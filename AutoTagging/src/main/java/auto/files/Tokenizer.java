package auto.files;

import auto.models.TaggedToken;
import auto.models.TokenInput;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Tokenizer {
	private static final Properties POS_PROPERTIES;
	static {
		POS_PROPERTIES = new Properties();
		POS_PROPERTIES.put("annotators", "tokenize, ssplit");
	}


	public static void TokenizeFile(String fileName) throws IOException {
		String text = Files.readString(Path.of(fileName));

		CoreDocument sentenceDocument = new CoreDocument(text);

		StanfordCoreNLP pipeline = new StanfordCoreNLP(POS_PROPERTIES);
		pipeline.annotate(sentenceDocument);

		List<TokenInput> tokens = new ArrayList<>();
		sentenceDocument.sentences().forEach(sentence -> {
			tokens.addAll(sentence.tokensAsStrings().stream()
				.map(token -> new TokenInput(token, false))
				.collect(Collectors.toList()));
		});

		JSONArray tokensArray = new JSONArray(tokens);

		JSONObject output = new JSONObject();
		output.put("type", "tokens");
		output.put("tokens", tokensArray);

		String outputFileName = stripExtension(fileName) + ".json";
		Files.writeString(Path.of(outputFileName), output.toString(2), StandardOpenOption.CREATE);
	}

	private static String stripExtension(String fileName) {
		int pos = fileName.lastIndexOf('.');
		if (pos > 0) {
			return fileName.substring(0, pos);
		}

		return fileName;
	}
}
