package auto.matchers.rules;

import auto.models.TaggedToken;

import java.util.List;

public interface Rule {
	boolean matches(List<String> tokens, List<TaggedToken> taggedTokens, int index);
}
