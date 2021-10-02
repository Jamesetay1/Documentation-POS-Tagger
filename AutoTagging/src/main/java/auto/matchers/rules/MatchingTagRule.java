package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

public class MatchingTagRule implements Rule {
	private final String tag;

	public MatchingTagRule(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index) {
		for (TaggedToken taggedToken : taggedTokens) {
			if (taggedToken != null) {
				if (tag.equals(taggedToken.getTag()) && tokens.get(index).equals(taggedToken.getToken())) {
					return true;
				}
			}
		}

		return false;
	}
}
