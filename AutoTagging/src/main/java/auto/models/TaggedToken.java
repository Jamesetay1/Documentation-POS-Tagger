package auto.models;

public class TaggedToken {
	private final String tag;
	private final String token;

	public TaggedToken(String tag, String token) {
		this.tag = tag;
		this.token = token;
	}

	public String getTag() {
		return tag;
	}

	public String getToken() {
		return token;
	}
}
