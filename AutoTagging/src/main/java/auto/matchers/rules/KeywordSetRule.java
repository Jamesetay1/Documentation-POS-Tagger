package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

public class KeywordSetRule implements Rule {
	/**
	 * The text of the keyword
	 */
	private final List<String> keywords;

	public KeywordSetRule(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getKeyword() {
		return keywords;
	}


	@Override
	public boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index) {
		for (String keyword : keywords) {
			if (keyword.equals(tokens.get(index))) {
				return true;
			}
		}

		return false;
	}
}
