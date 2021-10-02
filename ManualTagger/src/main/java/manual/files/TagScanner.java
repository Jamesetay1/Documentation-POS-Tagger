package manual.files;

import manual.models.TaggableToken;
import manual.models.TaggedToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagScanner {
	private static final int MAX_CONTEXT = 3;

	private FileScanner scanner;

	private List<TaggedToken> curTokens;
	private int i;

	public TagScanner(String directory) throws IOException {
		scanner = new FileScanner(directory);

		loadNextTokens();
	}

	public void updateCurrent(String newTag) throws IOException {
		// Update the i - 1 token, because is i is incremented after nextTaggableToken is called
		curTokens.get(i - 1).setTag(newTag);
		scanner.updateCurrent(curTokens);
	}

	private void loadNextTokens() throws IOException {
		curTokens = scanner.nextTokens();
		i = 0;
	}

	public TaggableToken nextTaggableToken() throws IOException {
		while (true) {
			if (i >= curTokens.size()) {
				loadNextTokens();

				if (curTokens == null) {
					break;
				}
			}

			var cur = curTokens.get(i);
			i++;

			if (cur.tag == null || "".equals(cur.tag)) {
				return new TaggableToken(cur.token, getPreContext(i - 1), getPostContext(i - 1));
			}
		}

		return null;
	}

	private String[] getPreContext(int i) {
		List<String> context = new ArrayList<>();
		for (int j = i - MAX_CONTEXT; j < i; j++) {
			if (j >= 0) {
				context.add(curTokens.get(j).token);
			}
		}

		return context.toArray(new String[0]);
	}

	private String[] getPostContext(int i) {
		List<String> context = new ArrayList<>();
		for (int j = i + 1; j <= i + MAX_CONTEXT; j++) {
			if (j < curTokens.size()) {
				context.add(curTokens.get(j).token);
			}
		}

		return context.toArray(new String[0]);
	}

	public String getCurFileName() {
		return scanner.getCurFileName();
	}
}
