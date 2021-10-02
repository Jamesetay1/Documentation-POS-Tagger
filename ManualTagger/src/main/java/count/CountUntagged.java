package count;

import manual.files.TagScanner;

import java.io.IOException;

public class CountUntagged {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Missing directory argument");
			System.out.println("Usage: CountTagged <directory to scan>");
			System.exit(1);
		}

		TagScanner scanner = new TagScanner(args[0]);

		int count = 0;
		while (scanner.nextTaggableToken() != null) {
			count++;
		}

		System.out.println("There are " + count + " untagged tokens");
	}
}
