package auto.tagging;

import auto.models.TaggedToken;
import auto.models.matchers.Matcher;
import auto.models.matchers.MatcherSet;
import auto.matchers.rules.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeTagger {
	public static List<TaggedToken> tag(MatcherSet ruleSet, List<String> tokens) {
		List<IndexedToken> tokensLeft = IntStream.range(0, tokens.size())
				.mapToObj(i -> new IndexedToken(tokens.get(i), i))
				.collect(Collectors.toList());

		List<TaggedToken> taggedTokens = new ArrayList<>(Collections.nCopies(tokens.size(), null));

		int numTagged = 0;
		while (numTagged - 1 < tokens.size()) {
			int curIterationNumTagged = 0;

			var iterator = tokensLeft.iterator();
			while (iterator.hasNext()) {
				var curToken = iterator.next();

				for (Matcher matcher : ruleSet.getMatchers()) {
					boolean matches = true;
					for (Rule rule : matcher.getRules()) {
						if (!rule.matches(tokens, taggedTokens, curToken.index)) {
							matches = false;
							break;
						}
					}

					if (matches) {
						iterator.remove();
						taggedTokens.set(curToken.index, new TaggedToken(matcher.getTag(), curToken.token));

						numTagged++;
						curIterationNumTagged++;
						break;
					}
				}
			}

			if (curIterationNumTagged == 0) {
				// We've tagged all we can

				// Add tokens that couldn't be identified with a null tag
				for (IndexedToken token : tokensLeft) {
					taggedTokens.set(token.index, new TaggedToken(null, token.token));
				}

				break;
			}
		}

		return taggedTokens;
	}

	private static class IndexedToken {
		private String token;
		private int index;

		public IndexedToken(String token, int index) {
			this.token = token;
			this.index = index;
		}
	}
}
