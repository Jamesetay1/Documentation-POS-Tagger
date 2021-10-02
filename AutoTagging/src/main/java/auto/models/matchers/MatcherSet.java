package auto.models.matchers;

import java.util.List;

public class MatcherSet {
	private final List<Matcher> matchers;

	public MatcherSet(List<Matcher> matchers) {
		this.matchers = matchers;
	}

	public List<Matcher> getMatchers() {
		return matchers;
	}
}
