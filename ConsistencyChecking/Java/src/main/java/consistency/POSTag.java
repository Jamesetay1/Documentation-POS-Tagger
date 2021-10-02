package consistency;

public class POSTag {
	private String word;
	private String tag;

	public POSTag(String word, String tag) {
		this.word = word;
		this.tag = tag;
	}

	public String getWord() {
		return word;
	}

	public String getTag() {
		return tag;
	}
}
