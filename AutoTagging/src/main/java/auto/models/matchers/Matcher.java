package auto.models.matchers;

import auto.matchers.rules.Rule;

import java.util.List;

public class Matcher {
	private final List<Rule> rules;
	private final String tag;

	public Matcher(List<Rule> rules, String tag) {
		this.rules = rules;
		this.tag = tag;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public String getTag() {
		return tag;
	}
}
