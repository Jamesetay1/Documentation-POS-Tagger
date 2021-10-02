package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

/**
 * A rule that directly maps a keyword or symbol into a tag
 */
public class KeywordRule implements Rule {
	/**
	 * The text of the keyword
	 */
	private final String keyword;

	public KeywordRule(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}


	@Override
	public boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index) {
		return keyword.equals(tokens.get(index));
	}
}
