package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

public class OrRule implements Rule {
	private final List<Rule> rules;

	public OrRule(List<Rule> rules) {
		this.rules = rules;
	}

	public List<Rule> getRules() {
		return rules;
	}

	@Override
	public boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index) {
		for (Rule rule : rules) {
			if (rule.matches(tokens, taggedTokens, index)) {
				return true;
			}
		}

		return false;
	}
}
