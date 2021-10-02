package auto.models;

public class TokenInput {
	private String token;
	private boolean code;

	public TokenInput(String token, boolean code) {
		this.token = token;
		this.code = code;
	}

	public String getToken() {
		return token;
	}

	public boolean isCode() {
		return code;
	}
}
