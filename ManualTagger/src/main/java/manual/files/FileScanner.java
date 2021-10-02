package manual.files;

import manual.models.TaggedToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {
	private List<Path> jsonFiles;
	private int i;

	public FileScanner(String directory) throws IOException {
		jsonFiles = scanForJSONFiles(directory);
		i = 0;
	}

	public void updateCurrent(List<TaggedToken> tokens) throws IOException {
		var tokensJson = new JSONArray(tokens);

		var outputJson = new JSONObject();
		outputJson.put("type", "autotagged");
		outputJson.put("tokens", tokensJson);

		updateCurrent(outputJson);
	}

	private void updateCurrent(JSONObject updated) throws IOException {
		Files.writeString(jsonFiles.get(i - 1), updated.toString(2), StandardOpenOption.CREATE);
	}

	private JSONObject nextFile() throws IOException {
		if (i >= jsonFiles.size()) {
			return null;
		}

		String text = Files.readString(jsonFiles.get(i));
		i++;

		JSONObject obj;
		try {
			obj = new JSONObject(text);
		} catch (JSONException exc) {
			return nextFile();
		}

		if ("autotagged".equals(obj.getString("type"))) {
			return obj;
		} else {
			return nextFile();
		}
	}

	public List<TaggedToken> nextTokens() throws IOException {
		var nextFile = nextFile();
		if (nextFile == null) {
			return null;
		}

		var tokensJson = nextFile.optJSONArray("tokens");
		if (tokensJson == null) {
			return nextTokens();
		}

		List<TaggedToken> tokens = new ArrayList<>();
		for (Object obj : tokensJson) {
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;

				tokens.add(new TaggedToken(jsonObj.optString("tag", null), jsonObj.optString("token", null)));
			}
		}

		return tokens;
	}

	public static List<Path> scanForJSONFiles(String directory) throws IOException {
		List<Path> files = new ArrayList<>();

		Path start = Path.of(directory);
		Files.walkFileTree(start, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (file.toString().endsWith(".json")) {
					files.add(file);
				}

				return FileVisitResult.CONTINUE;
			}
		});

		return files;
	}

	public String getCurFileName() {
		return jsonFiles.get(i - 1).toString();
	}
}
