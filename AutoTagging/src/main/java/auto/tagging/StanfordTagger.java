package auto.tagging;

import auto.models.TaggedToken;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StanfordTagger {
	private static final Properties POS_PROPERTIES;
	static {
		POS_PROPERTIES = new Properties();
		POS_PROPERTIES.put("annotators", "tokenize, ssplit, pos");
	}

	public static List<TaggedToken> tag(List<String> tokens) {
		// Form the tokens into a string so that we can tag it using Stanford NLP
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			sb.append(token);
			sb.append(' ');
		}

		CoreDocument sentenceDocument = new CoreDocument(sb.toString());

		StanfordCoreNLP pipeline = new StanfordCoreNLP(POS_PROPERTIES);
		pipeline.annotate(sentenceDocument);

		List<String> tags = new ArrayList<>();
		sentenceDocument.sentences().forEach(sentence -> {
			tags.addAll(sentence.posTags());
		});

		if (tags.size() != tokens.size()) {
			throw new IllegalStateException("Number of tokens doesn't match the number of tags");
		}

		return IntStream.range(0, tokens.size())
				.mapToObj(i -> new TaggedToken(tags.get(i), tokens.get(i)))
				.collect(Collectors.toList());
	}
}
