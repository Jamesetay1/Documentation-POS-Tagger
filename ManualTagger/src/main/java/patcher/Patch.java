package patcher;

import manual.files.TagScanner;
import manual.models.TaggableToken;

import java.io.IOException;

public class Patch {
	private static final String TOKEN = "Object";
	private static final String TAG = "<typen>";

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Missing directory argument");
			System.out.println("Usage: CountTagged <directory to scan>");
			System.exit(1);
		}

		TagScanner scanner = new TagScanner(args[0]);

		int count = 0;
		TaggableToken curToken;
		while ((curToken = scanner.nextTaggableToken()) != null) {
			if (TOKEN.equals(curToken.token)) {
				scanner.updateCurrent(TAG);
				count++;
			}
		}

		System.out.println("Patched " + count + " tokens");
	}
}
