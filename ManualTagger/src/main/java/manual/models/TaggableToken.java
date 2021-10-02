package manual.models;

public class TaggableToken {
	public String token;
	public String[] preContext;
	public String[] postContext;

	public TaggableToken(String token, String[] preContext, String[] postContext) {
		this.token = token;
		this.preContext = preContext;
		this.postContext = postContext;
	}

	public String getToken() {
		return token;
	}

	public String[] getPreContext() {
		return preContext;
	}

	public String[] getPostContext() {
		return postContext;
	}
}
