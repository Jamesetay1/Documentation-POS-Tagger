package models;

public class TaggedToken {
	public String tag;
	public String token;

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

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setToken(String token) {
		this.token = token;
	}
}