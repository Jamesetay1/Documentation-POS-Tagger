package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

/**
 * A rule that matches the tokens around it
 */
public class OffsetRule implements Rule {
	/**
	 * The offset from the current word to look at
	 */
	private final int offset;

	/**
	 * The tag of the already matched token to look for at that location
	 */
	private final String tag;

	public OffsetRule(int offset, String tag) {
		this.offset = offset;
		this.tag = tag;
	}

	public int getOffset() {
		return offset;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index) {
		int newIndex = index + offset;

		if (0 > newIndex || newIndex >= tokens.size()) {
			return false;
		}

		if (taggedTokens.get(newIndex) == null) {
			return false;
		}

		return tag.equals(taggedTokens.get(newIndex).getTag());
	}
}
