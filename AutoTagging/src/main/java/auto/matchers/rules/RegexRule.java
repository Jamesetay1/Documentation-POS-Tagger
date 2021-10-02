package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

public class RegexRule implements Rule {
	/**
	 * The regex to check against
	 */
	private final String regex;

	public RegexRule(String regex) {
		this.regex = regex;
	}

	@Override
	public boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index) {
		return tokens.get(index).matches(regex);
	}
}
