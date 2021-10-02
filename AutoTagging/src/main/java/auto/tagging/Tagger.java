package auto.tagging;

import auto.models.TaggedToken;
import auto.models.TokenInput;
import auto.models.matchers.MatcherSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tagger {
	public static List<TaggedToken> tag(MatcherSet ruleSet, List<TokenInput> tokens) {
		List<String> codeTokens = tokens.stream()
				.filter(TokenInput::isCode)
				.map(TokenInput::getToken)
				.collect(Collectors.toList());

		List<String> nonCodeTokens = tokens.stream()
				.filter(token -> !token.isCode())
				.map(TokenInput::getToken)
				.collect(Collectors.toList());

		List<TaggedToken> taggedCodeTokens = CodeTagger.tag(ruleSet, codeTokens);
		List<TaggedToken> taggedNonCodeTokens = StanfordTagger.tag(nonCodeTokens);

		// Stitch the two back together
		int curCodeIndex = 0;
		int curNonCodeIndex = 0;

		List<TaggedToken> taggedTokens = new ArrayList<>();

		for (TokenInput token : tokens) {
			if (token.isCode()) {
				taggedTokens.add(taggedCodeTokens.get(curCodeIndex));
				curCodeIndex++;
			} else {
				taggedTokens.add(taggedNonCodeTokens.get(curNonCodeIndex));
				curNonCodeIndex++;
			}
		}

		return taggedTokens;
	}
}
