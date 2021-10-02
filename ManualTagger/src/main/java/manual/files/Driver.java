package manual.files;

import manual.models.TaggableToken;

import java.io.IOException;

public class Driver {
	TagScanner scanner;

	TaggableToken curToken;

	public Driver(String directory) throws IOException {
		scanner = new TagScanner(directory);
	}

	public TaggableToken getNext() throws IOException {
		curToken = scanner.nextTaggableToken();
		return curToken;
	}

	public void updateCurrent(String newTag) throws IOException {
		scanner.updateCurrent(newTag);
	}

	public String getCurFileName() {
		return scanner.getCurFileName();
	}
}
